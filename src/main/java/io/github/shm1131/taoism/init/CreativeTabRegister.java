package io.github.shm1131.taoism.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static io.github.shm1131.taoism.TaoismMain.MODID;

public class CreativeTabRegister {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);


    public static final Supplier<CreativeModeTab> TAOISM_TAB = TABS.register("taoism",
            ()-> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup."+MODID))
                    .icon(()-> new ItemStack(
                        ItemRegister.ICON_ITEM.get()
                    ))
                    .displayItems((params,output)->{
                        output.accept(ItemRegister.ICON_ITEM.get());
                        output.accept(ItemRegister.DANG_GUI.get());
                    })
                    .build()
            );
}
