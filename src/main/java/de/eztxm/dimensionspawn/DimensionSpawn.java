package de.eztxm.dimensionspawn;

import de.eztxm.dimensionspawn.config.Config;
import de.eztxm.dimensionspawn.event.SpawnEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.NeoForge;

@Mod(DimensionSpawn.MOD_ID)
public class DimensionSpawn {
    public static final String MOD_ID = "dimensionspawn";

    public DimensionSpawn(ModContainer modContainer) {
        NeoForge.EVENT_BUS.register(new SpawnEvent());
        Config.setupConfig();
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC, "dimensionspawn.toml");
    }
}
