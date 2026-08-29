package io.github.shm1131.taoism.player.attachment.api;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public interface ICoolDownData {

    MapCodec<ICoolDownData> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        Codec.LONG.fieldOf("drop_cool_down").forGetter(ICoolDownData::getDropCoolDown),
        Codec.LONG.fieldOf("cheng_fu_cool_down").forGetter(ICoolDownData::getChengFuCoolDown)
    ).apply(instance, CoolDownData::new));

    long getDropCoolDown();
    long getChengFuCoolDown();

    ICoolDownData EMPTY = new CoolDownData(0L, 0L);

    record CoolDownData(long dropCoolDown, long chengFuCoolDown) implements ICoolDownData {
        @Override
        public long getDropCoolDown() { return dropCoolDown; }

        @Override
        public long getChengFuCoolDown() { return chengFuCoolDown; }
    }
}
