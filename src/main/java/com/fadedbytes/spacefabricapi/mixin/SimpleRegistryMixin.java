package com.fadedbytes.spacefabricapi.mixin;

import com.fadedbytes.spacefabricapi.SpaceFabricAPI;
import com.fadedbytes.spacefabricapi.duck.UnfreezableDuck;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SimpleRegistry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(SimpleRegistry.class)
public class SimpleRegistryMixin<T> implements UnfreezableDuck {

    @Override
    public void spaceFabricAPI$unfreeze() {
        this.frozen = false;
        SpaceFabricAPI.LOGGER.info("Unfreezing registry: " + this.key.toString());
    }

    @Override
    public boolean spaceFabricAPI$isFrozen() {
        return this.frozen;
    }

    @Shadow
    private boolean frozen;

    @Final
    @Shadow
    RegistryKey<? extends Registry<T>> key;

}
