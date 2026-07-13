package io.github.kanybd1.Taoism.level.biomes;

import io.github.kanybd1.Taoism.TaoismMain;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.Carvers;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.attribute.*;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings;

import java.util.List;

public class AfterlifeBiomes {
    public static final ResourceKey<Biome> MERCY_PLAINS = ResourceKey.create(Registries.BIOME,
            Identifier.fromNamespaceAndPath(TaoismMain.MODID, "mercy_plains"));
    public static final ResourceKey<Biome> CHAOS_WASTELAND = ResourceKey.create(Registries.BIOME,
            Identifier.fromNamespaceAndPath(TaoismMain.MODID, "chaos_wasteland"));
    public static final ResourceKey<Biome> BLOOD_SWAMP = ResourceKey.create(Registries.BIOME,
            Identifier.fromNamespaceAndPath(TaoismMain.MODID, "blood_swamp"));

    public static void bootstrap(BootstrapContext<Biome> context) {
        var placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        var carvers = context.lookup(Registries.CONFIGURED_CARVER);

        // === 慈悲平原 ===
        context.register(MERCY_PLAINS, new Biome.BiomeBuilder()
                .hasPrecipitation(false)
                .temperature(0.5F).downfall(0.6F)
                .putAttributes(EnvironmentAttributeMap.builder()
                        .set(EnvironmentAttributes.FOG_COLOR, 0xE8E0D0)
                        .set(EnvironmentAttributes.SKY_COLOR, 0xF5EFE0)
                        .set(EnvironmentAttributes.WATER_FOG_COLOR, 0x6A9AB0)
                        .set(EnvironmentAttributes.SUNRISE_SUNSET_COLOR, 0xFFE8D0B0)
                        .set(EnvironmentAttributes.AMBIENT_LIGHT_COLOR, 0xFFF5EFE0)
                        .set(EnvironmentAttributes.BLOCK_LIGHT_TINT, 0xFFE8D8C0)
                        .set(EnvironmentAttributes.CLOUD_HEIGHT, 220.0F)
                        .set(EnvironmentAttributes.STAR_BRIGHTNESS, 0.3F)
                        .set(EnvironmentAttributes.BACKGROUND_MUSIC, BackgroundMusic.EMPTY)
                        .set(EnvironmentAttributes.BED_RULE, BedRule.CAN_SLEEP_WHEN_DARK)
                        .set(EnvironmentAttributes.MONSTERS_BURN, false)
                        .build())
                .specialEffects(new BiomeSpecialEffects.Builder()
                        .waterColor(0x88B4C8)
                        .grassColorOverride(0xA8B890)
                        .foliageColorOverride(0x8AA870)
                        .build())
                .mobSpawnSettings(MobSpawnSettings.EMPTY)
                // ⭐ 修正1: 使用 new Builder() 构造器
                // ⭐ 修正2: addCarver 不再需要 GenerationStep.Carving 参数
                // ⭐ 修正3: 测试阶段先用原版 CAVE，避免 ModCarvers 未注册导致崩溃
                .generationSettings(new BiomeGenerationSettings.Builder(placedFeatures, carvers)
                        .addCarver(Carvers.CAVE)
                        .build())
                .build());

        // === 混沌荒原 ===
        context.register(CHAOS_WASTELAND, new Biome.BiomeBuilder()
                .hasPrecipitation(false)
                .temperature(0.0F).downfall(0.2F)
                .putAttributes(EnvironmentAttributeMap.builder()
                        .set(EnvironmentAttributes.FOG_COLOR, 0x8A8078)
                        .set(EnvironmentAttributes.FOG_START_DISTANCE, 4.0F)
                        .set(EnvironmentAttributes.FOG_END_DISTANCE, 128.0F)
                        .set(EnvironmentAttributes.SKY_COLOR, 0x6E6860)
                        .set(EnvironmentAttributes.WATER_FOG_COLOR, 0x3A3430)
                        .set(EnvironmentAttributes.SUNRISE_SUNSET_COLOR, 0x806050)
                        .set(EnvironmentAttributes.AMBIENT_LIGHT_COLOR, 0xFF6E6860)
                        .set(EnvironmentAttributes.BLOCK_LIGHT_TINT, 0xFF908070)
                        .set(EnvironmentAttributes.CLOUD_COLOR, 0xFF6E6860)
                        .set(EnvironmentAttributes.STAR_BRIGHTNESS, 0.0F)
                        .set(EnvironmentAttributes.AMBIENT_PARTICLES, List.of(
                                new AmbientParticle(ParticleTypes.ASH, 0.003f)
                        ))
                        .set(EnvironmentAttributes.WATER_EVAPORATES, true)
                        .set(EnvironmentAttributes.CAN_PILLAGER_PATROL_SPAWN, false)
                        .build())
                .specialEffects(new BiomeSpecialEffects.Builder()
                        .waterColor(0x5A5048)
                        .grassColorOverride(0x706858)
                        .foliageColorOverride(0x5E5648)
                        .dryFoliageColorOverride(0x8A7A60)
                        .build())
                .mobSpawnSettings(MobSpawnSettings.EMPTY)
                // ⭐ 测试阶段暂时用原版 NETHER_CAVE 代替自定义雕刻器
                .generationSettings(new BiomeGenerationSettings.Builder(placedFeatures, carvers)
                        .addCarver(Carvers.NETHER_CAVE)
                        .build())
                .build());

        // === 血怨沼泽 ===
        context.register(BLOOD_SWAMP, new Biome.BiomeBuilder()
                .hasPrecipitation(false)
                .temperature(-0.5F).downfall(0.9F)
                .putAttributes(EnvironmentAttributeMap.builder()
                        .set(EnvironmentAttributes.FOG_COLOR, 0x3A1818)
                        .set(EnvironmentAttributes.FOG_START_DISTANCE, 2.0F)
                        .set(EnvironmentAttributes.FOG_END_DISTANCE, 96.0F)
                        .set(EnvironmentAttributes.SKY_COLOR, 0x1A0808)
                        .set(EnvironmentAttributes.WATER_FOG_COLOR, 0x400810)
                        .set(EnvironmentAttributes.SUNRISE_SUNSET_COLOR, 0x601010)
                        .set(EnvironmentAttributes.AMBIENT_LIGHT_COLOR, 0xFF2A1018)
                        .set(EnvironmentAttributes.BLOCK_LIGHT_TINT, 0xFF6A2020)
                        .set(EnvironmentAttributes.NIGHT_VISION_COLOR, 0xFF3A1818)
                        .set(EnvironmentAttributes.STAR_BRIGHTNESS, 0.05F)
                        .set(EnvironmentAttributes.AMBIENT_PARTICLES, List.of(
                                new AmbientParticle(ParticleTypes.SOUL_FIRE_FLAME, 0.005f)
                        ))
                        .set(EnvironmentAttributes.BED_RULE, BedRule.EXPLODES)
                        .set(EnvironmentAttributes.RESPAWN_ANCHOR_WORKS, true)
                        .set(EnvironmentAttributes.MONSTERS_BURN, false)
                        .set(EnvironmentAttributes.SNOW_GOLEM_MELTS, true)
                        .build())
                .specialEffects(new BiomeSpecialEffects.Builder()
                        .waterColor(0x6A1020)
                        .grassColorOverride(0x3A2020)
                        .foliageColorOverride(0x2A1018)
                        .dryFoliageColorOverride(0x4A2828)
                        .grassColorModifier(BiomeSpecialEffects.GrassColorModifier.SWAMP)
                        .build())
                .mobSpawnSettings(MobSpawnSettings.EMPTY)
                .generationSettings(new BiomeGenerationSettings.Builder(placedFeatures, carvers)
                        .addCarver(Carvers.CAVE_EXTRA_UNDERGROUND)
                        .build())
                .build());
    }
}