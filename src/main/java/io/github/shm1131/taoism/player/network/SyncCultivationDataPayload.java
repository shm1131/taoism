package io.github.shm1131.taoism.player.network;

import io.github.shm1131.taoism.TaoismMain;
import io.github.shm1131.taoism.player.client.ClientCultivationCache;
import io.github.shm1131.taoism.player.data.cultivation.ICultivationData;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SyncCultivationDataPayload(ICultivationData data) implements CustomPacketPayload {

    public static final Type<SyncCultivationDataPayload> TYPE =
            new Type<>(Identifier.fromNamespaceAndPath(TaoismMain.MODID, "sync_cultivation_data"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncCultivationDataPayload> STREAM_CODEC =
            StreamCodec.composite(
                    ICultivationData.STREAM_CODEC,
                    SyncCultivationDataPayload::data,
                    SyncCultivationDataPayload::new
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(SyncCultivationDataPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            ClientCultivationCache.update(payload.data());
            });
    }
}