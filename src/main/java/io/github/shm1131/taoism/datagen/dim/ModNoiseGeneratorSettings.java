package io.github.shm1131.taoism.datagen.dim;

import com.mojang.serialization.Lifecycle;
import io.github.shm1131.taoism.TaoismMain;
import io.github.shm1131.taoism.datagen.biomes.AfterlifeBiomes;
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

    DensityFunction baseNoise = DensityFunctions.noise(
        noises.getOrThrow(Noises.CONTINENTALNESS), 0.5, 0.5
    );
    DensityFunction flatFactor = DensityFunctions.constant(0.3);
    DensityFunction baseTerrain = DensityFunctions.mul(baseNoise, flatFactor);

    DensityFunction yGradient = DensityFunctions.yClampedGradient(-64, 256, 1.5, -1.5);

    DensityFunction finalDensity = DensityFunctions.add(yGradient, baseTerrain).squeeze();

    NoiseRouter router = new NoiseRouter(
        DensityFunctions.zero(),
        DensityFunctions.zero(),
        DensityFunctions.zero(),
        DensityFunctions.zero(),
        baseTerrain,
        DensityFunctions.zero(),
        DensityFunctions.zero(),
        DensityFunctions.zero(),
        DensityFunctions.zero(),
        DensityFunctions.zero(),
        DensityFunctions.zero(),
        finalDensity,
        DensityFunctions.zero(),
        DensityFunctions.zero(),
        DensityFunctions.zero()
    );

    context.register(FLAT_RIVER_TERRAIN, new NoiseGeneratorSettings(
        NoiseSettings.create(-64, 384, 1, 2),
        Blocks.STONE.defaultBlockState(),
        Blocks.WATER.defaultBlockState(),
        router,
        makeSurfaceRules(),
        List.of(),
        63,
        false,
        false,
        false,
        false
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
