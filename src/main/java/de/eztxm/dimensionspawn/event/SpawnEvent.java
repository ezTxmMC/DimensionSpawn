package de.eztxm.dimensionspawn.event;

import de.eztxm.dimensionspawn.DimensionSpawn;
import de.eztxm.dimensionspawn.config.Config;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

import java.util.Collections;
import java.util.Objects;
import java.util.function.Function;

@EventBusSubscriber(modid = DimensionSpawn.MOD_ID, bus = EventBusSubscriber.Bus.FORGE)
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
                if (player.getRespawnPosition() == null) {
                    handleTeleport(player);
                }
            }
        }
    }

    private static void handleTeleport(Player player) {
        boolean useDimension = Config.useDimensionEntry.get();
        boolean useCoordinates = Config.useCoordinatesEntry.get();
        if (useDimension) {
            String[] dimensionSplit = Config.dimensionEntry.get().split(":");
            ResourceKey<Level> dimensionKey = ResourceKey.create(Registries.DIMENSION, new ResourceLocation(dimensionSplit[0], dimensionSplit[1]));
            Level level = player.level();
            ServerLevel dimension = Objects.requireNonNull(level.getServer()).getLevel(dimensionKey);
            assert dimension != null;
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
}
