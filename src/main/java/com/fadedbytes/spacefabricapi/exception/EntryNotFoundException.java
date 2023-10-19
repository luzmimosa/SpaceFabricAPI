package com.fadedbytes.spacefabricapi.exception;

public class EntryNotFoundException extends Exception {

    public EntryNotFoundException(String registryName, String entryName) {
        super(registryName + " [" + entryName + "] not found!");
    }

}
