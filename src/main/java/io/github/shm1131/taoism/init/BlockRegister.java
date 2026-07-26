package io.github.shm1131.taoism.init;

import io.github.shm1131.taoism.TaoismMain;
import io.github.shm1131.taoism.block.CinnabarOre;
import io.github.shm1131.taoism.block.Incubator;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BlockRegister {

  public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(TaoismMain.MODID);
  public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(TaoismMain.MODID);

  public static final DeferredBlock<Incubator> SPECIAL_BLOCK = BLOCKS.registerBlock(
      "incubator",
      Incubator::new,
      () -> BlockBehaviour.Properties.of().strength(4.0f)
  );
  public static final DeferredItem<BlockItem> SPECIAL_BLOCK_ITEM =
      ITEMS.registerSimpleBlockItem(SPECIAL_BLOCK);

  public static final DeferredBlock<CinnabarOre> CINNABAR_ORE = BLOCKS.registerBlock(
      "cinnabar_ore",
      CinnabarOre::new,
      () -> BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_ORE)
  );

  public static final DeferredItem<BlockItem> CINNABAR_ORE_ITEM =
      ITEMS.registerSimpleBlockItem(CINNABAR_ORE);


}
