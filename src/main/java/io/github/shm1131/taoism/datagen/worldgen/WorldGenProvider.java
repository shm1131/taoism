package io.github.shm1131.taoism.datagen.worldgen;

import io.github.shm1131.taoism.TaoismMain;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class WorldGenProvider extends DatapackBuiltinEntriesProvider {

    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
        .add(Registries.CONFIGURED_FEATURE, ModConfiguredFeature::bootstrap)
        .add(Registries.PLACED_FEATURE, ModPlacedFeature::bootstrap)
        .add(NeoForgeRegistries.Keys.BIOME_MODIFIERS,ModBiomeModifiers::bootstrap);

    public WorldGenProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output,registries,BUILDER, Set.of(TaoismMain.MODID));
    }

    @Override
    public String getName() {
        return "Taoism World Gen Provider";
    }
}
