package io.github.kanybd1.Taoism.level;

import io.github.kanybd1.Taoism.TaoismMain;
import io.github.kanybd1.Taoism.level.biomes.AfterlifeBiomes;
import io.github.kanybd1.Taoism.level.datagen.ModDimensionTypes;
import io.github.kanybd1.Taoism.level.datagen.ModLevelStems;
import io.github.kanybd1.Taoism.level.datagen.ModNoiseGeneratorSettings;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ModWorldGenProvider extends DatapackBuiltinEntriesProvider {

    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.DIMENSION_TYPE, ModDimensionTypes::bootstrap)
            .add(Registries.BIOME, AfterlifeBiomes::bootstrap)
            .add(Registries.NOISE_SETTINGS, ModNoiseGeneratorSettings::bootstrap)
            .add(Registries.LEVEL_STEM, ModLevelStems::bootstrap);

    public ModWorldGenProvider(PackOutput output, CompletableFuture<net.minecraft.core.HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(TaoismMain.MODID));
    }
}