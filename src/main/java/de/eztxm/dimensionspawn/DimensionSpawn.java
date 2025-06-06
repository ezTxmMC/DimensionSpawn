package de.eztxm.dimensionspawn;

import de.eztxm.dimensionspawn.config.Config;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;

@Mod(DimensionSpawn.MOD_ID)
public class DimensionSpawn {
    public static final String MOD_ID = "dimensionspawn";

    public DimensionSpawn(IEventBus eventBus, ModContainer modContainer) {
        eventBus.register(this);
        Config.setupConfig();
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC, "dimensionspawn.toml");
    }
}
