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
import net.neoforged.neoforge.registries.DeferredBlock;

import java.util.List;
import java.util.function.BiFunction;

public class ModConfiguredFeature {

    public static final ResourceKey<ConfiguredFeature<?, ?>> URI_ZHU_SHA_ORE_KEY = registerKey("uri_cinnabar_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> TRI_ZHU_SHA_ORE_KEY = registerKey("tri_cinnabar_ore");

    public static final ResourceKey<ConfiguredFeature<?, ?>> TRI_YUN_MU_ORE_KEY = registerKey("tri_yun_mu_ore");

    public static final ResourceKey<ConfiguredFeature<?, ?>> URI_YIN_ORE_KEY = registerKey("uri_yin_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> TRI_YIN_ORE_KEY = registerKey("tri_yin_ore");

    public static final ResourceKey<ConfiguredFeature<?, ?>> URI_QIAN_ORE_KEY = registerKey("uri_qian_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>>  TRI_QIAN_ORE_KEY = registerKey("tri_qian_ore");

    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        RuleTest stoneReplaceables = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
        RuleTest deepslateReplaceables = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);
        //用来合并主世界的石头与深板岩规则
        BiFunction<DeferredBlock<?>,DeferredBlock<?>,List<OreConfiguration.TargetBlockState>> overworldOreRule =
            (stoneOre, deepslateOre)-> List.of(
                OreConfiguration.target(stoneReplaceables, stoneOre.get().defaultBlockState()),
                OreConfiguration.target(deepslateReplaceables, deepslateOre.get().defaultBlockState())
            );

        List<OreConfiguration.TargetBlockState> ZhuShaOreRule = overworldOreRule.apply(BlockRegister.ZHU_SHA_ORE,BlockRegister.DEEP_ZHU_SHA_ORE);
        List<OreConfiguration.TargetBlockState> YinOreRule = overworldOreRule.apply(BlockRegister.SIMPLE_BLOCKS.get("yin_ore"),BlockRegister.SIMPLE_BLOCKS.get("deep_yin_ore"));
        List<OreConfiguration.TargetBlockState> YunMuOreRule = overworldOreRule.apply(BlockRegister.SIMPLE_BLOCKS.get("yun_mu_ore"),BlockRegister.SIMPLE_BLOCKS.get("yun_mu_ore"));
        List<OreConfiguration.TargetBlockState> QianOreRule =  overworldOreRule.apply(BlockRegister.SIMPLE_BLOCKS.get("qian_ore"),BlockRegister.SIMPLE_BLOCKS.get("qian_ore"));

        FeatureUtils.register(context, URI_ZHU_SHA_ORE_KEY, Feature.ORE,
            new OreConfiguration(ZhuShaOreRule, 6,0.0f));
        FeatureUtils.register(context, TRI_ZHU_SHA_ORE_KEY, Feature.ORE,
            new OreConfiguration(ZhuShaOreRule, 8, 0.0f));

        FeatureUtils.register(context, TRI_YUN_MU_ORE_KEY,Feature.ORE,
            new OreConfiguration(YunMuOreRule,13,0.1f));

        FeatureUtils.register(context, URI_YIN_ORE_KEY,Feature.ORE,
            new OreConfiguration(YinOreRule,7,0.0f));
        FeatureUtils.register(context, TRI_YIN_ORE_KEY,Feature.ORE,
            new OreConfiguration(YinOreRule,8,0.1f));

        FeatureUtils.register(context, URI_QIAN_ORE_KEY,Feature.ORE,
            new OreConfiguration(QianOreRule,6,0.0f));
        FeatureUtils.register(context, TRI_QIAN_ORE_KEY,Feature.ORE,
            new OreConfiguration(QianOreRule,8,0.0f));
    }

    private static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, Identifier.fromNamespaceAndPath(TaoismMain.MODID, name));
    }
}
