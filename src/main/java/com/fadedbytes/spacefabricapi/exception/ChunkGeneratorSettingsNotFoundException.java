package com.fadedbytes.spacefabricapi.exception;

public class ChunkGeneratorSettingsNotFoundException extends EntryNotFoundException {
    public ChunkGeneratorSettingsNotFoundException(String entryName) {
        super("Chunk generator settings", entryName);
    }
}
