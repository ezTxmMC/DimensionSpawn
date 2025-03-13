package de.eztxm.dimensionspawn;

import de.eztxm.dimensionspawn.config.Config;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(DimensionSpawn.MOD_ID)
public class DimensionSpawn {
    public static final String MOD_ID = "dimensionspawn";
    public static final Logger LOGGER = LogManager.getLogger();

    public DimensionSpawn() {
        MinecraftForge.EVENT_BUS.register(this);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.config, "dimensionspawn.toml");
    }
}
