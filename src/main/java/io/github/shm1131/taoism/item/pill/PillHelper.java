//ADDED:新建类PillHelper

package io.github.shm1131.taoism.item.pill;

import io.github.shm1131.taoism.item.herb.base.Flavor;
import io.github.shm1131.taoism.item.herb.base.Nature;
import net.minecraft.world.item.ItemStack;

public final class PillHelper {
    private PillHelper() {}

    public static PillProperties getProperties(ItemStack stack) {
        return stack.getOrDefault(
            ModDataComponents.PILL_PROPERTIES.get(),
            new PillProperties(Flavor.SWEET, Nature.NEUTRAL, 0f, 0f)
        );
    }

    public static void setProperties(ItemStack stack, PillProperties props) {
        stack.set(ModDataComponents.PILL_PROPERTIES.get(), props);
    }
}
