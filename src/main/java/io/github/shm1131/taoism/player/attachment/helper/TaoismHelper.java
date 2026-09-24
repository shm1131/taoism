package io.github.shm1131.taoism.player.attachment.helper;

import io.github.shm1131.taoism.init.TaoismAttachments;
import io.github.shm1131.taoism.network.SyncTaoismDataPayload;
import io.github.shm1131.taoism.player.attachment.api.ICoolDownData;
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
                                    int shouMing) {
        return new ITaoismData.TaoismData(
            chengFu, chengFuInit,
            xianTianQi, houTianQi,
            shouMing
        );
    }

    private static ITaoismData withChengFu(ITaoismData d, int chengFu) {
        return copy(d, chengFu, d.isChengFuInit(), d.getXianTianQi(), d.getHouTianQi(), d.getShouMing());
    }

    private static ITaoismData withXianTianQi(ITaoismData d, int xianTianQi) {
        return copy(d, d.getChengFu(), d.isChengFuInit(), xianTianQi, d.getHouTianQi(), d.getShouMing());
    }

    private static ITaoismData withHouTianQi(ITaoismData d, int houTianQi) {
        return copy(d, d.getChengFu(), d.isChengFuInit(), d.getXianTianQi(), houTianQi, d.getShouMing());
    }

    private static ITaoismData withShouMing(ITaoismData d, int shouMing) {
        return copy(d, d.getChengFu(), d.isChengFuInit(), d.getXianTianQi(), d.getHouTianQi(), shouMing);
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
        setData(player, withXianTianQi(d, Math.max(0, d.getXianTianQi() + amount)));
    }

    // ======================== HouTianQi ========================

    public static int getHouTianQi(Player player) {
        return getData(player).getHouTianQi();
    }

    public static void addHouTianQi(Player player, int amount) {
        ITaoismData d = getData(player);
        setData(player, withHouTianQi(d, Math.max(0, d.getHouTianQi() + amount)));
    }

    // ======================== ShouMing ========================

    public static int getShouMing(Player player) {
        return getData(player).getShouMing();
    }

    public static void addShouMing(Player player, int amount) {
        ITaoismData d = getData(player);
        setData(player, withShouMing(d, d.getShouMing() + amount));
    }

    // ======================== 初始化 / 重置 ========================

    public static void initYangWorld(Player player, int chengFu) {
        setData(player, new ITaoismData.TaoismData(
            chengFu, true,
            50, 0,
            (100 - chengFu) * 100
        ));
    }

    /**
     * 重置数据（原 resetForYangWorld），不再区分阴阳界
     */
    public static void resetData(Player player) {
        ITaoismData d = getData(player);
        setData(player, new ITaoismData.TaoismData(
            d.getChengFu(), d.isChengFuInit(),
            50, 0,
            (100 - d.getChengFu()) * 100
        ));
    }

    public static long getChengFuCoolDown(Player player) {
        return player.getData(TaoismAttachments.COOL_DOWN_DATA).getChengFuCoolDown();
    }

    public static void setChengFuCoolDown(Player player, long timestamp) {
        ICoolDownData d = player.getData(TaoismAttachments.COOL_DOWN_DATA);
        ICoolDownData newData = new ICoolDownData.CoolDownData(d.getDropCoolDown(), timestamp);
        player.setData(TaoismAttachments.COOL_DOWN_DATA, newData);
    }

    public static boolean isChengFuReady(Player player, long cooldownMs) {
        long last = getChengFuCoolDown(player);
        return System.currentTimeMillis() - last >= cooldownMs;
    }
}
