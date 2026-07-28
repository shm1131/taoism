package io.github.shm1131.taoism.init;

import io.github.shm1131.taoism.TaoismMain;
import io.github.shm1131.taoism.block.DeepZhuShaOre;
import io.github.shm1131.taoism.block.ZhuShaOre;
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

  public static final DeferredBlock<ZhuShaOre> ZHU_SHA_ORE = BLOCKS.registerBlock(
      "zhu_sha_ore",
      ZhuShaOre::new,
      () -> BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_ORE)
  );

  public static final DeferredItem<BlockItem> ZHU_SHA_ORE_ITEM =
      ITEMS.registerSimpleBlockItem(ZHU_SHA_ORE);

    public static final DeferredBlock<DeepZhuShaOre> DEEP_ZHU_SHA_ORE = BLOCKS.registerBlock(
        "deep_zhu_sha_ore",
        DeepZhuShaOre::new,
        () -> BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_IRON_ORE)
    );

    public static final DeferredItem<BlockItem> DEEP_ZHU_SHA_ORE_ITEM =
        ITEMS.registerSimpleBlockItem(DEEP_ZHU_SHA_ORE);
}
