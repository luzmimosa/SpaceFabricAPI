package com.fadedbytes.spacefabricapi.client;

import com.fadedbytes.spacefabricapi.SpaceFabricAPI;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.DimensionEffects;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

@Environment(EnvType.CLIENT)
public class CustomDimensionEffects {

    private static final HashMap<Identifier, DimensionEffects> EFFECTS = new HashMap<>();

    public static @Nullable DimensionEffects byIdentifier(Identifier id) {
        return EFFECTS.get(id);
    }

    public static void registerEffects(Identifier id, DimensionEffects effects) {
        SpaceFabricAPI.LOGGER.info("Registered dimension effect: " + id.toString());
        EFFECTS.put(id, effects);
    }

}
