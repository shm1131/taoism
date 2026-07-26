//CHANGED:实现接口StringRepresentable来适配丹药动态标签

package io.github.shm1131.taoism.item.herb.base;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;

public enum Flavor implements StringRepresentable {
    SOUR("sour"),
    SPICY("spicy"),
    SALTY("salty"),
    BITTER("bitter"),
    SWEET("sweet");

    private final String name;

    public static final Codec<Flavor> CODEC = StringRepresentable.fromEnum(Flavor::values);

    public static final StreamCodec<ByteBuf, Flavor> STREAM_CODEC =
        ByteBufCodecs.idMapper(
            i -> Flavor.values()[i],
            Enum::ordinal
        );

    Flavor(String name) {
        this.name = name;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }

    public String translationKey() { return "taoism.flavor." + this.name; }
}
