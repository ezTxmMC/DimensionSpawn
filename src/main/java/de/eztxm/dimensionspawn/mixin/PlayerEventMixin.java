package de.eztxm.dimensionspawn.mixin;

import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.eztxm.dimensionspawn.config.Config;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(PlayerManager.class)
public class PlayerEventMixin {

    @Inject(at = @At(value = "TAIL"), method = "onPlayerConnect")
    private void onPlayerJoin(ClientConnection connection, ServerPlayerEntity player, CallbackInfo ci) {
        if (player.getStatHandler().getStat(Stats.CUSTOM.getOrCreateStat(Stats.LEAVE_GAME)) < 1) {
            if (Config.get().useDimensionEntry) {
                String[] dimensionSplit = Config.get().dimensionEntry.split(":");
                RegistryKey<World> dimensionKey = RegistryKey.of(RegistryKeys.WORLD, new Identifier(dimensionSplit[0], dimensionSplit[1]));
                World world = player.getWorld();
                ServerWorld dimension = Objects.requireNonNull(world.getServer()).getWorld(dimensionKey);
                if (dimension == null) {
                    player.sendMessage(Text.of("[DimensionSpawn] The dimension " + dimensionKey + " does not exist in this instance."));
                    return;
                }
                if (Config.get().safeSpawn) {
                    if (Config.get().useCoordinatesEntry) {
                        BlockPos blockPos = new BlockPos((int) Config.get().xEntry, (int) Config.get().yEntry, (int) Config.get().zEntry);
                        BlockPos safeBlockPos = validPlayerSpawnLocation(dimension, blockPos, Config.get().safeSpawnRange);
                        if (safeBlockPos != null) {
                            player.teleport(dimension, safeBlockPos.getX(), safeBlockPos.getY(), safeBlockPos.getZ(), Config.get().yawEntry, Config.get().pitchEntry);
                            return;
                        }
                        return;
                    }
                    BlockPos safeBlockPos = validPlayerSpawnLocation(dimension, dimension.getSpawnPos(), Config.get().safeSpawnRange);
                    if (safeBlockPos != null) {
                        player.teleport(dimension, safeBlockPos.getX(), safeBlockPos.getY(), safeBlockPos.getZ(), 0f, 0f);
                        return;
                    }
                }
                if (Config.get().useCoordinatesEntry) {
                    player.teleport(dimension, Config.get().xEntry, Config.get().yEntry, Config.get().zEntry, Config.get().yawEntry, Config.get().pitchEntry);
                    return;
                }
                player.teleport(dimension, dimension.getSpawnPos().getX(), dimension.getSpawnPos().getY(), dimension.getSpawnPos().getZ(), 0f, 0f);
                return;
            }
            if (Config.get().useCoordinatesEntry) {
                player.teleport(player.getServerWorld(), Config.get().xEntry, Config.get().yEntry, Config.get().zEntry, Config.get().yawEntry, Config.get().pitchEntry);
            }
        }
    }

    @Inject(at = @At(value = "HEAD"), method = "respawnPlayer")
    private void respawnPlayer(ServerPlayerEntity player, boolean alive, CallbackInfoReturnable<ServerPlayerEntity> cir) {
        if (Objects.equals(player.getServerWorld().getRegistryKey().getRegistry(), new Identifier("minecraft", "the_end"))) {
            ServerWorld overworld = Objects.requireNonNull(player.getServer()).getOverworld();
            player.setSpawnPoint(overworld.getRegistryKey(), overworld.getSpawnPos(), 0, true, false);
            return;
        }
        if (Config.get().useDimensionEntry) {
            String[] dimensionSplit = Config.get().dimensionEntry.split(":");
            RegistryKey<World> dimensionKey = RegistryKey.of(RegistryKeys.WORLD, new Identifier(dimensionSplit[0], dimensionSplit[1]));
            if (Config.get().useCoordinatesEntry) {
                BlockPos blockPos = new BlockPos((int) Config.get().xEntry, (int) Config.get().yEntry, (int) Config.get().zEntry);
                if (Config.get().safeSpawn) {
                    BlockPos safeBlockPos = validPlayerSpawnLocation(Objects.requireNonNull(player.getServer()).getWorld(dimensionKey), blockPos, Config.get().safeSpawnRange);
                    if (safeBlockPos == null) {
                        return;
                    }
                    player.setSpawnPoint(dimensionKey, safeBlockPos, 0, true, false);
                    return;
                }
                player.setSpawnPoint(dimensionKey, blockPos, 0, true, false);
            }
            return;
        }
        if (Config.get().useCoordinatesEntry) {
            World world = player.getWorld();
            BlockPos blockPos = new BlockPos((int) Config.get().xEntry, (int) Config.get().yEntry, (int) Config.get().zEntry);
            player.setSpawnPoint(world.getRegistryKey(), blockPos, 0, true, false);
        }
    }

    @Unique
    private BlockPos validPlayerSpawnLocation(ServerWorld world, BlockPos position, int maximumRange) {
        BlockPos.Mutable currentPos = new BlockPos.Mutable();
        for (int range = 0; range < maximumRange; range++) {
            int radiusSq = range * range;
            int outerRadiusSq = (range + 1) * (range + 1);
            for (int yOffset = -range; yOffset <= range; yOffset++) {
                for (int xOffset = -range; xOffset <= range; xOffset++) {
                    for (int zOffset = -range; zOffset <= range; zOffset++) {
                        int distanceSq = xOffset * xOffset + yOffset * yOffset + zOffset * zOffset;
                        if (distanceSq >= radiusSq && distanceSq < outerRadiusSq) {
                            currentPos.set(
                                    position.getX() + xOffset,
                                    position.getY() + yOffset,
                                    position.getZ() + zOffset
                            );
                            if (world.getBlockState(currentPos.down()).isOpaque()
                                    && world.getBlockState(currentPos).isAir()
                                    && world.getBlockState(currentPos.up()).isAir()) {
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
