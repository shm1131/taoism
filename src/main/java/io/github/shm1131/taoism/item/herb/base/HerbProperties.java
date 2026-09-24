//ADDED:新建类PillProperties

package io.github.shm1131.taoism.item.herb.base;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record HerbProperties(
    Flavor flavor,
    Nature nature,
    float toxicity,
    float potency
) {
    public static final Codec<HerbProperties> CODEC = RecordCodecBuilder.create(inst -> inst.group(
        Flavor.CODEC.fieldOf("flavor").forGetter(HerbProperties::flavor),
        Nature.CODEC.fieldOf("nature").forGetter(HerbProperties::nature),
        Codec.FLOAT.fieldOf("toxicity").forGetter(HerbProperties::toxicity),
        Codec.FLOAT.fieldOf("potency").forGetter(HerbProperties::potency)
    ).apply(inst, HerbProperties::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, HerbProperties> STREAM_CODEC =
        StreamCodec.composite(
            Flavor.STREAM_CODEC, HerbProperties::flavor,
            Nature.STREAM_CODEC, HerbProperties::nature,
            ByteBufCodecs.FLOAT,   HerbProperties::toxicity,
            ByteBufCodecs.FLOAT,   HerbProperties::potency,
            HerbProperties::new
        );
}
