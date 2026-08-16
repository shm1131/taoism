package io.github.shm1131.taoism.player.attachment.api;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public interface IJingLiData {

    float MAX_JING_LI_EAT = 10;
    float MAX_JING_LI_SLEEP = 10;
    float TOTAL_JING_LI = MAX_JING_LI_EAT + MAX_JING_LI_SLEEP;
    float EXHAUSTION_THRESHOLD = 2.0f;

    StreamCodec<FriendlyByteBuf, IJingLiData> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public IJingLiData decode(FriendlyByteBuf buf) {
            return new JingLiData(
                buf.readFloat(),
                buf.readFloat(),
                buf.readFloat()
            );
        }

        @Override
        public void encode(FriendlyByteBuf buf, IJingLiData data) {
            buf.writeFloat(data.getJingLiEat());
            buf.writeFloat(data.getJingLiSleep());
            buf.writeFloat(data.getExhaustion());
        }
    };

    MapCodec<IJingLiData> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        Codec.FLOAT.fieldOf("jing_li_eat").forGetter(IJingLiData::getJingLiEat),
        Codec.FLOAT.fieldOf("jing_li_sleep").forGetter(IJingLiData::getJingLiSleep),
        Codec.FLOAT.fieldOf("exhaustion").forGetter(IJingLiData::getExhaustion)
    ).apply(instance, JingLiData::new));

    IJingLiData EMPTY = new JingLiData(MAX_JING_LI_EAT, MAX_JING_LI_SLEEP, 0);

    float getJingLiEat();
    float getJingLiSleep();
    float getExhaustion();
    float getTotalJingLi();

    record JingLiData(float jingLiEat, float jingLiSleep, float exhaustion) implements IJingLiData {
        @Override
        public float getJingLiEat() {
            return jingLiEat;
        }

        @Override
        public float getJingLiSleep() {
            return jingLiSleep;
        }

        @Override
        public float getExhaustion() {
            return exhaustion;
        }

        @Override
        public float getTotalJingLi() {
            return jingLiEat + jingLiSleep;
        }
    }
}
