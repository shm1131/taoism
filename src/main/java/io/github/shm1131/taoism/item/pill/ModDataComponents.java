//ADDED:新建类ModDataComponents

package io.github.shm1131.taoism.item.pill;

import io.github.shm1131.taoism.TaoismMain;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModDataComponents {
    public static final DeferredRegister.DataComponents COMPONENTS =
        DeferredRegister.createDataComponents(
            Registries.DATA_COMPONENT_TYPE,
            TaoismMain.MODID
        );

    public static final Supplier<DataComponentType<PillProperties>> PILL_PROPERTIES =
        COMPONENTS.registerComponentType(
            "pill_properties",
            builder -> builder
                .persistent(PillProperties.CODEC)
                .networkSynchronized(PillProperties.STREAM_CODEC)
        );

    public static void register(IEventBus bus) {
        COMPONENTS.register(bus);
    }
}
