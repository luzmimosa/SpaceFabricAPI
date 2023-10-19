package com.fadedbytes.spacefabricapi.api;

import com.fadedbytes.spacefabricapi.api.model.BakedDimension;
import com.fadedbytes.spacefabricapi.exception.ServerStoppedException;
import com.fadedbytes.spacefabricapi.internal.WorldManager;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;

public class BaseWorldManagerAPI implements WorldManagerAPI {

    private final MinecraftServer server;
    private final WorldManager worldManager;

    private boolean serverClosed;

    BaseWorldManagerAPI(MinecraftServer server) {
        this.server = server;
        this.worldManager = new WorldManager(server);

        this.serverClosed = false;

        ServerLifecycleEvents.SERVER_STOPPING.register(this::onServerStopping);
    }

    @Override
    public void createDimension(BakedDimension bakedDimension) {
        assertServerAvaiable();

        this.worldManager.createDimension(bakedDimension);
    }

    public void scheduleDimensionRemoving(Identifier id) {
        assertServerAvaiable();


    }

    @Override
    public MinecraftServer getServer() {
        assertServerAvaiable();

        return this.server;
    }

    private void assertServerAvaiable() {
        if (this.serverClosed) {
            throw new RuntimeException(new ServerStoppedException());
        }
    }

    private void onServerStopping(MinecraftServer server) {
        if (this.server.equals(server)) {
            this.serverClosed = true;
        }
    }
}
