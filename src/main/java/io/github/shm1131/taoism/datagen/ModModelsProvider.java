package io.github.shm1131.taoism.datagen;

import io.github.shm1131.taoism.TaoismMain;
import io.github.shm1131.taoism.init.BlockRegister;
import io.github.shm1131.taoism.init.HerbItemRegister;
import io.github.shm1131.taoism.init.ItemRegister;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.core.Holder;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;


import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

public class ModModelsProvider extends ModelProvider {
    public ModModelsProvider(PackOutput output) {
        super(output, TaoismMain.MODID);
    }

    protected static final Set<String> ITEM_BLACKLIST =Set.of(

    );//黑名单，使用路径名，名单里的物品不会进行自动生成

    protected static final Set<String> BLOCK_BLACKLIST =Set.of(
        "incubator"
    );//黑名单，使用路径名，名单里的方块不会进行自动生成


    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {

        getKnownBlocks()
            .forEach(holder -> {
                Block block = holder.value();
                blockModels.createTrivialCube(block);
            });
        //生成标准整立方体模型

        getKnownItems()
            .filter(holder -> !( holder.value() instanceof BlockItem))//剔除BlockItem
            .forEach(holder -> {
                Item item = holder.value();
                itemModels.generateFlatItem(item, ModelTemplates.FLAT_ITEM);
            });
        //生成标准扁平模型
    }

    @Override
    protected Stream<? extends Holder<Item>> getKnownItems() {
        return Stream.of(
            ItemRegister.ITEMS.getEntries().stream(),
            BlockRegister.ITEMS.getEntries().stream(),
            HerbItemRegister.ITEMS.getEntries().stream()
        ).flatMap(Function.identity())
            .filter(holder -> ! ITEM_BLACKLIST.contains(holder.getId().getPath()));
        //找时间把这些物品注册表合并了吧
    }

    @Override
    protected Stream<? extends Holder<Block>> getKnownBlocks() {
        return BlockRegister.BLOCKS.getEntries().stream()
            .filter(holder -> ! BLOCK_BLACKLIST.contains(holder.getId().getPath()));
    }
}
