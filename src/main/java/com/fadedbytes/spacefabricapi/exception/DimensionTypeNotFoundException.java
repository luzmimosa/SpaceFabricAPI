package com.fadedbytes.spacefabricapi.exception;

import net.minecraft.server.MinecraftServer;

public class DimensionTypeNotFoundException extends EntryNotFoundException {

    public DimensionTypeNotFoundException(String dimension) {
        super("Dimension type", dimension);
    }
}
