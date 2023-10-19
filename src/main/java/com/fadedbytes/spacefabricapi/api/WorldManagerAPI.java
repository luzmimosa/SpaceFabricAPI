package com.fadedbytes.spacefabricapi.api;

import com.fadedbytes.spacefabricapi.api.model.BakedDimension;
import net.minecraft.server.MinecraftServer;

public interface WorldManagerAPI {

    void createDimension(BakedDimension bakedDimension);

    MinecraftServer getServer();

    static WorldManagerAPI forServer(MinecraftServer server) {
        return new BaseWorldManagerAPI(server);
    }

}
