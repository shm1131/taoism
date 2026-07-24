package io.github.shm1131.taoism.init;

import io.github.shm1131.taoism.TaoismMain;
import io.github.shm1131.taoism.item.IconItem;
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
}
