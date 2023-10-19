package com.fadedbytes.spacefabricapi.exception;

public class BiomeKeyNotFoundException extends EntryNotFoundException {
    public BiomeKeyNotFoundException(String entryName) {
        super("Biome key", entryName);
    }
}
