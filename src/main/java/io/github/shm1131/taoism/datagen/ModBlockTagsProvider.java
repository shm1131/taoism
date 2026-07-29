package io.github.shm1131.taoism.datagen;

import io.github.shm1131.taoism.TaoismMain;
import io.github.shm1131.taoism.init.BlockRegister;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagsProvider extends BlockTagsProvider {
    public ModBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider){
        super(output,lookupProvider, TaoismMain.MODID);
    }

    @Override
    public void addTags(HolderLookup.Provider registries){
        tag(BlockTags.MINEABLE_WITH_PICKAXE)
            .add(BlockRegister.SPECIAL_BLOCK.get())
            .add(BlockRegister.ZHU_SHA_ORE.get())
            .add(BlockRegister.DEEP_ZHU_SHA_ORE.get())
            .add(BlockRegister.SIMPLE_BLOCKS.get("yun_mu_ore").get())
            .add(BlockRegister.SIMPLE_BLOCKS.get("yin_ore").get())
            .add(BlockRegister.SIMPLE_BLOCKS.get("deep_yin_ore").get());

        tag(BlockTags.NEEDS_STONE_TOOL)
            .add(BlockRegister.ZHU_SHA_ORE.get())
            .add(BlockRegister.DEEP_ZHU_SHA_ORE.get());

        tag(BlockTags.NEEDS_IRON_TOOL)
            .add(BlockRegister.SIMPLE_BLOCKS.get("yin_ore").get())
            .add(BlockRegister.SIMPLE_BLOCKS.get("deep_yin_ore").get());
    }
}
