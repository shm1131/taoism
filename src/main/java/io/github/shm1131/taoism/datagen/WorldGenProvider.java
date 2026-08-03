package io.github.shm1131.taoism.datagen;

import io.github.shm1131.taoism.TaoismMain;
import io.github.shm1131.taoism.datagen.biomes.AfterlifeBiomes;
import io.github.shm1131.taoism.datagen.dim.ModDimensionTypes;
import io.github.shm1131.taoism.datagen.dim.ModLevelStems;
import io.github.shm1131.taoism.datagen.dim.ModNoiseGeneratorSettings;
import io.github.shm1131.taoism.datagen.worldgen.ModBiomeModifiers;
import io.github.shm1131.taoism.datagen.worldgen.ModConfiguredFeature;
import io.github.shm1131.taoism.datagen.worldgen.ModPlacedFeature;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class WorldGenProvider extends DatapackBuiltinEntriesProvider {

    // 合并所有需要动态注册的 Registry
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
        // === 原 WorldGen 部分 ===
        .add(Registries.CONFIGURED_FEATURE, ModConfiguredFeature::bootstrap)
        .add(Registries.PLACED_FEATURE, ModPlacedFeature::bootstrap)
        .add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, ModBiomeModifiers::bootstrap)
        // === 原 DataGenProvider 部分 ===
        .add(Registries.DIMENSION_TYPE, ModDimensionTypes::bootstrap)
        .add(Registries.BIOME, AfterlifeBiomes::bootstrap)
        .add(Registries.NOISE_SETTINGS, ModNoiseGeneratorSettings::bootstrap)
        .add(Registries.LEVEL_STEM, ModLevelStems::bootstrap);

    public WorldGenProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(TaoismMain.MODID));
    }

    @Override
    public String getName() {
        return "Taoism World Gen & Dimension Provider";
    }
}
