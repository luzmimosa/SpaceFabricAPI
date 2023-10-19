package com.fadedbytes.spacefabricapi.api.bakers;

import com.fadedbytes.spacefabricapi.api.model.BakedDimension;
import com.fadedbytes.spacefabricapi.exception.BiomeKeyNotFoundException;
import com.fadedbytes.spacefabricapi.exception.ChunkGeneratorSettingsNotFoundException;
import com.fadedbytes.spacefabricapi.exception.DimensionTypeNotFoundException;
import com.fadedbytes.spacefabricapi.exception.EntryNotFoundException;
import com.mojang.serialization.Lifecycle;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.biome.BuiltinBiomes;
import net.minecraft.world.biome.OverworldBiomeCreator;
import net.minecraft.world.biome.source.*;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.WorldPreset;
import net.minecraft.world.gen.WorldPresets;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import net.minecraft.world.gen.chunk.NoiseChunkGenerator;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class DimensionBaker {

    protected long seed;
    private Lifecycle dimensionLifecycle;
    private RegistryKey<DimensionOptions> dimensionKey;
    private RegistryEntry<DimensionType> dimensionType;
    private ChunkGenerator chunkGenerator;

    public DimensionBaker() {
        this.seed = Math.round(Math.random() * Long.MAX_VALUE);
        this.dimensionLifecycle = Lifecycle.stable();
        this.dimensionKey = null;
    }

    public DimensionBaker seed(long seed) {
        this.seed = seed;
        return this;
    }

    public DimensionBaker lifecycle(@NotNull Lifecycle dimensionLifecycle) {
        this.dimensionLifecycle = dimensionLifecycle;
        return this;
    }

    public DimensionBaker dimensionKey(@NotNull RegistryKey<DimensionOptions> dimensionKey) {
        this.dimensionKey = dimensionKey;
        return this;
    }

    public DimensionBaker dimensionKey(@NotNull String namespace, @NotNull String path) {
        this.dimensionKey = RegistryKey.of(
                RegistryKeys.DIMENSION,
                new Identifier(namespace, path)
        );
        return this;
    }

    public DimensionBaker dimensionType(@NotNull RegistryEntry<DimensionType> dimensionType) {
        this.dimensionType = dimensionType;
        return this;
    }

    public DimensionBaker chunkGenerator(@NotNull ChunkGenerator chunkGenerator) {
        this.chunkGenerator = chunkGenerator;
        return this;
    }

    public long getSeed() {
        return this.seed;
    }

    public Lifecycle getDimensionLifecycle() {
        return this.dimensionLifecycle;
    }

    public RegistryKey<DimensionOptions> getDimensionKey() {
        return this.dimensionKey;
    }

    public RegistryEntry<DimensionType> getDimensionType() {
        return this.dimensionType;
    }

    public ChunkGenerator getChunkGenerator() {
        return this.chunkGenerator;
    }

    public BakedDimension bake() {

        assert this.dimensionKey != null;
        assert this.dimensionType != null;
        assert this.chunkGenerator != null;
        assert this.dimensionLifecycle != null;

        return new BakedDimension(
                new DimensionOptions(
                        this.dimensionType,
                        this.chunkGenerator
                ),
                this.dimensionKey,
                this.dimensionLifecycle,
                this.seed
        );
    }

    public static class Ingredients {
        public static class DimensionTypes {
            public static RegistryEntry<DimensionType> Overworld(MinecraftServer server) {
                try {
                    return fromKey(net.minecraft.world.dimension.DimensionTypes.OVERWORLD, server);
                } catch (DimensionTypeNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }

            public static RegistryEntry<DimensionType> End(MinecraftServer server) {
                try {
                    return fromKey(net.minecraft.world.dimension.DimensionTypes.THE_END, server);
                } catch (DimensionTypeNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }

            public static RegistryEntry<DimensionType> Nether(MinecraftServer server) {
                try {
                    return fromKey(net.minecraft.world.dimension.DimensionTypes.THE_NETHER, server);
                } catch (DimensionTypeNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }

            public static RegistryEntry<DimensionType> Caves(MinecraftServer server) {
                try {
                    return fromKey(net.minecraft.world.dimension.DimensionTypes.OVERWORLD_CAVES, server);
                } catch (DimensionTypeNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }

            public static RegistryEntry<DimensionType> fromKey(RegistryKey<DimensionType> key, MinecraftServer server) throws DimensionTypeNotFoundException {
                AtomicReference<RegistryEntry<DimensionType>> dimensionTypeEntry = new AtomicReference<>();
                server.getCombinedDynamicRegistries()
                        .getCombinedRegistryManager()
                        .get(RegistryKeys.DIMENSION_TYPE)
                        .getEntry(key)
                        .ifPresent(dimensionTypeEntry::set);

                RegistryEntry<DimensionType> dimensionType = dimensionTypeEntry.get();

                if (dimensionType == null) {
                    throw new DimensionTypeNotFoundException(key.toString());
                }

                return dimensionType;
            }
        }
        public static class ChunkGenerators {
            public static ChunkGenerator Overworld(BiomeSource biomes, MinecraftServer server) {
                try {
                    return new NoiseChunkGenerator(
                            biomes,
                            settingsOf(ChunkGeneratorSettings.OVERWORLD, server)
                    );
                } catch (ChunkGeneratorSettingsNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }

            public static ChunkGenerator End(BiomeSource biomes, MinecraftServer server) {
                try {
                    return new NoiseChunkGenerator(
                            biomes,
                            settingsOf(ChunkGeneratorSettings.END, server)
                    );
                } catch (ChunkGeneratorSettingsNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }

            public static ChunkGenerator Amplified(BiomeSource biomes, MinecraftServer server) {
                try {
                    return new NoiseChunkGenerator(
                            biomes,
                            settingsOf(ChunkGeneratorSettings.AMPLIFIED, server)
                    );
                } catch (ChunkGeneratorSettingsNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }

            public static ChunkGenerator Caves(BiomeSource biomes, MinecraftServer server) {
                try {
                    return new NoiseChunkGenerator(
                            biomes,
                            settingsOf(ChunkGeneratorSettings.CAVES, server)
                    );
                } catch (ChunkGeneratorSettingsNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }

            public static ChunkGenerator Nether(BiomeSource biomes, MinecraftServer server) {
                try {
                    return new NoiseChunkGenerator(
                            biomes,
                            settingsOf(ChunkGeneratorSettings.NETHER, server)
                    );
                } catch (ChunkGeneratorSettingsNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }

            public static ChunkGenerator FloatingIslands(BiomeSource biomes, MinecraftServer server) {
                try {
                    return new NoiseChunkGenerator(
                            biomes,
                            settingsOf(ChunkGeneratorSettings.FLOATING_ISLANDS, server)
                    );
                } catch (ChunkGeneratorSettingsNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }

            public static ChunkGenerator LargeBiomes(BiomeSource biomes, MinecraftServer server) {
                try {
                    return new NoiseChunkGenerator(
                            biomes,
                            settingsOf(ChunkGeneratorSettings.LARGE_BIOMES, server)
                    );
                } catch (ChunkGeneratorSettingsNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }

            public static RegistryEntry<ChunkGeneratorSettings> settingsOf(RegistryKey<ChunkGeneratorSettings> settingsKey, MinecraftServer server) throws ChunkGeneratorSettingsNotFoundException {
                AtomicReference<RegistryEntry<ChunkGeneratorSettings>> settingsEntry = new AtomicReference<>();
                server.getCombinedDynamicRegistries()
                        .getCombinedRegistryManager()
                        .get(RegistryKeys.CHUNK_GENERATOR_SETTINGS)
                        .getEntry(settingsKey)
                        .ifPresent(settingsEntry::set);

                RegistryEntry<ChunkGeneratorSettings> dimensionType = settingsEntry.get();

                if (dimensionType == null) {
                    throw new ChunkGeneratorSettingsNotFoundException(settingsKey.toString());
                }

                return dimensionType;
            }
        }
        public static class BiomeSources {
            public static FixedBiomeSource FixedSource(RegistryKey<Biome> biomeKey, MinecraftServer server) throws BiomeKeyNotFoundException {
                AtomicReference<RegistryEntry<Biome>> biome = new AtomicReference<>();
                server.getCombinedDynamicRegistries()
                        .getCombinedRegistryManager()
                        .get(RegistryKeys.BIOME)
                        .getEntry(biomeKey)
                        .ifPresent(biome::set);

                if (biome.get() == null) {
                    throw new BiomeKeyNotFoundException(biomeKey.toString());
                }

                return new FixedBiomeSource(biome.get());
            }
            public static FixedBiomeSource FixedSource(RegistryEntry<Biome> biomeEntry) {
                return new FixedBiomeSource(biomeEntry);
            }
            public static MultiNoiseBiomeSource Overworld(MinecraftServer server) {
                AtomicReference<RegistryEntry<MultiNoiseBiomeSourceParameterList>> parameters = new AtomicReference<>();
                server.getCombinedDynamicRegistries()
                        .getCombinedRegistryManager()
                        .get(RegistryKeys.MULTI_NOISE_BIOME_SOURCE_PARAMETER_LIST)
                        .getEntry(MultiNoiseBiomeSourceParameterLists.OVERWORLD)
                        .ifPresent(parameters::set);

                if (parameters.get() == null) {
                    throw new RuntimeException("Multinoise biome source parameters for Overworld not found at the server registry");
                }

                return MultiNoiseBiomeSource.create(parameters.get());
            }
            public static MultiNoiseBiomeSource Nether(MinecraftServer server) {
                AtomicReference<RegistryEntry<MultiNoiseBiomeSourceParameterList>> parameters = new AtomicReference<>();
                server.getCombinedDynamicRegistries()
                        .getCombinedRegistryManager()
                        .get(RegistryKeys.MULTI_NOISE_BIOME_SOURCE_PARAMETER_LIST)
                        .getEntry(MultiNoiseBiomeSourceParameterLists.NETHER)
                        .ifPresent(parameters::set);

                if (parameters.get() == null) {
                    throw new RuntimeException("Multinoise biome source parameters for Nether not found at the server registry");
                }

                return MultiNoiseBiomeSource.create(parameters.get());
            }
        }
    }

}
