package de.eztxm.dimensionspawn;

import de.eztxm.dimensionspawn.config.Config;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DimensionSpawn implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("dimensionspawn");

    @Override
    public void onInitialize() {
        Config.get();
    }
}
