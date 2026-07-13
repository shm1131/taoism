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
                d.getShouMing()
        ));
    }

    // ==================== 气 ====================
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
                d.getShouMing()
        ));
    }

    public static void addHouTianQi(Player player, int amount) {
        ITaoismData d = getData(player);
        setData(player, new ITaoismData.TaoismData(
                d.getChengFu(), d.isChengFuInit(),
                d.getXianTianQi(), d.getHouTianQi() + amount,
                d.getShouMing()
        ));
    }

    // ==================== 寿命 ====================
    public static int getShouMing(Player player) {
        return getData(player).getShouMing();
    }

    public static void addShouMing(Player player, int amount) {
        ITaoismData d = getData(player);
        setData(player, new ITaoismData.TaoismData(
                d.getChengFu(), d.isChengFuInit(),
                d.getXianTianQi(), d.getHouTianQi(),
                d.getShouMing() + amount
        ));
    }

    // ==================== 初始化 / 重置 ====================
    public static void initialize(Player player, int chengFu, int xianTianQi, int shouMing) {
        setData(player, new ITaoismData.TaoismData(
                chengFu, true,
                xianTianQi, 0,
                shouMing
        ));
    }

    public static void resetForReincarnation(Player player, int baseQi, int baseShouMing) {
        ITaoismData d = getData(player);
        setData(player, new ITaoismData.TaoismData(
                d.getChengFu(), d.isChengFuInit(),
                baseQi, 0,
                baseShouMing
        ));
    }
}