package io.github.shm1131.taoism.datagen;

import io.github.shm1131.taoism.init.BlockRegister;
import io.github.shm1131.taoism.init.ItemRegister;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.Set;

public class ModBlockLootTablesProvider extends BlockLootSubProvider {

    public ModBlockLootTablesProvider(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.DEFAULT_FLAGS, registries);
    }

    @Override
    public void generate(){
        dropSelf(BlockRegister.SPECIAL_BLOCK.get());
        add(BlockRegister.ZHU_SHA_ORE.get(),createOreDrops(BlockRegister.ZHU_SHA_ORE.get(), ItemRegister.ZHU_SHA.get(),1.0f,4.0f));
        add(BlockRegister.DEEP_ZHU_SHA_ORE.get(),createOreDrops(BlockRegister.DEEP_ZHU_SHA_ORE.get(), ItemRegister.ZHU_SHA.get(),1.0f,4.0f));

    }


    //矿石战利品列表，来自BlockLootSubProvider类，修改得到此函数
    protected LootTable.Builder createOreDrops(Block block,Item item,float min,float max) {
        HolderLookup.RegistryLookup<Enchantment> enchantments = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
        return this.createSilkTouchDispatchTable(
            block,
            (LootPoolEntryContainer.Builder<?>)this.applyExplosionDecay(
                block,
                LootItem.lootTableItem(item)
                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(min, max)))
                    .apply(ApplyBonusCount.addOreBonusCount(enchantments.getOrThrow(Enchantments.FORTUNE)))));
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return BlockRegister.BLOCKS.getEntries().stream().map(Holder::value)::iterator;
    }
    //不想某方块有战利品表则使用 .noLootTable()
}
