package io.github.shm1131.taoism.handler;

import io.github.shm1131.taoism.player.attachment.helper.JingLiHelper;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

@EventBusSubscriber
public class EnergyHandler {
    @SubscribeEvent
    public static void onCraft(PlayerEvent.ItemCraftedEvent event) {
        JingLiHelper.exhaustJingLi(event.getEntity(), JingLiHelper.JingLiAction.CRAFT_BASIC);
    }
}
