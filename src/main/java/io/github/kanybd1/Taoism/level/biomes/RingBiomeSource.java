package io.github.kanybd1.Taoism.level.biomes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate;

import java.util.stream.Stream;

public class RingBiomeSource extends BiomeSource {

    public static final MapCodec<RingBiomeSource> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Biome.CODEC.fieldOf("inner_biome").forGetter(s -> s.innerBiome),
                    Biome.CODEC.fieldOf("middle_biome").forGetter(s -> s.middleBiome),
                    Biome.CODEC.fieldOf("outer_biome").forGetter(s -> s.outerBiome),
                    Codec.FLOAT.fieldOf("inner_radius").forGetter(s -> s.innerRadius),
                    Codec.FLOAT.fieldOf("middle_radius").forGetter(s -> s.middleRadius)
            ).apply(instance, RingBiomeSource::new)
    );

    private final Holder<Biome> innerBiome;   // 0 ~ innerRadius: 慈悲平原
    private final Holder<Biome> middleBiome;  // innerRadius ~ middleRadius: 混沌荒原
    private final Holder<Biome> outerBiome;   // > middleRadius: 血怨沼泽
    private final float innerRadius;          // 100
    private final float middleRadius;         // 200

    public RingBiomeSource(Holder<Biome> inner, Holder<Biome> middle,
                           Holder<Biome> outer, float innerR, float middleR) {
        this.innerBiome = inner;
        this.middleBiome = middle;
        this.outerBiome = outer;
        this.innerRadius = innerR;
        this.middleRadius = middleR;
    }

    @Override
    protected Stream<Holder<Biome>> collectPossibleBiomes() {
        return Stream.of(innerBiome, middleBiome, outerBiome);
    }

    @Override
    protected MapCodec<? extends BiomeSource> codec() {
        return CODEC;
    }

    @Override
    public Holder<Biome> getNoiseBiome(int quartX, int quartY, int quartZ, Climate.Sampler sampler) {
        double x = quartX * 4.0;
        double z = quartZ * 4.0;
        double dist = Math.sqrt(x * x + z * z);

        // ⭐ 硬编码绝对边界，优先级高于一切噪声计算
        if (dist > 1100.0) return outerBiome;   // 1100格外强制沼泽
        if (dist > 600.0 && dist < 1100.0)  return middleBiome;  // 600格外强制荒原
        if (dist < 600.0)  return innerBiome;

        Climate.TargetPoint point = sampler.sample(quartX, quartY, quartZ);

        // 仅在绝对边界内才进行噪声扰动
        double innerOffset = point.temperature() * 8.0;
        double outerOffset = point.humidity() * 8.0;

        double innerBound = innerRadius + innerOffset;
        double middleBound = middleRadius + outerOffset;

        if (dist <= innerBound) {
            return innerBiome;
        } else if (dist <= middleBound) {
            return middleBiome;
        } else {
            return outerBiome;
        }
    }
}