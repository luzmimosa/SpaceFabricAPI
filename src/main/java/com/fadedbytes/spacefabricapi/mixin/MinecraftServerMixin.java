package com.fadedbytes.spacefabricapi.mixin;

import com.fadedbytes.spacefabricapi.duck.MinecraftServerDuck;
import com.google.common.collect.ImmutableList;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldGenerationProgressListener;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.SaveProperties;
import net.minecraft.world.World;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.border.WorldBorderListener;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.level.UnmodifiableLevelProperties;
import net.minecraft.world.level.storage.LevelStorage;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;
import java.util.concurrent.Executor;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin implements MinecraftServerDuck {

    @Override
    public void spaceFabricAPI$addAndLoadWorld(RegistryKey<DimensionOptions> registryKey, DimensionOptions dimensionOptions, long seed, WorldGenerationProgressListener generationProgressListener) {
        RegistryKey<World> worldRegistryKey = RegistryKey.of(RegistryKeys.WORLD, registryKey.getValue());
        ServerWorld overworld = this.worlds.get(World.OVERWORLD);

        UnmodifiableLevelProperties unmodifiableLevelProperties = new UnmodifiableLevelProperties(this.saveProperties, this.saveProperties.getMainWorldProperties());
        ServerWorld serverWorld = new ServerWorld(
                ((MinecraftServer) ((Object)this)),
                this.workerExecutor,
                this.session,
                unmodifiableLevelProperties,
                worldRegistryKey,
                dimensionOptions,
                generationProgressListener,
                false, // isDebugWorld()
                BiomeAccess.hashSeed(seed),
                ImmutableList.of(),
                false,
                overworld.getRandomSequences()
        );

        overworld.getWorldBorder().addListener(new WorldBorderListener.WorldBorderSyncer(serverWorld.getWorldBorder()));
        this.worlds.put(worldRegistryKey, serverWorld);
    }

    @Shadow
    @Final
    protected SaveProperties saveProperties;

    @Shadow
    @Final
    private Executor workerExecutor;

    @Shadow
    @Final
    protected LevelStorage.Session session;

    @Shadow
    @Final
    private Map<RegistryKey<World>, ServerWorld> worlds;
}
