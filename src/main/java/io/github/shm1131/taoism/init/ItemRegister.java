package io.github.shm1131.taoism.init;

import io.github.shm1131.taoism.TaoismMain;
import io.github.shm1131.taoism.item.Cinnabar;
import io.github.shm1131.taoism.item.CinnabarDust;
import io.github.shm1131.taoism.item.IconItem;
import io.github.shm1131.taoism.item.herb.pill.PillItem;
import io.github.shm1131.taoism.item.talismans.YellowPaper;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ItemRegister {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, TaoismMain.MODID);

     public static final DeferredHolder<Item, IconItem> ICON_ITEM = ITEMS.register("icon_item",
        () -> new IconItem(new Item.Properties()
            .setId(
               ResourceKey.create(
                  Registries.ITEM,
                  Identifier.fromNamespaceAndPath(TaoismMain.MODID,"icon_item")
                  )
              )
         )
      );

    public static final DeferredHolder<Item, YellowPaper> YELLOW_PAPER = ITEMS.register("yellow_paper",
          () -> new YellowPaper(new Item.Properties()
              .setId(
                  ResourceKey.create(
                      Registries.ITEM,
                      Identifier.fromNamespaceAndPath(TaoismMain.MODID,"yellow_paper")
                  )
              )
          )
    );

    //ADDED:加入丹药物品的注册
    public static final DeferredHolder<Item, PillItem> FLORISTIC_PILL = ITEMS.register("pill",
          () -> new PillItem(new Item.Properties()
              .setId(
                  ResourceKey.create(
                      Registries.ITEM,
                      Identifier.fromNamespaceAndPath(TaoismMain.MODID,"pill")
                  )
              )
          )
    );


    public static final DeferredHolder<Item, Cinnabar> CINNABAR = ITEMS.register("cinnabar",
        () -> new Cinnabar(new Item.Properties()
            .setId(
                ResourceKey.create(
                    Registries.ITEM,
                    Identifier.fromNamespaceAndPath(TaoismMain.MODID,"cinnabar")
                )
            )
        )
    );

    public static final DeferredHolder<Item, CinnabarDust> CINNABAR_DUST = ITEMS.register("cinnabar_dust",
        () -> new CinnabarDust(new Item.Properties()
            .setId(
                ResourceKey.create(
                    Registries.ITEM,
                    Identifier.fromNamespaceAndPath(TaoismMain.MODID,"cinnabar_dust")
                )
            )
        )
    );
}
