package io.github.shm1131.taoism.network;

import io.github.shm1131.taoism.TaoismMain;
import io.github.shm1131.taoism.player.attachment.api.IJingLiData;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record SyncJingLiDataPayload(IJingLiData data) implements CustomPacketPayload {

    public static final Type<SyncJingLiDataPayload> TYPE =
        new Type<>(Identifier.fromNamespaceAndPath(TaoismMain.MODID, "sync_jing_li_data"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncJingLiDataPayload> STREAM_CODEC =
        StreamCodec.composite(
            IJingLiData.STREAM_CODEC,
            SyncJingLiDataPayload::data,
            SyncJingLiDataPayload::new
        );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
