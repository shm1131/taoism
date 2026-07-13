package io.github.kanybd1.Taoism.player.network;

import io.github.kanybd1.Taoism.TaoismMain;
import io.github.kanybd1.Taoism.player.client.ClientTaoismCache;
import io.github.kanybd1.Taoism.player.data.attachment.ITaoismData;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SyncTaoismDataPayload(ITaoismData data) implements CustomPacketPayload {

    public static final Type<SyncTaoismDataPayload> TYPE =
            new Type<>(Identifier.fromNamespaceAndPath(TaoismMain.MODID, "sync_taoism_data"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncTaoismDataPayload> STREAM_CODEC =
            StreamCodec.composite(
                    ITaoismData.STREAM_CODEC,
                    SyncTaoismDataPayload::data,
                    SyncTaoismDataPayload::new
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(SyncTaoismDataPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            ClientTaoismCache.update(payload.data());
        });
    }
}