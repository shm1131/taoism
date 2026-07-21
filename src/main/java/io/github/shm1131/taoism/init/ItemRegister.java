package io.github.shm1131.taoism.init;

import io.github.shm1131.taoism.TaoismMain;
import io.github.shm1131.taoism.item.herbs.base.Flavor;
import io.github.shm1131.taoism.item.herbs.base.HerbProperties;
import io.github.shm1131.taoism.item.herbs.base.Nature;
import io.github.shm1131.taoism.item.herbs.dang_gui.DangGui;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ItemRegister {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, TaoismMain.MODID);

    private static Item.Properties herbProps(String name) {
        return new Item.Properties()
                .setId(ResourceKey.create(
                        Registries.ITEM,
                        Identifier.fromNamespaceAndPath(TaoismMain.MODID, name)
                ));
    }

    public static final DeferredHolder<Item, DangGui> DANG_GUI = ITEMS.register(
            "dang_gui",
            () -> new DangGui(
                    herbProps("dang_gui"),
                    new HerbProperties(
                            Flavor.BITTER,
                            Nature.COLD,
                            0.5f,
                            0.5f
                    )
            )
    );

}
