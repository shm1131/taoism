package io.github.shm1131.taoism.player.attachment;

import io.github.shm1131.taoism.init.TaoismAttachments;
import io.github.shm1131.taoism.network.SyncTaoismDataPayload;
import io.github.shm1131.taoism.player.attachment.api.ITaoismData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;

public class TaoismHelper {

  private static ITaoismData getData(Player player) {
    return player.getData(TaoismAttachments.TAOISM_DATA);
  }

  private static void setData(Player player, ITaoismData data) {
    player.setData(TaoismAttachments.TAOISM_DATA, data);
    if (!player.level().isClientSide() && player instanceof ServerPlayer sp) {
      PacketDistributor.sendToPlayer(sp, new SyncTaoismDataPayload(data));
    }
  }


  public static int getChengFu(Player player) {
    return getData(player).getChengFu();
  }

  public static void addChengFu(Player player, int amount) {
    ITaoismData d = getData(player);
    setData(player, new ITaoismData.TaoismData(
        d.getChengFu() + amount, d.isChengFuInit(),
        d.getXianTianQi(), d.getHouTianQi(),
        d.getShouMing(), d.isInRealm()
    ));
  }


  public static int getXianTianQi(Player player) {
    return getData(player).getXianTianQi();
  }

  public static int getHouTianQi(Player player) {
    return getData(player).getHouTianQi();
  }

  public static void addXianTianQi(Player player, int amount) {
    ITaoismData d = getData(player);
    setData(player, new ITaoismData.TaoismData(
        d.getChengFu(), d.isChengFuInit(),
        d.getXianTianQi() + amount, d.getHouTianQi(),
        d.getShouMing(), d.isInRealm()
    ));
  }

  public static void addHouTianQi(Player player, int amount) {
    ITaoismData d = getData(player);
    setData(player, new ITaoismData.TaoismData(
        d.getChengFu(), d.isChengFuInit(),
        d.getXianTianQi(), d.getHouTianQi() + amount,
        d.getShouMing(), d.isInRealm()
    ));
  }


  public static int getShouMing(Player player) {
    return getData(player).getShouMing();
  }

  public static void addShouMing(Player player, int amount) {
    ITaoismData d = getData(player);
    setData(player, new ITaoismData.TaoismData(
        d.getChengFu(), d.isChengFuInit(),
        d.getXianTianQi(), d.getHouTianQi(),
        d.getShouMing() + amount, d.isInRealm()
    ));
  }


  public static void setInRealm(Player player, boolean inRealm) {
    ITaoismData d = getData(player);
    setData(player, new ITaoismData.TaoismData(
        d.getChengFu(), d.isChengFuInit(),
        d.getXianTianQi(), d.getHouTianQi(),
        d.getShouMing(), inRealm
    ));
  }


  public static void initYangWorld(Player player, int chengFu) {
    setData(player, new ITaoismData.TaoismData(
        chengFu, true,
        50, 0,
        (50 + chengFu) * 20,
        false
    ));
  }

  public static void resetForYangWorld(Player player) {
    ITaoismData d = getData(player);
    setData(player, new ITaoismData.TaoismData(
        d.getChengFu(), d.isChengFuInit(),
        50, 0,
        (50 + d.getChengFu()) * 20,
        false
    ));
  }

  public static void resetForYinWorld(Player player, int chengFu) {
    ITaoismData d = getData(player);
    setData(player, new ITaoismData.TaoismData(
        chengFu, d.isChengFuInit(),
        0, 0,
        chengFu * 20,
        true
    ));
  }
}