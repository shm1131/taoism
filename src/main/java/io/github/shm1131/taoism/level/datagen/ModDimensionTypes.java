package io.github.shm1131.taoism.level.datagen;


import io.github.shm1131.taoism.TaoismMain;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TimelineTags;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.attribute.BedRule;
import net.minecraft.world.attribute.EnvironmentAttributeMap;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.clock.WorldClock;
import net.minecraft.world.level.CardinalLighting;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.timeline.Timeline;

import java.util.Optional;

public class ModDimensionTypes {
    // 定义你的维度 ResourceKey
    public static final ResourceKey<DimensionType> EMPTY_DIMENSION_KEY =
            ResourceKey.create(Registries.DIMENSION_TYPE, Identifier.fromNamespaceAndPath(TaoismMain.MODID, "empty_dimension"));

    public static void bootstrap(BootstrapContext<DimensionType> context) {

        HolderGetter<Timeline> timelines = context.lookup(Registries.TIMELINE);
        HolderGetter<WorldClock> clocks = context.lookup(Registries.WORLD_CLOCK);

        // 最小化环境属性 - 空维度不需要花哨的效果
        EnvironmentAttributeMap emptyAttributes = EnvironmentAttributeMap.builder()
                .set(EnvironmentAttributes.FOG_COLOR, 0x000000)       // 黑色雾
                .set(EnvironmentAttributes.SKY_COLOR, 0x000000)      // 黑色天空
                .set(EnvironmentAttributes.AMBIENT_LIGHT_COLOR, 0x000000)
                .set(EnvironmentAttributes.BED_RULE, BedRule.EXPLODES) // 床爆炸，防止玩家跳过夜晚
                .set(EnvironmentAttributes.RESPAWN_ANCHOR_WORKS, false)
                .build();

        context.register(EMPTY_DIMENSION_KEY, new DimensionType(
                false,                          // ultrawarm: 不是地狱般炎热
                true,                           // natural: 允许自然生物生成（可改false）
                false,                          // piglinSafe: 猪灵会颤抖
                false,                          // bedWorks: 床不可用（配合BedRule.EXPLODES）
                1.0F,                           // coordinateScale: 坐标缩放比
                0,                              // minY: 最低Y坐标
                256,                            // height: 世界高度
                256,                            // logicalHeight: 逻辑高度（建筑上限）
                BlockTags.INFINIBURN_OVERWORLD, // infiniburn: 基岩燃烧标签
                0.0F,                           // ambientLight: 环境光亮度
                new DimensionType.MonsterSettings(ConstantInt.of(7), 0), // 怪物生成设置
                DimensionType.Skybox.NONE,                    // 无天空盒渲染
                CardinalLighting.Type.DEFAULT,  // 默认光照
                emptyAttributes,                // 环境属性
                timelines.getOrThrow(TimelineTags.IN_OVERWORLD), // 时间线（先用原版）
                Optional.empty()                // 无自定义时钟
        ));
    }
}