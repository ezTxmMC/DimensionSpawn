package de.eztxm.dimensionspawn.event;

import de.eztxm.dimensionspawn.config.Config;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.DimensionTransition;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import java.util.Collections;
import java.util.Objects;

public class SpawnEvent {

    @SubscribeEvent
    public void onPlayerFirstJoin(PlayerEvent.PlayerLoggedInEvent event) {
        ServerPlayer player = (ServerPlayer) event.getEntity();
        int statCounter = player.getStats().getValue(Stats.CUSTOM.get(Stats.LEAVE_GAME));
        if (statCounter == 0) {
            handleTeleport(player);
        }
    }

    @SubscribeEvent
    public void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        ServerPlayer player = (ServerPlayer) event.getEntity();
        if (!event.isEndConquered() && player.getRespawnPosition() == null) {
            handleTeleport(player);
        }
    }

    private void handleTeleport(Player player) {
        boolean useDimension = Config.useDimensionEntry.get();
        boolean useCoordinates = Config.useCoordinatesEntry.get();
        if (useDimension) {
            String[] dimensionSplit = Config.dimensionEntry.get().split(":");
            ResourceKey<Level> dimensionKey = ResourceKey.create(Registries.DIMENSION, ResourceLocation.fromNamespaceAndPath(dimensionSplit[0], dimensionSplit[1]));
            Level level = player.level();
            ServerLevel dimension = Objects.requireNonNull(level.getServer()).getLevel(dimensionKey);
            if (dimension == null) {
                player.sendSystemMessage(Component.literal("[DimensionSpawn] The dimension " + dimensionKey + " does not exist in this instance."));
                return;
            }
            DimensionTransition transition = dimensionTransition(player, dimension);
            if (transition == null) {
                return;
            }
            player.changeDimension(transition);
            return;
        }
        if (useCoordinates) {
            double x = Config.xEntry.get();
            double y = Config.yEntry.get();
            double z = Config.zEntry.get();
            float cYaw = Config.yawEntry.get().floatValue();
            float cPitch = Config.pitchEntry.get().floatValue();
            player.teleportTo((ServerLevel) player.level(), x, y, z, Collections.emptySet(), cYaw, cPitch);
        }
    }

    private DimensionTransition dimensionTransition(Entity entity, ServerLevel destWorld) {
        boolean useCoordinates = Config.useCoordinatesEntry.get();
        boolean safeSpawn = Config.safeSpawn.get();
        int safeSpawnRange = Config.safeSpawnRange.get();
        if (useCoordinates) {
            double x = Config.xEntry.get();
            double y = Config.yEntry.get();
            double z = Config.zEntry.get();
            float cYaw = Config.yawEntry.get().floatValue();
            float cPitch = Config.pitchEntry.get().floatValue();
            if (safeSpawn) {
                BlockPos blockPos = new BlockPos((int) x, (int) y, (int) z);
                BlockPos safeBlockPos = validPlayerSpawnLocation(destWorld, blockPos, safeSpawnRange);
                if (safeBlockPos == null) {
                    return new DimensionTransition(destWorld, entity, entity1 -> entity1.teleportTo(destWorld, x, y, z, Collections.emptySet(), cYaw, cPitch));
                }
                entity.teleportTo(destWorld, safeBlockPos.getX(), safeBlockPos.getY(), safeBlockPos.getZ(), Collections.emptySet(), cYaw, cPitch);
                return new DimensionTransition(destWorld, entity, entity1 -> entity1.teleportTo(destWorld, safeBlockPos.getX(), safeBlockPos.getY(), safeBlockPos.getZ(), Collections.emptySet(), cYaw, cPitch));
            }
            return new DimensionTransition(destWorld, entity, entity1 -> entity1.teleportTo(destWorld, x, y, z, Collections.emptySet(), cYaw, cPitch));
        }
        return null;
    }

    private BlockPos validPlayerSpawnLocation(ServerLevel world, BlockPos position, int maximumRange) {
        BlockPos.MutableBlockPos currentPos = new BlockPos.MutableBlockPos();
        for (int range = 0; range < maximumRange; range++) {
            int radiusSq = range * range;
            int outerRadiusSq = (range + 1) * (range + 1);
            for (int yOffset = -range; yOffset <= range; yOffset++) {
                for (int xOffset = -range; xOffset <= range; xOffset++) {
                    for (int zOffset = -range; zOffset <= range; zOffset++) {
                        int distanceSq = xOffset * xOffset + yOffset * yOffset + zOffset * zOffset;
                        if (distanceSq >= radiusSq && distanceSq < outerRadiusSq) {
                            currentPos.set(position.getX() + xOffset, position.getY() + yOffset, position.getZ() + zOffset);
                            if (world.getBlockState(currentPos.below()).canOcclude() &&
                                    world.getBlockState(currentPos).isAir() &&
                                    world.getBlockState(currentPos.above()).isAir()) {
                                return currentPos;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }
}
