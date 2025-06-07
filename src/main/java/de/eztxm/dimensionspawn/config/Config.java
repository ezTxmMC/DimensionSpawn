package de.eztxm.dimensionspawn.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static ModConfigSpec SPEC;
    public static ModConfigSpec.BooleanValue safeSpawn;
    public static ModConfigSpec.IntValue safeSpawnRange;

    public static ModConfigSpec.BooleanValue useDimensionEntry;
    public static ModConfigSpec.ConfigValue<String> dimensionEntry;

    public static ModConfigSpec.BooleanValue useCoordinatesEntry;
    public static ModConfigSpec.DoubleValue xEntry;
    public static ModConfigSpec.DoubleValue yEntry;
    public static ModConfigSpec.DoubleValue zEntry;
    public static ModConfigSpec.DoubleValue yawEntry;
    public static ModConfigSpec.DoubleValue pitchEntry;

    public static void setupConfig() {
        BUILDER.comment(" Welcome to the DimensionSpawn config.\n Here you can set the dimension and the coordinates for player spawning and respawning.");

        BUILDER.push("General");
        safeSpawn = BUILDER
                .comment("Here you can enable and disable the usage of a safe spawnpoint.")
                .define("safeSpawn", false);
        safeSpawnRange = BUILDER
                .comment("Here you set the range of the safe spawn search.")
                .defineInRange("safeSpawnRange", 100, 0, 10000);

        BUILDER.pop();
        BUILDER.push("Dimension");
        useDimensionEntry = BUILDER
                .comment("Here you can enable and disable the usage of a dimension.")
                .define("useDimension", false);
        dimensionEntry = BUILDER
                .comment(" There you can define a default dimension to spawn. Syntax: modid:world_id. You can use '/execute in' tab-completion to find a list of available dimensions.")
                .define("dimension", "minecraft:overworld");

        BUILDER.pop();
        BUILDER.push("Coordinates");
        useCoordinatesEntry = BUILDER
                .comment("Here you can enable and disable the usage of a coordinates.")
                .define("useCoordinates", false);

        xEntry = BUILDER.defineInRange("x", 0D, -Double.MAX_VALUE, Double.MAX_VALUE);
        yEntry = BUILDER.defineInRange("y", 0D, -Double.MAX_VALUE, Double.MAX_VALUE);
        zEntry = BUILDER.defineInRange("z", 0D, -Double.MAX_VALUE, Double.MAX_VALUE);
        yawEntry = BUILDER.defineInRange("yaw", 0.0, -180.0, 180.0);
        pitchEntry = BUILDER.defineInRange("pitch", 0.0, -90.0, 90.0);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
