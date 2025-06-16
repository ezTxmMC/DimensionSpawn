# DimensionSpawn

## Overview
DimensionSpawn is a lightweight Minecraft mod designed to teleport players to custom coordinates in a specified dimension upon their initial spawn and respawn. Ideal for modded servers, adventure maps, and custom survival worlds!

## Features
- **First Spawn Teleportation:** Automatically teleports players when they first join the server to predetermined coordinates in your selected dimension.
- **Respawn Handling:** Players respawn at configured coordinates, maintaining immersive gameplay and unique spawning mechanics.
- **Multi-Dimension Support:** Fully compatible with overworld, nether, end, and custom modded dimensions.
- **Easy Configuration:** Modify spawn coordinates and dimension easily through a simple configuration file.

## Installation
1. Download the latest version of DimensionSpawn.
2. Place the `.jar` file in your `mods` folder.
3. Start your Minecraft server/client.
4. Configure your spawn coordinates in the generated `dimensionspawn.toml` file.

## Configuration Example
```toml
# Welcome to the DimensionSpawn config.
# Here you can set the dimension and the coordinates for player spawning and respawning.
[General]
    #Here you can enable and disable the usage of a safe spawnpoint.
    safeSpawn = true
    #Here you set the range of the safe spawn search.
    # Default: 100
    # Range: 0 ~ 10000
    safeSpawnRange = 250

[Dimension]
    # Here you can enable and disable the usage of a dimension.
    useDimension = true
    # There you can define a default dimension to spawn. Syntax: modid:world_id. You can use '/execute in' tab-completion to find a list of available dimensions.
    dimension = "minecraft:the_nether"

[Coordinates]
    # Here you can enable and disable the usage of coordinates.
    useCoordinates = true
    # Range: -1.7976931348623157E308 ~ 1.7976931348623157E308
    x = 3427.0
    # Range: -1.7976931348623157E308 ~ 1.7976931348623157E308
    y = 60.0
    # Range: -1.7976931348623157E308 ~ 1.7976931348623157E308
    z = -235.0
    #Range: -180.0 ~ 180.0
    yaw = 35.0
    #Range: -90.0 ~ 90.0
    pitch = 23.0
```

## Compatibility
- Compatible with Forge 1.20.1, NeoForge 1.21.1, NeoForge 1.21.3 & NeoForge 1.21.4.
- NeoForge 1.21.5 are work in progress.
- Fabric will added later.
- Plays nicely with most dimension and teleportation mods.

## Support
For issues, feature requests, or assistance, please visit our GitHub repository or leave a comment below.

Happy teleporting!

