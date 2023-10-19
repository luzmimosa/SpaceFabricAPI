package com.fadedbytes.spacefabricapi.api.model;

import com.mojang.serialization.Lifecycle;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.dimension.DimensionOptions;
import org.jetbrains.annotations.NotNull;

public record BakedDimension(
        @NotNull DimensionOptions dimensionOptions,
        @NotNull RegistryKey<DimensionOptions> dimensionKey,
        @NotNull Lifecycle dimensionLifecycle,
        long seed
) {

}
