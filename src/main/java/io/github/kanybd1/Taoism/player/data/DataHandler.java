package io.github.kanybd1.Taoism.player.data;

import io.github.kanybd1.Taoism.player.data.attachment.ITaoismData;
import io.github.kanybd1.Taoism.player.data.attachment.TaoismAttachments;
import io.github.kanybd1.Taoism.player.data.attachment.TaoismHelper;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import static net.minecraft.network.chat.Component.literal;

@EventBusSubscriber
public class DataHandler {

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if(event.getEntity().level().isClientSide()){return;}
        var player = event.getEntity();

        ITaoismData data = player.getData(TaoismAttachments.TAOISM_DATA);
        if (!data.isChengFuInit()) {
            int chengFu = player.getRandom().nextInt(100);
            TaoismHelper.initialize(player, chengFu, 50, (50 + chengFu) * 3000);
            player.sendSystemMessage(literal("§6✦ 天道已定，你的承负为：" + chengFu));
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Pre event) {
        if (event.getEntity().level().isClientSide()) {return;}
        var player = event.getEntity();

        if (player.tickCount % 20 != 0) return;

        ITaoismData data = player.getData(TaoismAttachments.TAOISM_DATA);
        if (!data.isChengFuInit()) return;

        if (data.getShouMing() <= 0) {
            player.kill((ServerLevel) player.level());
            return;
        }
        TaoismHelper.addShouMing(player, -20);
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event){
        if(event.getEntity().level().isClientSide()){return;}
        var player = event.getEntity();
        int chengFu =  player.getData(TaoismAttachments.TAOISM_DATA).getChengFu();

        TaoismHelper.resetForReincarnation(player, 50, (50 + chengFu) * 12000);
        player.sendSystemMessage(literal("§6✦ 天道既承，你的承负为：" + chengFu));
    }
}
