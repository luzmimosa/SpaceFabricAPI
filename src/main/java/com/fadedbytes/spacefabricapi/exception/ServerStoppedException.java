package com.fadedbytes.spacefabricapi.exception;

public class ServerStoppedException extends Exception {
    
    public ServerStoppedException() {
        super("Server has been stopped");
    }
    
}
