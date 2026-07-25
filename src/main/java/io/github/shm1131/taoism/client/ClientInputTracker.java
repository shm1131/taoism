//ADDED:新建ClientInputTracker类来获取键盘动作

package io.github.shm1131.taoism.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;

@EventBusSubscriber(value = Dist.CLIENT, modid = "taoism")
public final class ClientInputTracker {
    private static volatile boolean shiftDown = false;

    private ClientInputTracker() {}

    public static boolean isShiftDown() {
        return shiftDown;
    }

    @SubscribeEvent
    public static void onKey(InputEvent.Key event) {
        // GLFW_KEY_LEFT_SHIFT = 340, GLFW_KEY_RIGHT_SHIFT = 348
        if (event.getKey() == 340 || event.getKey() == 348) {
            // action: 1=PRESS, 2=REPEAT, 0=RELEASE
            shiftDown = event.getAction() != 0;
        }
    }
}
