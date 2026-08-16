//ADDED:新建类PillHelper

package io.github.shm1131.taoism.item.herb;

import io.github.shm1131.taoism.item.herb.base.Flavor;
import io.github.shm1131.taoism.item.herb.base.ModDataComponents;
import io.github.shm1131.taoism.item.herb.base.Nature;
import io.github.shm1131.taoism.item.herb.base.HerbProperties;
import net.minecraft.world.item.ItemStack;

public final class PropertiesHelper {
    private PropertiesHelper() {}

    public static HerbProperties getProperties(ItemStack stack) {
        return stack.getOrDefault(
            ModDataComponents.PILL_PROPERTIES.get(),
            new HerbProperties(Flavor.SWEET, Nature.NEUTRAL, 0f, 0f)
        );
    }

    public static void setProperties(ItemStack stack, HerbProperties props) {
        stack.set(ModDataComponents.PILL_PROPERTIES.get(), props);
    }
}
