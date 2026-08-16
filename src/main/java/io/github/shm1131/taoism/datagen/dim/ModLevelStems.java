package io.github.shm1131.taoism.datagen.dim;

import com.mojang.datafixers.util.Pair;
import io.github.shm1131.taoism.TaoismMain;
import io.github.shm1131.taoism.datagen.biomes.AfterlifeBiomes;
import io.github.shm1131.taoism.datagen.biomes.RingBiomeSource;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.MultiNoiseBiomeSource;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;

import java.util.List;

public class ModLevelStems {

  public static final ResourceKey<Level> TAOISM_REALM_KEY =
      ResourceKey.create(Registries.DIMENSION,
          Identifier.fromNamespaceAndPath(TaoismMain.MODID, "taoism_realm"));

  public static final ResourceKey<LevelStem> TAOISM_REALM_STEM_KEY =
      ResourceKey.create(Registries.LEVEL_STEM,
          Identifier.fromNamespaceAndPath(TaoismMain.MODID, "taoism_realm"));

  public static void bootstrap(BootstrapContext<LevelStem> context) {
    var dimensionTypes = context.lookup(Registries.DIMENSION_TYPE);
    var noiseSettings = context.lookup(Registries.NOISE_SETTINGS);
    var biomes = context.lookup(Registries.BIOME);

    var dimType = dimensionTypes.getOrThrow(ModDimensionTypes.EMPTY_DIMENSION_KEY);
    var customNoise = noiseSettings.getOrThrow(ModNoiseGeneratorSettings.FLAT_RIVER_TERRAIN);

    RingBiomeSource biomeSource = new RingBiomeSource(
        biomes.getOrThrow(AfterlifeBiomes.MERCY_PLAINS),     // 内圈：慈悲平原
        biomes.getOrThrow(AfterlifeBiomes.CHAOS_WASTELAND),  // 中圈：混沌荒原
        biomes.getOrThrow(AfterlifeBiomes.BLOOD_SWAMP),      // 外圈：血怨沼泽
        500.0F,  // 内圈半径（方块）
        2000.0F   // 中圈半径（方块）
    );

    context.register(TAOISM_REALM_STEM_KEY, new LevelStem(
        dimType,
        new NoiseBasedChunkGenerator(biomeSource, customNoise)
    ));
  }

  private static MultiNoiseBiomeSource createAfterlifeBiomeSource(HolderGetter<Biome> biomes) {
    List<Pair<Climate.ParameterPoint, Holder<Biome>>> entries = List.of(
        Pair.of(
            Climate.parameters(0.5F, 0.3F, -0.5F, 0.0F, 0.0F, 0.0F, 0.0F),
            biomes.getOrThrow(AfterlifeBiomes.MERCY_PLAINS)
        ),
        Pair.of(
            Climate.parameters(-0.5F, -0.5F, 0.5F, 0.0F, 0.0F, 0.0F, 0.0F),
            biomes.getOrThrow(AfterlifeBiomes.CHAOS_WASTELAND)
        ),
        Pair.of(
            Climate.parameters(0.0F, 0.8F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F),
            biomes.getOrThrow(AfterlifeBiomes.BLOOD_SWAMP)
        )
    );

    return MultiNoiseBiomeSource.createFromList(new Climate.ParameterList<>(entries));
  }
}
