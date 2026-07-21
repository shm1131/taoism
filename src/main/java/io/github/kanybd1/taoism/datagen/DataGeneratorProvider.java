package io.github.kanybd1.taoism.datagen;

import io.github.kanybd1.taoism.TaoismMain;
import io.github.kanybd1.taoism.datagen.world.biomes.AfterlifeBiomes;
import io.github.kanybd1.taoism.datagen.world.dim.ModDimensionTypes;
import io.github.kanybd1.taoism.datagen.world.dim.ModLevelStems;
import io.github.kanybd1.taoism.datagen.world.dim.ModNoiseGeneratorSettings;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class DataGeneratorProvider extends DatapackBuiltinEntriesProvider {

  public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
      .add(Registries.DIMENSION_TYPE, ModDimensionTypes::bootstrap)
      .add(Registries.BIOME, AfterlifeBiomes::bootstrap)
      .add(Registries.NOISE_SETTINGS, ModNoiseGeneratorSettings::bootstrap)
      .add(Registries.LEVEL_STEM, ModLevelStems::bootstrap);

  public DataGeneratorProvider(PackOutput output, CompletableFuture<net.minecraft.core.HolderLookup.Provider> registries) {
    super(output, registries, BUILDER, Set.of(TaoismMain.MODID));
  }
}