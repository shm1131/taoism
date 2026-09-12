package io.github.shm1131.taoism.handler;

import io.github.shm1131.taoism.init.TaoismAttachments;
import io.github.shm1131.taoism.network.SyncJingLiDataPayload;
import io.github.shm1131.taoism.player.attachment.api.IJingLiData;
import io.github.shm1131.taoism.player.attachment.api.ITaoismData;
import io.github.shm1131.taoism.player.attachment.helper.JingLiHelper;
import io.github.shm1131.taoism.player.attachment.helper.TaoismHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import static net.minecraft.network.chat.Component.literal;

@EventBusSubscriber
public class TaoismDataHandler {

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity().level().isClientSide()) return;
        var player = event.getEntity();

        IJingLiData data1 = player.getData(TaoismAttachments.JINGLI_DATA.get());
        PacketDistributor.sendToPlayer((ServerPlayer) player, new SyncJingLiDataPayload(data1));

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
            player.kill((ServerLevel) event.getEntity().level());
            return;
        }

        TaoismHelper.addShouMing(player, -20);
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (event.getEntity().level().isClientSide()) return;
        if (event.isEndConquered()) return;

        ServerPlayer player = (ServerPlayer) event.getEntity();
        ITaoismData data = player.getData(TaoismAttachments.TAOISM_DATA);
        int chengFu = data.getChengFu();

        // 统一重置数据
        TaoismHelper.resetData(player);
        JingLiHelper.init(player);

        player.sendSystemMessage(literal("§6✦ 重入人间，承负：" + chengFu));
    }
}
