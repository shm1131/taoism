package io.github.shm1131.taoism.network.handler;

import io.github.shm1131.taoism.network.SyncCultivationDataPayload;
import io.github.shm1131.taoism.network.SyncTaoismDataPayload;
import io.github.shm1131.taoism.player.data.ClientCultivationCache;
import io.github.shm1131.taoism.player.data.ClientTaoismCache;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class NetworkHandlerClient {
    public static void handle(SyncCultivationDataPayload payload, IPayloadContext context) {
        ClientCultivationCache.update(payload.data());
    }

    public static void handle(SyncTaoismDataPayload payload, IPayloadContext context) {
        ClientTaoismCache.update(payload.data());
    }
}
