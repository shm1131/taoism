package io.github.shm1131.taoism.handler;

import io.github.shm1131.taoism.init.TaoismAttachments;
import io.github.shm1131.taoism.player.attachment.api.IJingLiData;
import io.github.shm1131.taoism.player.attachment.api.ISleepData;
import io.github.shm1131.taoism.player.attachment.helper.JingLiHelper;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.level.block.BreakBlockEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import static net.minecraft.network.chat.Component.literal;

@EventBusSubscriber
public class EnergyHandler {

    private static final long SLEEP_START_TIME = 16000L;
    private static int cooldown = 0;

    // ==================== 工具方法：安全读写 SleepData ====================

    private static ISleepData getSleepData(Player player) {
        return player.getData(TaoismAttachments.SLEEP_DATA);
    }

    private static void setSleepData(Player player, ISleepData data) {
        player.setData(TaoismAttachments.SLEEP_DATA, data);
    }

    // ==================== 玩家 Tick：警告消息 + 熬夜惩罚 ====================

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Pre event) {
        if (cooldown <= 20) {cooldown++;}

        Player player = event.getEntity();
        if (player.level().isClientSide()) return;

        IJingLiData jingLi = player.getData(TaoismAttachments.JINGLI_DATA);
        ISleepData sleep = getSleepData(player);

        boolean lowWarning = sleep.isLowWarning();
        boolean lastWarning = sleep.isLastWarning();
        boolean stayUpLateWarning = sleep.isStayUpLateWarning();

        float totalJingLi = jingLi.getTotalJingLi();
        float eatJingLi = jingLi.getJingLiEat();

        // --- 精力低警告（基于总精力，阈值5触发，阈值10清除）---
        if (!lowWarning && totalJingLi <= 5 && totalJingLi > 0) {
            lowWarning = true;
            player.sendSystemMessage(literal("疲惫感袭来"));
        } else if (lowWarning && totalJingLi > 10) {
            // ✅ 使用同一数据源 + 滞后区间，避免边界震荡
            lowWarning = false;
        }

        // --- 精力耗尽警告（基于总精力，0触发，>5清除）---
        if (!lastWarning && totalJingLi == 0) {
            lastWarning = true;
            player.sendSystemMessage(literal("精力透支，尽快休息"));
        } else if (lastWarning && totalJingLi > 5) {
            // ✅ 使用同一数据源 + 滞后区间
            lastWarning = false;
        }

        // --- 熬夜警告（仅触发一次）---
        if (sleep.isStayUpLate()) {
            if (!stayUpLateWarning) {
                stayUpLateWarning = true;
                player.sendSystemMessage(literal("请注意休息"));
            }
            JingLiHelper.exhaustJingLi(player, JingLiHelper.JingLiAction.STAY_UP_LATE);
        }

        // --- 写回 Attachment（仅在状态变化时写入）---
        if (lowWarning != sleep.isLowWarning()

            || lastWarning != sleep.isLastWarning()
            || stayUpLateWarning != sleep.isStayUpLateWarning()) {
            setSleepData(player, new ISleepData.SleepData(
                lowWarning, lastWarning, stayUpLateWarning, sleep.isStayUpLate()));
        }
    }

    // ==================== 合成事件 ====================

    @SubscribeEvent
    public static void onCraft(PlayerEvent.ItemCraftedEvent event) {
        Player player = event.getEntity();
        if (player.level().isClientSide()) return;

        JingLiHelper.exhaustJingLi(player, JingLiHelper.JingLiAction.CRAFT_BASIC);

        if (player.getData(TaoismAttachments.JINGLI_DATA).getTotalJingLi() == 0 && cooldown >= 20) {
            player.sendSystemMessage(literal("思绪混沌，合成失败"));
        }
    }

    // ==================== 破坏方块事件 ====================

    @SubscribeEvent
    public static void onBlockBreak(BreakBlockEvent event) {
        if (!(event.getPlayer() instanceof ServerPlayer player)) return;

        JingLiHelper.exhaustJingLi(player, JingLiHelper.JingLiAction.BREAK_BLOCK);

        if (player.getData(TaoismAttachments.JINGLI_DATA).getTotalJingLi() <= 0) {
            event.setCanceled(true);
            player.sendSystemMessage(literal("精力不足，无法采集"));
        }
    }

    // ==================== 进食事件 ====================

    @SubscribeEvent
    public static void onPlayerEat(LivingEntityUseItemEvent.Finish event) {
        if (!(event.getEntity() instanceof Player player)) return; // ⚠️ 修复：原代码逻辑取反了
        if (player.level().isClientSide()) return;

        ItemStack stack = event.getItem();
        if (!stack.has(DataComponents.FOOD)) return; // ⚠️ 修复：原代码逻辑取反了

        JingLiHelper.restoreJingLiEat(player, 4);
    }

    // ==================== 世界 Tick：熬夜状态切换 ====================

    @SubscribeEvent
    public static void onLevelTick(LevelTickEvent.Post event) {
        if (!(event.getLevel() instanceof ServerLevel level)) return;
        if (level.dimension() != Level.OVERWORLD) return;

        long current = level.getOverworldClockTime() % 24000L;
        long previous = (current - 1L + 24000L) % 24000L;

        boolean justEnteredSleepWindow = previous < SLEEP_START_TIME && current >= SLEEP_START_TIME;
        boolean justLeftSleepWindow = previous >= SLEEP_START_TIME && current < SLEEP_START_TIME;
        boolean isInSleepWindow = current >= SLEEP_START_TIME;

        for (ServerPlayer player : level.getServer().getPlayerList().getPlayers()) {
            if (player.level().dimension() != Level.OVERWORLD) continue;

            ISleepData sleep = getSleepData(player);

            if (justEnteredSleepWindow || (isInSleepWindow && !sleep.isStayUpLate())) {
                if (!player.isSleeping()) {
                    setSleepData(player, new ISleepData.SleepData(
                        sleep.isLowWarning(), sleep.isLastWarning(),
                        sleep.isStayUpLateWarning(), true));
                }
            } else if (justLeftSleepWindow) {
                if (sleep.isStayUpLate() || sleep.isStayUpLateWarning()) {
                    setSleepData(player, new ISleepData.SleepData(
                        sleep.isLowWarning(), sleep.isLastWarning(),
                        false, false));
                }
            }
        }
    }
}
