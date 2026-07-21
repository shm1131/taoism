package io.github.kanybd1.taoism.datagen.world.dim;

import com.mojang.serialization.Lifecycle;
import io.github.kanybd1.taoism.TaoismMain;
import io.github.kanybd1.taoism.datagen.world.biomes.AfterlifeBiomes;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.*;

import java.util.List;

public class ModNoiseGeneratorSettings {
    public static final ResourceKey<NoiseGeneratorSettings> FLAT_RIVER_TERRAIN =
        ResourceKey.create(Registries.NOISE_SETTINGS,
            Identifier.fromNamespaceAndPath(TaoismMain.MODID, "flat_river_terrain"));

    public static void bootstrap(BootstrapContext<NoiseGeneratorSettings> context) {
        var noises = context.lookup(Registries.NOISE);

        // === 1. 构建平坦地形密度函数（无河流）===
        // ⭐ add/mul/squeeze 全部使用 DensityFunctions 静态方法
        DensityFunction baseNoise = DensityFunctions.noise(
            noises.getOrThrow(Noises.CONTINENTALNESS), 0.5, 0.5
        );
        DensityFunction flatFactor = DensityFunctions.constant(0.3);
        DensityFunction baseTerrain = DensityFunctions.mul(baseNoise, flatFactor);

        // Y轴梯度：y=63时值为0，上方为负（空气），下方为正（固体）
        DensityFunction yGradient = DensityFunctions.yClampedGradient(-64, 256, 1.5, -1.5);

        // 最终密度 = Y梯度 + 平坦地形扰动，再 squeeze 平滑
        DensityFunction finalDensity = DensityFunctions.add(yGradient, baseTerrain).squeeze();

        // === 2. 构建 NoiseRouter ===
        NoiseRouter router = new NoiseRouter(
            DensityFunctions.zero(), // barrier
            DensityFunctions.zero(), // fluidLevelFloodedness
            DensityFunctions.zero(), // fluidLevelSpread
            DensityFunctions.zero(), // lava
            baseTerrain,             // temperature
            DensityFunctions.zero(), // vegetation
            DensityFunctions.zero(), // continents
            DensityFunctions.zero(), // erosion
            DensityFunctions.zero(), // depth
            DensityFunctions.zero(), // ridges
            DensityFunctions.zero(), // initialDensityWithoutJaggedness
            finalDensity,            // ⭐ 最终密度
            DensityFunctions.zero(), // veinToggle
            DensityFunctions.zero(), // veinRidged
            DensityFunctions.zero()  // veinGap
        );

        // === 3. 注册 NoiseGeneratorSettings（11个参数，精确对齐源码）===
        context.register(FLAT_RIVER_TERRAIN, new NoiseGeneratorSettings(
            NoiseSettings.create(-64, 384, 1, 2), // noiseSettings
            Blocks.STONE.defaultBlockState(),      // defaultBlock
            Blocks.WATER.defaultBlockState(),      // defaultFluid
            router,                                // noiseRouter
            makeSurfaceRules(),                    // surfaceRule
            List.of(),                             // spawnTarget
            63,                                    // seaLevel
            false,                                 // disableMobGeneration
            false,                                 // aquifersEnabled
            false,                                 // oreVeinsEnabled
            false                                  // useLegacyRandomSource
        ), Lifecycle.stable());
    }

    private static SurfaceRules.RuleSource makeSurfaceRules() {
        SurfaceRules.RuleSource mercyPlains = SurfaceRules.ifTrue(
            SurfaceRules.isBiome(AfterlifeBiomes.MERCY_PLAINS),
            SurfaceRules.sequence(
                SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR,
                    SurfaceRules.state(Blocks.GRASS_BLOCK.defaultBlockState())),
                SurfaceRules.ifTrue(SurfaceRules.UNDER_FLOOR,
                    SurfaceRules.state(Blocks.DIRT.defaultBlockState()))
            )
        );

        SurfaceRules.RuleSource chaosWastelands = SurfaceRules.ifTrue(
            SurfaceRules.isBiome(AfterlifeBiomes.CHAOS_WASTELAND),
            SurfaceRules.sequence(
                SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR,
                    SurfaceRules.state(Blocks.GRAVEL.defaultBlockState())),
                SurfaceRules.ifTrue(SurfaceRules.UNDER_FLOOR,
                    SurfaceRules.state(Blocks.STONE.defaultBlockState()))
            )
        );

        SurfaceRules.RuleSource bloodSwamp = SurfaceRules.ifTrue(
            SurfaceRules.isBiome(AfterlifeBiomes.BLOOD_SWAMP),
            SurfaceRules.sequence(
                SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR,
                    SurfaceRules.state(Blocks.MYCELIUM.defaultBlockState())),
                SurfaceRules.ifTrue(SurfaceRules.UNDER_FLOOR,
                    SurfaceRules.state(Blocks.MUD.defaultBlockState()))
            )
        );

        return SurfaceRules.sequence(
            mercyPlains,
            chaosWastelands,
            bloodSwamp,
            SurfaceRules.state(Blocks.STONE.defaultBlockState())
        );
    }
}
