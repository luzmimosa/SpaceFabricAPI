package com.fadedbytes.spacefabricapi.duck;

import net.minecraft.registry.RegistryKey;
import net.minecraft.server.WorldGenerationProgressListener;
import net.minecraft.world.dimension.DimensionOptions;

public interface MinecraftServerDuck {

    void spaceFabricAPI$addAndLoadWorld(RegistryKey<DimensionOptions> registryKey, DimensionOptions dimensionOptions, long seed, WorldGenerationProgressListener generationProgressListener);

}
