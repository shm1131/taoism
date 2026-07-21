package io.github.shm1131.taoism.data;

import com.mojang.logging.LogUtils;
import io.github.kanybd1.taoism.level.datagen.ModLevelStems;
import io.github.shm1131.taoism.data.attachment.TaoismHelper;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import org.slf4j.Logger;


import java.util.stream.Collectors;

import static net.minecraft.network.chat.Component.literal;

@EventBusSubscriber
public class TaoismDataHandler {

    public static final Logger LOGGER = LogUtils.getLogger();

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity().level().isClientSide()) return;
        var player = event.getEntity();

        ITaoismData data = player.getData(TaoismAttachments.TAOISM_DATA);
        if (!data.isChengFuInit()) {
            int chengFu = player.getRandom().nextInt(100);
            TaoismHelper.initYangWorld(player, chengFu);
            player.sendSystemMessage(literal("§6✦ 天道已定，你的承负为：" + chengFu));
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Pre event) {
        if (event.getEntity().level().isClientSide()) return;
        var player = event.getEntity();
        if (player.tickCount % 20 != 0) return;

        ITaoismData data = player.getData(TaoismAttachments.TAOISM_DATA);
        if (!data.isChengFuInit()) return;

        if (data.getShouMing() <= 0) {
            boolean actuallyInRealm = player.level().dimension()
                    .equals(ModLevelStems.TAOISM_REALM_KEY);
            TaoismHelper.setInRealm(player, actuallyInRealm);
            player.kill((ServerLevel) player.level());
            return;
        }
        TaoismHelper.addShouMing(player, -20);
    }

    @SubscribeEvent
    public static void onPlayerDeath(LivingDeathEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        ITaoismData data = player.getData(TaoismAttachments.TAOISM_DATA);
        if (!data.isChengFuInit()) return;

        boolean actuallyInRealm = player.level().dimension()
                .equals(ModLevelStems.TAOISM_REALM_KEY);
        TaoismHelper.setInRealm(player, actuallyInRealm);

        LOGGER.debug("Player {} died in dimension {}, isInRealm set to {}",
                player.getName().getString(),
                player.level().dimension(),
                actuallyInRealm);
    }

    /**
     * Clone 事件：死亡时在旧玩家数据上标记"下一次重生应去的维度"
     * 注意：wasDeath=true 才是死亡重生，false 是末地返回主世界，不要干扰
     */
    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        if (!event.isWasDeath()) return;
        if (event.getEntity().level().isClientSide()) return;

        // ✅ 仅原样拷贝死亡时的维度状态，保持 isInRealm 语义为"死亡时所在维度"
        ITaoismData oldData = event.getOriginal().getData(TaoismAttachments.TAOISM_DATA);
        TaoismHelper.setInRealm(event.getEntity(), oldData.isInRealm());

        LOGGER.debug("Clone: copied isInRealm={} from dead player", oldData.isInRealm());
    }

    /**
     * Respawn 事件：新玩家已创建完毕，根据预设的 isInRealm 执行延迟传送 + 数据重置
     */
    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (event.getEntity().level().isClientSide()) return;
        if (event.isEndConquered()) return; // 末地返回不触发轮回

        ServerPlayer player = (ServerPlayer) event.getEntity();
        ITaoismData data = player.getData(TaoismAttachments.TAOISM_DATA);
        int chengFu = data.getChengFu();
        boolean targetIsYin = !data.isInRealm(); // Clone 中已预设为目标维度

        if (targetIsYin) {
            ServerLevel yinLevel = player.level().getServer().getLevel(ModLevelStems.TAOISM_REALM_KEY);

            if (yinLevel == null) {
                player.sendSystemMessage(literal("§c✦ 道境未加载，无法轮回！"));
                LOGGER.error("TAOISM_REALM_KEY not found! Registered dimensions: {}",
                        player.level().getServer().levelKeys().stream()
                                .map(ResourceKey::toString)
                                .collect(Collectors.joining(", ")));
                return;
            }

            // ✅ 固定重生高度 150，不再依赖高度图
            final int SPAWN_Y = 150;

            player.sendSystemMessage(literal("§5✦ 魂归幽冥，承负：" + chengFu
                    + "，阴寿：" + (chengFu * 600)));

            player.level().getServer().execute(() -> {
                player.teleportTo(
                        yinLevel,
                        0,
                        SPAWN_Y,
                        0,
                        java.util.Set.of(),
                        0f,
                        0f,
                        false
                );

                player.addEffect(new net.minecraft.world.effect.MobEffectInstance(
                        net.minecraft.world.effect.MobEffects.SLOW_FALLING,
                        chengFu * 600,   // 持续时间：1200 ticks = 60 秒
                        0,      // 等级 0（最低级，仅防摔伤，不影响下落手感）
                        false,  // 不显示粒子
                        true,   // 显示图标
                        true    // 可见
                ));

                TaoismHelper.resetForYinWorld(player, chengFu);
            });
        }else {
            // ☀️ 目标：阳间 → 已在主世界重生，只需重置数据
            TaoismHelper.resetForYangWorld(player);
            player.sendSystemMessage(literal("§6✦ 重入人间，承负：" + chengFu));
        }

        TaoismHelper.setInRealm(player, targetIsYin);
    }
}