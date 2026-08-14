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

    private static ITaoismData copy(ITaoismData d,
                                    int chengFu, boolean chengFuInit,
                                    int xianTianQi, int houTianQi,
                                    int shouMing,
                                    boolean isInRealm,
                                    int JingLiEat, int jingLiSleep) {
        return new ITaoismData.TaoismData(
            chengFu, chengFuInit,
            xianTianQi, houTianQi,
            shouMing,
            isInRealm,
            JingLiEat, jingLiSleep
        );
    }

    private static ITaoismData withChengFu(ITaoismData d, int chengFu) {
        return copy(d, chengFu, d.isChengFuInit(), d.getXianTianQi(), d.getHouTianQi(),
            d.getShouMing(), d.isInRealm(), d.getJingLiEat(), d.getJingLiSleep());
    }

    private static ITaoismData withXianTianQi(ITaoismData d, int xianTianQi) {
        return copy(d, d.getChengFu(), d.isChengFuInit(), xianTianQi, d.getHouTianQi(),
            d.getShouMing(), d.isInRealm(), d.getJingLiEat(), d.getJingLiSleep());
    }

    private static ITaoismData withHouTianQi(ITaoismData d, int houTianQi) {
        return copy(d, d.getChengFu(), d.isChengFuInit(), d.getXianTianQi(), houTianQi,
            d.getShouMing(), d.isInRealm(), d.getJingLiEat(), d.getJingLiSleep());
    }

    private static ITaoismData withShouMing(ITaoismData d, int shouMing) {
        return copy(d, d.getChengFu(), d.isChengFuInit(), d.getXianTianQi(), d.getHouTianQi(),
            shouMing, d.isInRealm(), d.getJingLiEat(), d.getJingLiSleep());
    }

    private static ITaoismData withInRealm(ITaoismData d, boolean inRealm) {
        return copy(d, d.getChengFu(), d.isChengFuInit(), d.getXianTianQi(), d.getHouTianQi(),
            d.getShouMing(), inRealm, d.getJingLiEat(), d.getJingLiSleep());
    }

    private static ITaoismData withJingLiSleep(ITaoismData d, int jingLiSleep) {
        return copy(d, d.getChengFu(), d.isChengFuInit(), d.getXianTianQi(), d.getHouTianQi(),
            d.getShouMing(), d.isInRealm(), d.getJingLiEat(), jingLiSleep);
    }

    private static ITaoismData withJingLiEat(ITaoismData d, int JingLiEat) {
        return copy(d, d.getChengFu(), d.isChengFuInit(), d.getXianTianQi(), d.getHouTianQi(),
            d.getShouMing(), d.isInRealm(), JingLiEat, d.getJingLiSleep());
    }

    // ======================== ChengFu ========================

    public static int getChengFu(Player player) {
        return getData(player).getChengFu();
    }

    public static void addChengFu(Player player, int amount) {
        ITaoismData d = getData(player);
        setData(player, withChengFu(d, d.getChengFu() + amount));
    }

    // ======================== XianTianQi ========================

    public static int getXianTianQi(Player player) {
        return getData(player).getXianTianQi();
    }

    public static void addXianTianQi(Player player, int amount) {
        ITaoismData d = getData(player);
        setData(player, withXianTianQi(d, d.getXianTianQi() + amount));
    }

    // ======================== HouTianQi ========================

    public static int getHouTianQi(Player player) {
        return getData(player).getHouTianQi();
    }

    public static void addHouTianQi(Player player, int amount) {
        ITaoismData d = getData(player);
        setData(player, withHouTianQi(d, d.getHouTianQi() + amount));
    }

    // ======================== ShouMing ========================

    public static int getShouMing(Player player) {
        return getData(player).getShouMing();
    }

    public static void addShouMing(Player player, int amount) {
        ITaoismData d = getData(player);
        setData(player, withShouMing(d, d.getShouMing() + amount));
    }

    // ======================== InRealm ========================

    public static void setInRealm(Player player, boolean inRealm) {
        ITaoismData d = getData(player);
        setData(player, withInRealm(d, inRealm));
    }


    // ======================== JingLi ========================

    public static int getJingLiEat(Player p) { return getData(p).getJingLiEat(); }
    public static int getJingLiSleep(Player p) { return getData(p).getJingLiSleep(); }

    public static int getTotalJingLi(Player p) {
        ITaoismData d = getData(p);
        return d.getJingLiEat() + d.getJingLiSleep();
    }

    public static void addJingLiEat(Player p, int amount) {
        ITaoismData d = getData(p);
        int newVal = Math.clamp(d.getJingLiEat() + amount, 0, ITaoismData.MAX_JING_LI_EAT);
        setData(p, withJingLiEat(d, newVal));
    }

    public static void addJingLiSleep(Player p, int amount) {
        ITaoismData d = getData(p);
        int newVal = Math.clamp(d.getJingLiSleep() + amount, 0, ITaoismData.MAX_JING_LI_SLEEP);
        setData(p, withJingLiSleep(d, newVal));
    }
    // ======================== 初始化 / 重置 ========================

    public static void initYangWorld(Player player, int chengFu) {
        setData(player, new ITaoismData.TaoismData(
            chengFu, true,
            50, 0,
            (50 + chengFu) * 100,
            false,
            10, 10
        ));
    }

    public static void resetForYangWorld(Player player) {
        ITaoismData d = getData(player);
        setData(player, new ITaoismData.TaoismData(
            d.getChengFu(), d.isChengFuInit(),
            50, 0,
            (50 + d.getChengFu()) * 100,
            false,
            10, 10
        ));
    }

    public static void resetForYinWorld(Player player, int chengFu) {
        ITaoismData d = getData(player);
        setData(player, new ITaoismData.TaoismData(
            chengFu, d.isChengFuInit(),
            0, 0,
            chengFu * 20,
            true,
            10, 10
        ));
    }
}
