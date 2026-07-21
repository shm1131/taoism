package io.github.kanybd1.taoism.network;

import io.github.kanybd1.taoism.client.ClientTaoismCache;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.handling.IPayloadContext;

@OnlyIn(Dist.CLIENT)
@SuppressWarnings("unused")
public class ClientPacketHandler {
  public static void handleSyncTaoismDataPayload(
      final SyncTaoismDataPayload payload,
      final IPayloadContext context
  ) {
    // NOTE: If we have a work needed running on Main-Thread, call enqueueWork.
      // Or let's do it on NetWork-Thread.
    ClientTaoismCache.update(payload.data());
  }
}
