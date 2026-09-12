package io.github.shm1131.taoism.init;

import io.github.shm1131.taoism.TaoismMain;
import io.github.shm1131.taoism.block.alchemy.AlchemyFurnaceMenu;
import io.github.shm1131.taoism.block.incubator.IncubatorMenu;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS =
        DeferredRegister.create(BuiltInRegistries.MENU, TaoismMain.MODID);

    public static final Supplier<MenuType<IncubatorMenu>> INCUBATOR_MENU =
        MENUS.register("incubator", () -> IMenuTypeExtension.create(IncubatorMenu::new));

    public static final Supplier<MenuType<AlchemyFurnaceMenu>> ALCHEMY_FURNACE =
        MENUS.register("alchemy_furnace", () -> IMenuTypeExtension.create(AlchemyFurnaceMenu::new));
}
