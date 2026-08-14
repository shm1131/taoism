package io.github.shm1131.taoism.player.attachment.api;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public interface ITaoismData {

    int MAX_JING_LI_EAT = 10;
    int MAX_JING_LI_SLEEP = 10;
    int TOTAL_JING_LI = MAX_JING_LI_EAT + MAX_JING_LI_SLEEP;

  StreamCodec<FriendlyByteBuf, ITaoismData> STREAM_CODEC = new StreamCodec<>() {
    @Override
    public ITaoismData decode(FriendlyByteBuf buf) {
      return new TaoismData(
          buf.readInt(), buf.readBoolean(),       // chengFu
          buf.readInt(), buf.readInt(),  // qi
          buf.readInt(),        // shouMing
          buf.readBoolean(),
          buf.readInt(), buf.readInt()
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
      buf.writeInt(data.getJingLiEat());
      buf.writeInt(data.getJingLiSleep());
    }
  };
  MapCodec<ITaoismData> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
      Codec.INT.fieldOf("cheng_fu").forGetter(ITaoismData::getChengFu),
      Codec.BOOL.fieldOf("is_init_cheng_fu").forGetter(ITaoismData::isChengFuInit),
      Codec.INT.fieldOf("xian_tian_qi").forGetter(ITaoismData::getXianTianQi),
      Codec.INT.fieldOf("hou_tian_qi").forGetter(ITaoismData::getHouTianQi),
      Codec.INT.fieldOf("shou_ming").forGetter(ITaoismData::getShouMing),
      Codec.BOOL.fieldOf("is_in_realm").forGetter(ITaoismData::isInRealm),
      Codec.INT.fieldOf("jing_li_eat").forGetter(ITaoismData::getJingLiEat),
      Codec.INT.fieldOf("jing_li_sleep").forGetter(ITaoismData::getJingLiSleep)
  ).apply(instance, TaoismData::new));
  ITaoismData EMPTY = new TaoismData(
      0, false,
      0, 0,
      0, false,
      20,0
  );

  int getChengFu();

  boolean isChengFuInit();

  int getXianTianQi();

  int getHouTianQi();

  int getShouMing();

  boolean isInRealm();

  int getJingLiEat();

  int getJingLiSleep();

  record TaoismData(
      int chengFu, boolean chengFuInit,
      int xianTianQi, int houTianQi,
      int shouMing,
      boolean isInRealm,
      int JingLiEat, int jingLiSleep
  ) implements ITaoismData {
    @Override
    public int getChengFu() {
      return chengFu;
    }

    @Override
    public boolean isChengFuInit() {
      return chengFuInit;
    }

    @Override
    public int getXianTianQi() {
      return xianTianQi;
    }

    @Override
    public int getHouTianQi() {
      return houTianQi;
    }

    @Override
    public int getShouMing() {
      return shouMing;
    }

    @Override
    public boolean isInRealm() {
      return isInRealm;
    }

    @Override
    public int getJingLiEat() {return JingLiEat;}
    @Override
    public int getJingLiSleep() {return jingLiSleep;}
  }
}
