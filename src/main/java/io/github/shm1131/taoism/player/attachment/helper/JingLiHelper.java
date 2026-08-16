package io.github.shm1131.taoism.player.attachment.helper;

import io.github.shm1131.taoism.datagen.dim.ModLevelStems;
import io.github.shm1131.taoism.init.TaoismAttachments;
import io.github.shm1131.taoism.network.SyncJingLiDataPayload;
import io.github.shm1131.taoism.player.attachment.api.IJingLiData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;

public class JingLiHelper {

    // ======================== 行为消耗枚举 ========================
    public enum JingLiAction {
        CRAFT_BASIC(0.4f),
        CRAFT_COMPLEX(1.2f),
        SPRINT_PER_METER(0.05f),
        SWIM_PER_METER(0.015f),
        JUMP(0.025f),
        SPRINT_JUMP(0.4f),
        ATTACK(0.05f),
        BREAK_BLOCK(0.005f),
        HURT(0.1f);

        private final float exhaustion;
        JingLiAction(float exhaustion) { this.exhaustion = exhaustion; }
        public float getExhaustion() { return exhaustion; }
    }

    // ======================== 底层数据读写 ========================

    private static IJingLiData getData(Player player) {
        return player.getData(TaoismAttachments.JINGLI_DATA);
    }

    private static void setData(Player player, IJingLiData data) {
        player.setData(TaoismAttachments.JINGLI_DATA, data);
        if (!player.level().isClientSide() && player instanceof ServerPlayer sp) {
            PacketDistributor.sendToPlayer(sp, new SyncJingLiDataPayload(data));
        }
    }

    private static void setDataSilently(Player player, IJingLiData data) {
        player.setData(TaoismAttachments.JINGLI_DATA, data);
    }

    private static IJingLiData copy(IJingLiData d, float jingLiEat, float jingLiSleep, float exhaustion) {
        return new IJingLiData.JingLiData(jingLiEat, jingLiSleep, exhaustion);
    }

    private static IJingLiData withJingLiEat(IJingLiData d, float jingLiEat) {
        return copy(d, jingLiEat, d.getJingLiSleep(), d.getExhaustion());
    }

    private static IJingLiData withJingLiSleep(IJingLiData d, float jingLiSleep) {
        return copy(d, d.getJingLiEat(), jingLiSleep, d.getExhaustion());
    }

    private static IJingLiData withExhaustion(IJingLiData d, float exhaustion) {
        return copy(d, d.getJingLiEat(), d.getJingLiSleep(), exhaustion);
    }

    // ======================== 基础属性读写 ========================

    public static float getJingLiEat(Player p) {
        return getData(p).getJingLiEat();
    }

    public static float getJingLiSleep(Player p) {
        return getData(p).getJingLiSleep();
    }

    public static float getTotalJingLi(Player p) {
        return getData(p).getTotalJingLi();
    }

    public static void addJingLiEat(Player p, float amount) {
        IJingLiData d = getData(p);
        float newVal = Math.clamp(d.getJingLiEat() + amount, 0, IJingLiData.MAX_JING_LI_EAT);
        setData(p, withJingLiEat(d, newVal));
    }

    public static void addJingLiSleep(Player p, float amount) {
        IJingLiData d = getData(p);
        float newVal = Math.clamp(d.getJingLiSleep() + amount, 0, IJingLiData.MAX_JING_LI_SLEEP);
        setData(p, withJingLiSleep(d, newVal));
    }

    // ======================== 精力消耗核心逻辑 ========================

    public static void exhaustJingLi(Player player, JingLiAction action) {
        exhaustJingLi(player, action.getExhaustion());
    }

    public static void exhaustJingLi(Player player, float amount) {
        if (player.level().isClientSide()) return;
        if (player.isCreative() || player.isSpectator()) return;
        if (player.level().dimension().equals(ModLevelStems.TAOISM_REALM_KEY)) return;

        IJingLiData d = getData(player);
        float currentExhaust = d.getExhaustion();
        float totalExhaust = currentExhaust + amount;

        float currentEat = d.getJingLiEat();
        float currentSleep = d.getJingLiSleep();
        boolean needSync = false;

        while (totalExhaust >= IJingLiData.EXHAUSTION_THRESHOLD) {
            totalExhaust -= IJingLiData.EXHAUSTION_THRESHOLD;

            if (currentEat > 0) {
                currentEat = Math.max(0, currentEat - 1.0f);
                needSync = true;
            } else if (currentSleep > 0) {
                currentSleep = Math.max(0, currentSleep - 1.0f);
                needSync = true;
            }
        }

        IJingLiData newData = new IJingLiData.JingLiData(currentEat, currentSleep, totalExhaust);

        if (needSync) {
            setData(player, newData);
        } else {
            setDataSilently(player, newData);
        }
    }

    // ======================== 初始化 / 重置 ========================

    public static void init(Player player) {
        setData(player, IJingLiData.EMPTY);
    }

    public static void resetForYangWorld(Player player) {
        setData(player, new IJingLiData.JingLiData(10.0f, 10.0f, 0.0f));
    }

    public static void resetForYinWorld(Player player) {
        setData(player, new IJingLiData.JingLiData(10.0f, 10.0f, 0.0f));
    }
}
