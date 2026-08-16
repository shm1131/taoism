package io.github.shm1131.taoism.datagen.worldgen;

import io.github.shm1131.taoism.TaoismMain;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class ModBiomeModifiers {
    public static final ResourceKey<BiomeModifier> ADD_ZHU_SHA_ORE = registerKey("add_zhu_sha_ore");
    public static final ResourceKey<BiomeModifier> ADD_YIN_ORE = registerKey("add_yin_ore");
    public static final ResourceKey<BiomeModifier> ADD_YUN_MU_ORE = registerKey("add_yun_mu_ore");
    public static final ResourceKey<BiomeModifier> ADD_QIAN_ORE = registerKey("add_qian_ore");

    public static void bootstrap(BootstrapContext<BiomeModifier> context){
        var placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        var biomes = context.lookup(Registries.BIOME);

        context.register(ADD_ZHU_SHA_ORE, new BiomeModifiers.AddFeaturesBiomeModifier(
            biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
            HolderSet.direct(
                placedFeatures.getOrThrow(ModPlacedFeature.UNI_ZHU_SHA_ORE_KEY),
                placedFeatures.getOrThrow(ModPlacedFeature.TRI_ZHU_SHA_ORE_KEY)
            ), GenerationStep.Decoration.UNDERGROUND_ORES));

        context.register(ADD_YUN_MU_ORE, new BiomeModifiers.AddFeaturesBiomeModifier(
            biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
            HolderSet.direct(
                placedFeatures.getOrThrow(ModPlacedFeature.TRI_YUN_MU_ORE_KEY)
            ), GenerationStep.Decoration.UNDERGROUND_ORES));

        context.register(ADD_YIN_ORE, new BiomeModifiers.AddFeaturesBiomeModifier(
            biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
            HolderSet.direct(
                placedFeatures.getOrThrow(ModPlacedFeature.UNI_YIN_ORE_KEY),
                placedFeatures.getOrThrow(ModPlacedFeature.TRI_YIN_ORE_KEY)
            ), GenerationStep.Decoration.UNDERGROUND_ORES));

        context.register(ADD_QIAN_ORE, new BiomeModifiers.AddFeaturesBiomeModifier(
            biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
            HolderSet.direct(
                placedFeatures.getOrThrow(ModPlacedFeature.UNI_QIAN_ORE_KEY),
                placedFeatures.getOrThrow(ModPlacedFeature.TRI_QIAN_ORE_KEY)
            ), GenerationStep.Decoration.UNDERGROUND_ORES));

    }

    private static ResourceKey<BiomeModifier> registerKey(String name){
        return ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, Identifier.fromNamespaceAndPath(TaoismMain.MODID,name));
    }
}
