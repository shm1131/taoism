package io.github.shm1131.taoism.datagen.worldgen;

import io.github.shm1131.taoism.TaoismMain;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;

public class ModPlacedFeature {

    public static final ResourceKey<PlacedFeature> UNI_ZHU_SHA_ORE_KEY = createKey("uni_zhu_sha_ore_placed");
    public static final ResourceKey<PlacedFeature> TRI_ZHU_SHA_ORE_KEY = createKey("tri_zhu_sha_ore_placed");

    public static final ResourceKey<PlacedFeature> TRI_YUN_MU_ORE_KEY = createKey("tri_yun_mu_ore_placed");

    public static final ResourceKey<PlacedFeature> UNI_YIN_ORE_KEY = createKey("uni_yin_ore_placed");
    public static final ResourceKey<PlacedFeature> TRI_YIN_ORE_KEY = createKey("tri_yin_ore_placed");

    public static final ResourceKey<PlacedFeature> UNI_QIAN_ORE_KEY = createKey("uni_qian_ore_placed");
    public static final ResourceKey<PlacedFeature> TRI_QIAN_ORE_KEY = createKey("tri_qian_ore_placed");

    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?,?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

        PlacementUtils.register(context, UNI_ZHU_SHA_ORE_KEY,configuredFeatures.getOrThrow(ModConfiguredFeature.URI_ZHU_SHA_ORE_KEY),
            commonOrePlacement(6, HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(-16), VerticalAnchor.absolute(64))));
        PlacementUtils.register(context, TRI_ZHU_SHA_ORE_KEY, configuredFeatures.getOrThrow(ModConfiguredFeature.TRI_ZHU_SHA_ORE_KEY),
            commonOrePlacement(8, HeightRangePlacement.triangle(VerticalAnchor.absolute(-16), VerticalAnchor.absolute(112))));

        PlacementUtils.register(context,TRI_YUN_MU_ORE_KEY,configuredFeatures.getOrThrow(ModConfiguredFeature.TRI_YUN_MU_ORE_KEY),
            commonOrePlacement(3,HeightRangePlacement.triangle(VerticalAnchor.absolute(-10), VerticalAnchor.absolute(70))));

        PlacementUtils.register(context, UNI_YIN_ORE_KEY,configuredFeatures.getOrThrow(ModConfiguredFeature.URI_YIN_ORE_KEY),
            commonOrePlacement(5,HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(-32), VerticalAnchor.absolute(96))));
        PlacementUtils.register(context, TRI_YIN_ORE_KEY,configuredFeatures.getOrThrow(ModConfiguredFeature.TRI_YIN_ORE_KEY),
            commonOrePlacement(8,HeightRangePlacement.triangle(VerticalAnchor.absolute(-32), VerticalAnchor.absolute(128))));

        PlacementUtils.register(context,UNI_QIAN_ORE_KEY,configuredFeatures.getOrThrow(ModConfiguredFeature.URI_QIAN_ORE_KEY),
            commonOrePlacement(6,HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(-48), VerticalAnchor.absolute(24))));
        PlacementUtils.register(context,TRI_QIAN_ORE_KEY,configuredFeatures.getOrThrow(ModConfiguredFeature.TRI_QIAN_ORE_KEY),
            commonOrePlacement(8,HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(-48), VerticalAnchor.absolute(96))));
    }

    private static List<PlacementModifier> orePlacement(PlacementModifier pCountPlacement, PlacementModifier pHeightRange) {
        return List.of(pCountPlacement, InSquarePlacement.spread(), pHeightRange, BiomeFilter.biome());
    }

    private static List<PlacementModifier> commonOrePlacement(int pCount, PlacementModifier pHeightRange) {
        return orePlacement(CountPlacement.of(pCount), pHeightRange);
    }

    private static List<PlacementModifier> rareOrePlacement(int pChance, PlacementModifier pHeightRange) {
        return orePlacement(RarityFilter.onAverageOnceEvery(pChance), pHeightRange);
    }

    private static ResourceKey<PlacedFeature> createKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, Identifier.fromNamespaceAndPath(TaoismMain.MODID,name));
    }

}
