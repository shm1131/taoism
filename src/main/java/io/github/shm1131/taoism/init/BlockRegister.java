package io.github.shm1131.taoism.init;

import io.github.shm1131.taoism.TaoismMain;
import io.github.shm1131.taoism.block.ore.DeepZhuShaOre;
import io.github.shm1131.taoism.block.ore.ZhuShaOre;
import io.github.shm1131.taoism.block.Incubator;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class BlockRegister {

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(TaoismMain.MODID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(TaoismMain.MODID);
    //通过Map来查找批量注册的Block与BlockItem
    public static Map<String, DeferredBlock<?>> SIMPLE_BLOCKS = new HashMap<>();
    public static Map<String, DeferredItem<?>> SIMPLE_ITEMS = new HashMap<>();
    //用来快速批量注册
    static List<String> StoneOreList = List.of(
        "yun_mu_ore",
        "yin_ore",
        "qian_ore"
    );
    static List<String> DeepslateOreList = List.of(
        "deep_yin_ore",
        "deep_qian_ore"
    );
    //批量注册逻辑
    static {
        Supplier<BlockBehaviour.Properties> StoneOreProperties = ()->  BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 3.0F);
        Supplier<BlockBehaviour.Properties> DeepslateOreProperties = ()-> BlockBehaviour.Properties.of().mapColor(MapColor.DEEPSLATE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(4.5F, 3.0F).sound(SoundType.DEEPSLATE);
        for (String name : StoneOreList){
            registerSimpleBlockWithBItem(name,StoneOreProperties);
        }
        for (String name : DeepslateOreList){
            registerSimpleBlockWithBItem(name,DeepslateOreProperties);
        }
    }


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


    protected static void registerSimpleBlockWithBItem(String name, Supplier<BlockBehaviour.Properties> properties){
        registerSimpleBlockWithBItem(name,properties,true);
    }

    protected static void registerSimpleBlockWithBItem(String name, Supplier<BlockBehaviour.Properties> properties,Boolean createBlockItem){
        DeferredBlock<?> Block = BLOCKS.registerBlock(name, Block::new, properties);
        SIMPLE_BLOCKS.put(name,Block);
        if (createBlockItem) {
            DeferredItem<BlockItem> Item = ITEMS.registerSimpleBlockItem(Block);
            SIMPLE_ITEMS.put(name, Item);
        }
    }
}
