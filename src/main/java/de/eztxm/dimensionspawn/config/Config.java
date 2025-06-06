package de.eztxm.dimensionspawn.config;

import de.eztxm.dimensionspawn.DimensionSpawn;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.ModConfigSpec;

// An eample config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Forge's config APIs
@EventBusSubscriber(modid = DimensionSpawn.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class Config {    pub

    public static ForgeConfigSpec.IntValue safeSpawnRange;

    public static ModConfigSpec.BooleanValue useDimensionEntry;
    public static ModConfigSpec.ConfigValue<String> dimensionEntry;

    public static ModConfigSpec.BooleanValue useCoordinatesEntry;
    public static ModConfigSpec.DoubleValue xEntry;
    public static ModConfigSpec.DoubleValue yEntry;
    public static ModConfigSpec.DoubleValue zEntry;
    public static ModConfigSpec.DoubleValue yawEntry;
    public static ModConfigSpec.DoubleValue pitchEntry;

    public static ModConfigSpec config;

    static {
        ModConfigSpec.Builder configBuilder = new ModConfigSpec.Builder();
        setupConfig(configBuilder);
        config = configBuilder.build();
    }

    private static void setupConfig(ModConfigSpec.Builder builder) {
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
