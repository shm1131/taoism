package io.github.shm1131.taoism.datagen.worldgen;

import io.github.shm1131.taoism.TaoismMain;
import io.github.shm1131.taoism.init.BlockRegister;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

public class ModConfiguredFeature {

    public static final ResourceKey<ConfiguredFeature<?,?>> OVERWORLD_CINNABAR_ORE_KEY = registerKey("overworld_cinnabar_ore");


    public static void bootstrap(BootstrapContext<ConfiguredFeature<?,?>> context) {

        RuleTest stoneReplaceables = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
        RuleTest deepslateReplaceables = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);

        FeatureUtils.register(context, OVERWORLD_CINNABAR_ORE_KEY, Feature.ORE,new OreConfiguration(stoneReplaceables, BlockRegister.CINNABAR_ORE.get().defaultBlockState(),8));
    }


    private static ResourceKey<ConfiguredFeature<?,?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, Identifier.fromNamespaceAndPath(TaoismMain.MODID,name));
    }

}
