//CHANGED:实现接口StringRepresentable来适配丹药动态标签

package io.github.shm1131.taoism.item.herb.base;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;

public enum Nature implements StringRepresentable {
    COLD("cold"),
    NEUTRAL("neutral"),
    WARM("warm");

    private final String name;

    public static final Codec<Nature> CODEC = StringRepresentable.fromEnum(Nature::values);

    public static final StreamCodec<ByteBuf, Nature> STREAM_CODEC =
        ByteBufCodecs.idMapper(
            i -> Nature.values()[i],
            Enum::ordinal
        );

    Nature(String name) {
        this.name = name;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }

    public String translationKey() { return "taoism.nature." + this.name; }
}
