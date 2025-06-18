package de.eztxm.dimensionspawn.config;

import net.fabricmc.loader.api.FabricLoader;
import java.nio.file.Path;
import java.util.Properties;
import java.io.*;

public class Config {
    private static final String CONFIG_FILE = "dimensionspawn.properties";
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve(CONFIG_FILE);

    public boolean safeSpawn = false;
    public int safeSpawnRange = 100;

    public boolean useDimensionEntry = false;
    public String dimensionEntry = "minecraft:overworld";

    public boolean useCoordinatesEntry = false;
    public double xEntry = 0D;
    public double yEntry = 0D;
    public double zEntry = 0D;
    public float yawEntry = 0.0F;
    public float pitchEntry = 0.0F;

    private static Config INSTANCE;

    public static Config get() {
        if (INSTANCE == null) {
            INSTANCE = new Config();
            INSTANCE.load();
        }
        return INSTANCE;
    }

    public void load() {
        Properties props = new Properties();
        if (CONFIG_PATH.toFile().exists()) {
            try (FileInputStream in = new FileInputStream(CONFIG_PATH.toFile())) {
                props.load(in);
                safeSpawn = Boolean.parseBoolean(props.getProperty("safeSpawn", "false"));
                safeSpawnRange = Integer.parseInt(props.getProperty("safeSpawnRange", "100"));
                useDimensionEntry = Boolean.parseBoolean(props.getProperty("useDimensionEntry", "false"));
                dimensionEntry = props.getProperty("dimensionEntry", "minecraft:overworld");
                useCoordinatesEntry = Boolean.parseBoolean(props.getProperty("useCoordinatesEntry", "false"));
                xEntry = Double.parseDouble(props.getProperty("xEntry", "0.0"));
                yEntry = Double.parseDouble(props.getProperty("yEntry", "0.0"));
                zEntry = Double.parseDouble(props.getProperty("zEntry", "0.0"));
                yawEntry = Float.parseFloat(props.getProperty("yawEntry", "0.0"));
                pitchEntry = Float.parseFloat(props.getProperty("pitchEntry", "0.0"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            save(); // Erstellt Standarddatei, falls nicht vorhanden
        }
    }

    public void save() {
        Properties props = new Properties();
        props.setProperty("safeSpawn", Boolean.toString(safeSpawn));
        props.setProperty("safeSpawnRange", Integer.toString(safeSpawnRange));
        props.setProperty("useDimensionEntry", Boolean.toString(useDimensionEntry));
        props.setProperty("dimensionEntry", dimensionEntry);
        props.setProperty("useCoordinatesEntry", Boolean.toString(useCoordinatesEntry));
        props.setProperty("xEntry", Double.toString(xEntry));
        props.setProperty("yEntry", Double.toString(yEntry));
        props.setProperty("zEntry", Double.toString(zEntry));
        props.setProperty("yawEntry", Double.toString(yawEntry));
        props.setProperty("pitchEntry", Double.toString(pitchEntry));
        try (FileOutputStream out = new FileOutputStream(CONFIG_PATH.toFile())) {
            props.store(out, "DimensionSpawn Config");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
