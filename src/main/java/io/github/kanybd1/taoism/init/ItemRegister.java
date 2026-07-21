package io.github.kanybd1.taoism.init;

import io.github.kanybd1.taoism.TaoismMain;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ItemRegister {
  public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, TaoismMain.MODID);
}
