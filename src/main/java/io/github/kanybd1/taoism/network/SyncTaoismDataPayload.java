package io.github.kanybd1.taoism.network;

import io.github.kanybd1.taoism.TaoismMain;
import io.github.kanybd1.taoism.data.attachment.ITaoismData;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

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
}
