//ADDED:新建类PillHelper

package io.github.shm1131.taoism.item.herb;

import io.github.shm1131.taoism.item.herb.base.Flavor;
import io.github.shm1131.taoism.init.ComponentsRegister;
import io.github.shm1131.taoism.item.herb.base.IHerbBase;
import io.github.shm1131.taoism.item.herb.base.Nature;
import io.github.shm1131.taoism.item.herb.base.HerbProperties;
import net.minecraft.world.item.ItemStack;

public final class PropertiesHelper {
    private PropertiesHelper() {}

    public static final HerbProperties DEFAULT_PROPS =
        new HerbProperties(Flavor.SWEET, Nature.NEUTRAL, 0f, 0f);

    public static HerbProperties getProperties(ItemStack stack) {
        HerbProperties comp = stack.get(ComponentsRegister.PILL_PROPERTIES.get());
        if (comp != null) return comp;

        if (stack.getItem() instanceof IHerbBase herb) {
            return herb.getHerbProperties();
        }

        return DEFAULT_PROPS;
    }

    public static void setProperties(ItemStack stack, HerbProperties props) {
        stack.set(ComponentsRegister.PILL_PROPERTIES.get(), props);
    }
}
