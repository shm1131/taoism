package io.github.shm1131.taoism.item.herb;

import io.github.shm1131.taoism.TaoismMain;
import io.github.shm1131.taoism.init.HerbItemRegister;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.core.Holder;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.stream.Stream;

public class ModItemModelProvider extends ModelProvider {

    public ModItemModelProvider(PackOutput output) {
        super(output, TaoismMain.MODID);
    }

    @Override
    protected Stream<? extends Holder<Block>> getKnownBlocks() {
        return Stream.empty();
    }

    @Override
    protected Stream<? extends Holder<Item>> getKnownItems() {
        return HerbItemRegister.ITEMS.getEntries().stream()
            .filter(holder -> !(holder.value() instanceof BlockItem))  // ⭐️ 排除所有 BlockItem
            .map(holder -> holder.value().builtInRegistryHolder());
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        for (DeferredHolder<Item, ? extends Item> holder : HerbItemRegister.ITEMS.getEntries()) {
            Item item = holder.value();
            if (!(item instanceof BlockItem)) {
                itemModels.generateFlatItem(item, ModelTemplates.FLAT_ITEM);
            }
        }
    }
}
