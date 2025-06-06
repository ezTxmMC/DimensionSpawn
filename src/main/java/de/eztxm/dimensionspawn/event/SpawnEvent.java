package de.eztxm.dimensionspawn.event;

import de.eztxm.dimensionspawn.DimensionSpawn;
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
import net.minecraftforge.common.util.ITeleporter;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Collections;
import java.util.Objects;
import java.util.function.Function;

@Mod.EventBusSubscriber(modid = DimensionSpawn.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SpawnEvent {

    @SubscribeEvent
    public static void onPlayerFirstJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() != null) {
            ServerPlayer player = (ServerPlayer) event.getEntity();
            if (player != null) {
                int statCounter = player.getStats().getValue(Stats.CUSTOM.get(Stats.LEAVE_GAME));
                if (statCounter == 0) {
                    handleTeleport(player);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (event.getEntity() != null) {
            ServerPlayer player = (ServerPlayer) event.getEntity();
            if (player != null) {
                if (!event.isEndConquered() && player.getRespawnPosition() == null) {
                    handleTeleport(player);
                }
            }
        }
    }

    private static void handleTeleport(Player player) {
        boolean useDimension = Config.useDimensionEntry.get();
        boolean useCoordinates = Config.useCoordinatesEntry.get();
        boolean safeSpawn = Config.safeSpawn.get();
        int safeSpawnRange = Config.safeSpawnRange.get();
        if (useDimension) {
            String[] dimensionSplit = Config.dimensionEntry.get().split(":");
            ResourceKey<Level> dimensionKey = ResourceKey.create(Registries.DIMENSION, new ResourceLocation(dimensionSplit[0], dimensionSplit[1]));
            Level level = player.level();
            ServerLevel dimension = Objects.requireNonNull(level.getServer()).getLevel(dimensionKey);
            if (dimension == null) {
                player.sendSystemMessage(Component.literal("[DimensionSpawn] The dimension " + dimensionKey + " does not exist in this instance."));
                return;
            }
            player.changeDimension(dimension, new ITeleporter() {
                @Override
                public Entity placeEntity(Entity entity, ServerLevel currentWorld, ServerLevel destWorld, float yaw, Function<Boolean, Entity> repositionEntity) {
                    entity = repositionEntity.apply(false);
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
                                entity.teleportTo(destWorld, x, y, z, Collections.emptySet(), cYaw, cPitch);
                                return entity;
                            }
                            entity.teleportTo(destWorld, safeBlockPos.getX(), safeBlockPos.getY(), safeBlockPos.getZ(), Collections.emptySet(), cYaw, cPitch);
                            return entity;
                        }
                        entity.teleportTo(destWorld, x, y, z, Collections.emptySet(), cYaw, cPitch);
                    }
                    return entity;
                }
                @Override
                public boolean playTeleportSound(ServerPlayer player, ServerLevel sourceWorld, ServerLevel destWorld) {
                    return false;
                }
            });
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

    public static BlockPos validPlayerSpawnLocation(ServerLevel world, BlockPos position, int maximumRange) {
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
