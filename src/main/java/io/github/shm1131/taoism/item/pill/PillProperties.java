//ADDED:新建类PillProperties

package io.github.shm1131.taoism.item.pill;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.shm1131.taoism.item.herb.base.Flavor;
import io.github.shm1131.taoism.item.herb.base.Nature;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record PillProperties(
    Flavor flavor,
    Nature nature,
    float toxicity,
    float potency
) {
    public static final Codec<PillProperties> CODEC = RecordCodecBuilder.create(inst -> inst.group(
        Flavor.CODEC.fieldOf("flavor").forGetter(PillProperties::flavor),
        Nature.CODEC.fieldOf("nature").forGetter(PillProperties::nature),
        Codec.FLOAT.fieldOf("toxicity").forGetter(PillProperties::toxicity),
        Codec.FLOAT.fieldOf("potency").forGetter(PillProperties::potency)
    ).apply(inst, PillProperties::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, PillProperties> STREAM_CODEC =
        StreamCodec.composite(
            Flavor.STREAM_CODEC, PillProperties::flavor,
            Nature.STREAM_CODEC, PillProperties::nature,
            ByteBufCodecs.FLOAT,   PillProperties::toxicity,
            ByteBufCodecs.FLOAT,   PillProperties::potency,
            PillProperties::new
        );
}
