package io.github.kanybd1.Taoism.player.data.cultivation;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public interface ICultivationData {
    CultivationRealm getRealm();

    int getCurrentSp();

    public static final StreamCodec<RegistryFriendlyByteBuf, ICultivationData> STREAM_CODEC =
            StreamCodec.composite(
                    CultivationRealm.STREAM_CODEC, ICultivationData::getRealm,
                    ByteBufCodecs.VAR_INT, ICultivationData::getCurrentSp,
                    (realm, sp) -> (ICultivationData) new CultivationData(realm, sp)  // 向上转型
            );

    MapCodec<ICultivationData> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            CultivationRealm.CODEC.fieldOf("realm").forGetter(ICultivationData::getRealm),
            Codec.INT.fieldOf("sp").forGetter(ICultivationData::getCurrentSp)
    ).apply(instance, (realm, sp) -> new CultivationData(realm, sp)));


    record CultivationData(CultivationRealm realm, int sp) implements ICultivationData {


        @Override
        public CultivationRealm getRealm() {
            return this.realm;
        }

        @Override
        public int getCurrentSp() {
            return this.sp;
        }
    }

    ICultivationData EMPTY = new CultivationData(CultivationRealm.ZHU_JI, 0);
}
