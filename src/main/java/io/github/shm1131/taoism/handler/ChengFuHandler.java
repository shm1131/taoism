package io.github.shm1131.taoism.handler;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;

@EventBusSubscriber
public class ChengFuHandler {
    @SubscribeEvent
    public static void onKillEntity(LivingDeathEvent event) {
        Entity entity = event.getEntity().getLastAttacker();
        if(entity instanceof Player player) {
            return;
        }
        

    }

}
