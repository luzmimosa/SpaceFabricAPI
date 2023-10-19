package com.fadedbytes.spacefabricapi;

import com.fadedbytes.spacefabricapi.api.WorldManagerAPI;
import com.fadedbytes.spacefabricapi.api.bakers.DimensionBaker;
import com.fadedbytes.spacefabricapi.client.CustomDimensionEffects;
import com.fadedbytes.spacefabricapi.duck.UnfreezableDuck;
import com.fadedbytes.spacefabricapi.exception.BiomeKeyNotFoundException;
import com.fadedbytes.spacefabricapi.internal.WorldManager;
import com.mojang.serialization.Lifecycle;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.server.ServerStartCallback;
import net.minecraft.client.render.DimensionEffects;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.biome.source.FixedBiomeSource;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.OptionalLong;
import java.util.concurrent.atomic.AtomicReference;

public class SpaceFabricAPI implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("spacefabricapi");

	@Override
	public void onInitialize() {
		LOGGER.info("SpaceFabricAPI initialized!");
	}

	private void testMod(MinecraftServer server) {
		WorldManagerAPI api = WorldManagerAPI.forServer(server);

		createWorld(server, api);
	}

	private void createWorld(MinecraftServer server, WorldManagerAPI api) {
		DimensionBaker baker = new DimensionBaker();

		FixedBiomeSource source;

		try {
			source = DimensionBaker.Ingredients.BiomeSources.FixedSource(BiomeKeys.CHERRY_GROVE, server);
		} catch (BiomeKeyNotFoundException e) {
			throw new RuntimeException(e);
		}

		baker.dimensionKey(RegistryKey.of(RegistryKeys.DIMENSION, new Identifier("uwu", "owo")));
		baker.seed(1234);
		baker.lifecycle(Lifecycle.stable());
		baker.dimensionType(dimensionType(server));
		baker.chunkGenerator(DimensionBaker.Ingredients.ChunkGenerators.FloatingIslands(source, server));

		api.createDimension(baker.bake());
	}

	private RegistryEntry<DimensionType> dimensionType(MinecraftServer server) {
		SimpleRegistry<DimensionType> dimensionRegistry = ((SimpleRegistry<DimensionType>) (server.getCombinedDynamicRegistries().getCombinedRegistryManager().get(RegistryKeys.DIMENSION_TYPE)));

		Identifier dimensionId = new Identifier("test", "miau");
		Identifier dimensionEffectsId = new Identifier("test", "test_effects");

		registerCustomDimensionEffects(dimensionEffectsId);

		((UnfreezableDuck)(dimensionRegistry)).spaceFabricAPI$unfreeze();
		RegistryEntry.Reference<DimensionType> dimensionTypeReference = dimensionRegistry.add(
				RegistryKey.of(RegistryKeys.DIMENSION_TYPE, dimensionId),
				new DimensionType(
						OptionalLong.of(12500),
						true,
						false,
						false,
						true,
						100,
						false,
						false,
						-160,
						320,
						320,
						BlockTags.INFINIBURN_END,
						dimensionEffectsId,
						1.0f,
						new DimensionType.MonsterSettings(false, false, UniformIntProvider.create(0, 7), 0)
				),
				Lifecycle.stable()
		);
		dimensionRegistry.freeze();

		return dimensionTypeReference;
	}

	private void registerCustomDimensionEffects(Identifier id) {

		CustomDimensionEffects.registerEffects(
				id,
				new DimensionEffects(
						10,
						true,
						DimensionEffects.SkyType.END,
						true,
						true
				) {
					@Override
					public Vec3d adjustFogColor(Vec3d color, float sunHeight) {
						return color.multiply(
								sunHeight * 0.94F + 0.06F,
								sunHeight * 0.94F + 0.06F,
								sunHeight * 0.91F + 0.09F
						); // From overworld
					}

					@Override
					public boolean useThickFog(int camX, int camY) {
						return true;
					}
				}
		);
	}
}