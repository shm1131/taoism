package io.github.shm1131.taoism.init;

import io.github.shm1131.taoism.TaoismMain;
import io.github.shm1131.taoism.item.bar.ZhuSha;
import io.github.shm1131.taoism.item.IconItem;
import io.github.shm1131.taoism.item.herb.pill.PillItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemRegister {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, TaoismMain.MODID);

    public static Map<String, DeferredHolder<Item, ? extends Item>> SIMPLE_ITEMS = new HashMap<>();

    public static List<String> SimpleItems = List.of(
        "yellow_paper",

        "yun_mu",
        "cu_yin",
        "yin_ding",
        "qian_pian"
    );

    static {
        for (String name : SimpleItems){
            DeferredHolder<Item, ? extends Item> def = ItemRegister.ITEMS.register(name,
                ()->new Item(new Item.Properties().setId(
                    ResourceKey.create(
                        Registries.ITEM,
                        Identifier.fromNamespaceAndPath(TaoismMain.MODID,name)
                    )
                ))
            );
            SIMPLE_ITEMS.put(name, def);
        }
    }





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


    public static final DeferredHolder<Item, ZhuSha> ZHU_SHA = ITEMS.register("zhu_sha",
        () -> new ZhuSha(new Item.Properties()
            .setId(
                ResourceKey.create(
                    Registries.ITEM,
                    Identifier.fromNamespaceAndPath(TaoismMain.MODID,"zhu_sha")
                )
            )
        )
    );
}
