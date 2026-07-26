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

import java.util.List;

public class ModConfiguredFeature {
    //埋藏
    public static final ResourceKey<ConfiguredFeature<?, ?>> OVERWORLD_CINNABAR_ORE_KEY = registerKey("overworld_cinnabar_ore");
    //ADDED：裸漏
    public static final ResourceKey<ConfiguredFeature<?, ?>> EXPOSED_CINNABAR_ORE_KEY = registerKey("exposed_cinnabar_ore");

    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        RuleTest stoneReplaceables = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
        RuleTest deepslateReplaceables = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);

        //CHANGED：列表合并深板岩和石头条件
        List<OreConfiguration.TargetBlockState> targets = List.of(
            OreConfiguration.target(stoneReplaceables, BlockRegister.CINNABAR_ORE.get().defaultBlockState()),
            OreConfiguration.target(deepslateReplaceables, BlockRegister.CINNABAR_ORE.get().defaultBlockState())
        );

        FeatureUtils.register(context, OVERWORLD_CINNABAR_ORE_KEY, Feature.ORE,
            new OreConfiguration(targets, 8));

        //ADDED：表面生成被丢弃概率0.0f，越高越不容易被发现
        FeatureUtils.register(context, EXPOSED_CINNABAR_ORE_KEY, Feature.SCATTERED_ORE,
            new OreConfiguration(targets, 4, 0.0f));
    }

    private static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, Identifier.fromNamespaceAndPath(TaoismMain.MODID, name));
    }
}
