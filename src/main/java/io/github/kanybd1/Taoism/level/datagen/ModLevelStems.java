package io.github.kanybd1.Taoism.level.datagen;

import com.mojang.datafixers.util.Pair;
import io.github.kanybd1.Taoism.TaoismMain;
import io.github.kanybd1.Taoism.level.biomes.AfterlifeBiomes;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.MultiNoiseBiomeSource;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;

import java.util.List;

public class ModLevelStems {

    public static final ResourceKey<Level> TAOISM_REALM_KEY =
            ResourceKey.create(Registries.DIMENSION,
                    Identifier.fromNamespaceAndPath(TaoismMain.MODID, "taoism_realm"));

    public static final ResourceKey<LevelStem> TAOISM_REALM_STEM_KEY =
            ResourceKey.create(Registries.LEVEL_STEM,
                    Identifier.fromNamespaceAndPath(TaoismMain.MODID, "taoism_realm"));

    private static final ResourceKey<NoiseGeneratorSettings> TEST_GRASS_NOISE_KEY =
            ResourceKey.create(Registries.NOISE_SETTINGS,
                    Identifier.fromNamespaceAndPath(TaoismMain.MODID, "test_grass_terrain"));

    public static void bootstrap(BootstrapContext<LevelStem> context) {
        var dimensionTypes = context.lookup(Registries.DIMENSION_TYPE);
        var noiseSettings = context.lookup(Registries.NOISE_SETTINGS);
        var biomes = context.lookup(Registries.BIOME);

        // ✅ getOrThrow 必须传 ResourceKey
        var dimType = dimensionTypes.getOrThrow(ModDimensionTypes.EMPTY_DIMENSION_KEY);
        Holder<NoiseGeneratorSettings> testNoiseHolder = noiseSettings.getOrThrow(NoiseGeneratorSettings.OVERWORLD);

        // ✅ 使用 createFromList + Climate.ParameterList 构造器
        MultiNoiseBiomeSource biomeSource = createAfterlifeBiomeSource(biomes);

        // ✅ 第二个参数必须是 Holder<NoiseGeneratorSettings>
        context.register(TAOISM_REALM_STEM_KEY, new LevelStem(
                dimType,
                new NoiseBasedChunkGenerator(biomeSource, testNoiseHolder)
        ));
    }

    private static MultiNoiseBiomeSource createAfterlifeBiomeSource(HolderGetter<Biome> biomes) {
        List<Pair<Climate.ParameterPoint, Holder<Biome>>> entries = List.of(
                // 慈悲平原：温和、适中湿度、地表
                Pair.of(
                        Climate.parameters(0.0F, 0.2F, -0.3F, 0.0F, 0.0F, 0.0F, 0.0F),
                        biomes.getOrThrow(AfterlifeBiomes.MERCY_PLAINS)
                ),
                // 混沌荒原：寒冷、极干、高大陆性
                Pair.of(
                        Climate.parameters(-0.8F, -0.9F, 0.8F, 0.0F, 0.0F, 0.0F, 0.0F),
                        biomes.getOrThrow(AfterlifeBiomes.CHAOS_WASTELAND)
                ),
                // 血怨沼泽：偏冷、极湿、低大陆性
                Pair.of(
                        Climate.parameters(-0.4F, 0.9F, -0.7F, 0.0F, 0.0F, 0.0F, 0.0F),
                        biomes.getOrThrow(AfterlifeBiomes.BLOOD_SWAMP)
                )
        );

        // ⭐ 关键修复：
        // 1. 用 new Climate.ParameterList<>(entries) 包装列表
        // 2. 用 createFromList() 传入，对应源码中 Either.left 分支
        return MultiNoiseBiomeSource.createFromList(new Climate.ParameterList<>(entries));
    }
}