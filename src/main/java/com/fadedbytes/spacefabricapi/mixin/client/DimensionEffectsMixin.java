package com.fadedbytes.spacefabricapi.mixin.client;

import com.fadedbytes.spacefabricapi.SpaceFabricAPI;
import com.fadedbytes.spacefabricapi.client.CustomDimensionEffects;
import net.minecraft.client.render.DimensionEffects;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DimensionEffects.class)
public class DimensionEffectsMixin {

    @Inject(
            at = @At("HEAD"),
            method = "byDimensionType",
            cancellable = true
    )
    private static void byCustomDimensionType(DimensionType dimensionType, CallbackInfoReturnable<DimensionEffects> cir) {
        SpaceFabricAPI.LOGGER.info("Asked for dimension effect: " + dimensionType.effects().toString());
        DimensionEffects customEffects = CustomDimensionEffects.byIdentifier(dimensionType.effects());
        SpaceFabricAPI.LOGGER.info("Found: " + customEffects);

        if (customEffects != null) {
            cir.setReturnValue(customEffects);
        }
    }
}
