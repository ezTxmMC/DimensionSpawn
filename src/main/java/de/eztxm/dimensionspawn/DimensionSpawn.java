package de.eztxm.dimensionspawn;

import de.eztxm.dimensionspawn.config.Config;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.NeoForge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(DimensionSpawn.MOD_ID)
public class DimensionSpawn {
    public static final String MOD_ID = "dimensionspawn";
    public static final Logger LOGGER = LogManager.getLogger();

    public DimensionSpawn(IEventBus modEventBus, ModContainer modContainer) {
        NeoForge.EVENT_BUS.register(this);
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.config, "dimensionspawn.toml");
    }
}
