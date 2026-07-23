package io.github.shm1131.taoism.network;

import io.github.shm1131.taoism.TaoismMain;
import io.github.shm1131.taoism.player.attachment.cultivation.ICultivationData;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

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
}
