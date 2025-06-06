package de.eztxm.dimensionspawn.config;

import de.eztxm.dimensionspawn.DimensionSpawn;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DimensionSpawn.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {
    public static ForgeConfigSpec.BooleanValue safeSpawn;
    public static ForgeConfigSpec.IntValue safeSpawnRange;

    public static ForgeConfigSpec.BooleanValue useDimensionEntry;
    public static ForgeConfigSpec.ConfigValue<String> dimensionEntry;

    public static ForgeConfigSpec.BooleanValue useCoordinatesEntry;
    public static ForgeConfigSpec.DoubleValue xEntry;
    public static ForgeConfigSpec.DoubleValue yEntry;
    public static ForgeConfigSpec.DoubleValue zEntry;
    public static ForgeConfigSpec.DoubleValue yawEntry;
    public static ForgeConfigSpec.DoubleValue pitchEntry;

    public static ForgeConfigSpec config;

    static {
        ForgeConfigSpec.Builder configBuilder = new ForgeConfigSpec.Builder();
        setupConfig(configBuilder);
        config = configBuilder.build();
    }

    private static void setupConfig(ForgeConfigSpec.Builder builder) {
        builder.comment(" Welcome to the DimensionSpawn config.\n Here you can set the dimension and the coordinates for player spawning and respawning.");

        builder.push("General");
        safeSpawn = builder
                .comment("Here you can enable and disable the usage of a safe spawnpoint.")
                .define("safeSpawn", false);
        safeSpawnRange = builder
                .comment("Here you set the range of the safe spawn search.")
                .defineInRange("safeSpawnRange", 100, 0, 10000);

        builder.pop();
        builder.push("Dimension");
        useDimensionEntry = builder
                .comment("Here you can enable and disable the usage of a dimension.")
                .define("useDimension", false);
        dimensionEntry = builder
                .comment(" There you can define a default dimension to spawn. Syntax: modid:world_id. You can use '/execute in' tab-completion to find a list of available dimensions.")
                .define("dimension", "minecraft:overworld");

        builder.pop();
        builder.push("Coordinates");
        useCoordinatesEntry = builder
                .comment("Here you can enable and disable the usage of a coordinates.")
                .define("useCoordinates", false);

        xEntry = builder.defineInRange("x", 0D, -Double.MAX_VALUE, Double.MAX_VALUE);
        yEntry = builder.defineInRange("y", 0D, -Double.MAX_VALUE, Double.MAX_VALUE);
        zEntry = builder.defineInRange("z", 0D, -Double.MAX_VALUE, Double.MAX_VALUE);
        yawEntry = builder.defineInRange("yaw", 0.0, -180.0, 180.0);
        pitchEntry = builder.defineInRange("pitch", 0.0, -90.0, 90.0);

        builder.pop();
    }
}
