package io.github.kanybd1.Taoism.player.data.attachment;

import io.github.kanybd1.Taoism.player.network.SyncTaoismDataPayload;
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

    // ==================== 承负 ====================
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

    // ==================== 气 ====================
    public static int getXianTianQi(Player player) { return getData(player).getXianTianQi(); }
    public static int getHouTianQi(Player player) { return getData(player).getHouTianQi(); }

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

    // ==================== 寿命 ====================
    public static int getShouMing(Player player) { return getData(player).getShouMing(); }

    public static void addShouMing(Player player, int amount) {
        ITaoismData d = getData(player);
        setData(player, new ITaoismData.TaoismData(
                d.getChengFu(), d.isChengFuInit(),
                d.getXianTianQi(), d.getHouTianQi(),
                d.getShouMing() + amount, d.isInRealm()
        ));
    }

    // ==================== 维度状态 ====================
    public static void setInRealm(Player player, boolean inRealm) {
        ITaoismData d = getData(player);
        setData(player, new ITaoismData.TaoismData(
                d.getChengFu(), d.isChengFuInit(),
                d.getXianTianQi(), d.getHouTianQi(),
                d.getShouMing(), inRealm
        ));
    }

    // ==================== 初始化 / 轮回重置 ====================

    /** 首次进入世界时的阳间初始化 */
    public static void initYangWorld(Player player, int chengFu) {
        setData(player, new ITaoismData.TaoismData(
                chengFu, true,
                50, 0,
                (50 + chengFu) * 20,  // 阳间初始寿命公式
                false                    // 阳间
        ));
    }

    /** 从阴间轮回回阳间：承负保留，寿命恢复阳间公式，isInRealm=false */
    public static void resetForYangWorld(Player player) {
        ITaoismData d = getData(player);
        setData(player, new ITaoismData.TaoismData(
                d.getChengFu(), d.isChengFuInit(),
                50, 0,                   // 先天之气重置，后天之气清零
                (50 + d.getChengFu()) * 20, // 阳间寿命公式
                false                    // ← 回到阳间
        ));
    }

    /** 从阳间轮回到阴间：承负保留，寿命=承负*600，isInRealm=true */
    public static void resetForYinWorld(Player player, int chengFu) {
        ITaoismData d = getData(player);
        setData(player, new ITaoismData.TaoismData(
                chengFu, d.isChengFuInit(),
                0, 0,                    // 阴间无气概念（按需调整）
                chengFu * 20,          // ✅ 阴间寿命 = 承负 × 600
                true                     // ← 进入阴间
        ));
    }
}