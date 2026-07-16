package io.github.kanybd1.Taoism.player.data.attachment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public interface ITaoismData {
    // === 承负 ===
    int getChengFu();
    boolean isChengFuInit();

    // === 气 ===
    int getXianTianQi();
    int getHouTianQi();

    // === 寿命 ===
    int getShouMing();

    boolean isInRealm();

    // === 编解码器 ===
    StreamCodec<FriendlyByteBuf, ITaoismData> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public ITaoismData decode(FriendlyByteBuf buf) {
            return new TaoismData(
                    buf.readInt(), buf.readBoolean(),       // chengFu
                    buf.readInt(), buf.readInt(),  // qi
                    buf.readInt(),        // shouMing
                    buf.readBoolean()
            );
        }

        @Override
        public void encode(FriendlyByteBuf buf, ITaoismData data) {
            buf.writeInt(data.getChengFu());
            buf.writeBoolean(data.isChengFuInit());
            buf.writeInt(data.getXianTianQi());
            buf.writeInt(data.getHouTianQi());
            buf.writeInt(data.getShouMing());
            buf.writeBoolean(data.isInRealm());
        }
    };

    MapCodec<ITaoismData> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.INT.fieldOf("cheng_fu").forGetter(ITaoismData::getChengFu),
            Codec.BOOL.fieldOf("is_init_cheng_fu").forGetter(ITaoismData::isChengFuInit),
            Codec.INT.fieldOf("xian_tian_qi").forGetter(ITaoismData::getXianTianQi),
            Codec.INT.fieldOf("hou_tian_qi").forGetter(ITaoismData::getHouTianQi),
            Codec.INT.fieldOf("shou_ming").forGetter(ITaoismData::getShouMing),
            Codec.BOOL.fieldOf("is_in_realm").forGetter(ITaoismData::isInRealm)
    ).apply(instance, TaoismData::new));

    record TaoismData(
            int chengFu, boolean chengFuInit,
            int xianTianQi, int houTianQi,
            int shouMing,
            boolean isInRealm
    ) implements ITaoismData {
        @Override public int getChengFu()      { return chengFu; }
        @Override public boolean isChengFuInit(){ return chengFuInit; }
        @Override public int getXianTianQi()   { return xianTianQi; }
        @Override public int getHouTianQi()    { return houTianQi; }
        @Override public int getShouMing()     { return shouMing; }
        @Override public boolean isInRealm()    { return isInRealm; }
    }

    ITaoismData EMPTY = new TaoismData(
            0, false,
            0, 0,
            0 ,false
    );
}