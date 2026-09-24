package io.github.shm1131.taoism.datagen;

import io.github.shm1131.taoism.TaoismMain;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ItemTagsProvider;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends ItemTagsProvider {
    public ModItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, TaoismMain.MODID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {

    }
}
