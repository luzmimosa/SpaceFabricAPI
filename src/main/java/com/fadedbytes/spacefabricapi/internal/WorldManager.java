package com.fadedbytes.spacefabricapi.internal;

import com.fadedbytes.spacefabricapi.api.model.BakedDimension;
import com.fadedbytes.spacefabricapi.duck.MinecraftServerDuck;
import com.fadedbytes.spacefabricapi.duck.UnfreezableDuck;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldGenerationProgressLogger;
import net.minecraft.util.Identifier;
import net.minecraft.world.dimension.DimensionOptions;

import static com.fadedbytes.spacefabricapi.SpaceFabricAPI.LOGGER;

public class WorldManager {

    private final MinecraftServer server;

    public WorldManager(MinecraftServer server) {
        this.server = server;
    }

    public void createDimension(BakedDimension dimension) {
        Registry<DimensionOptions> dimensionRegistry = registryFromKey(RegistryKeys.DIMENSION);

        // Unfreeze the registry so we can modify it.
        unfreezeRegistry(dimensionRegistry);

        // Register the dimension in the registry.
        RegistryEntry.Reference<DimensionOptions> dimensionRegistryReference = ((SimpleRegistry<DimensionOptions>) dimensionRegistry).add(
                dimension.dimensionKey(),
                dimension.dimensionOptions(),
                dimension.dimensionLifecycle()
        );

        // Refreeze the registry so the changes are applied.
        dimensionRegistry.freeze();

        // Register the dimension in the server.
        ((MinecraftServerDuck) this.server).spaceFabricAPI$addAndLoadWorld(
                dimensionRegistryReference.registryKey(),
                dimensionRegistryReference.value(),
                dimension.seed(),
                new WorldGenerationProgressLogger(5)
        );
    }

    private <T> void unfreezeRegistry(Registry<T> registry) {
        LOGGER.debug("Unfreezing registry: " + registry.getKey().toString());

        ((UnfreezableDuck) registry).spaceFabricAPI$unfreeze();
    }

    private <T> void refreezeRegistry(Registry<T> registry) {
        LOGGER.debug("Freezing registry: " + registry.getKey().toString());
        registry.freeze();
    }

    private <T> Registry<T> registryFromKey(RegistryKey<Registry<T>> registryKey) {
        return server.getCombinedDynamicRegistries().getCombinedRegistryManager().get(registryKey);
    }

    public void deleteDimension(Identifier dimensionId) {

    }

}
