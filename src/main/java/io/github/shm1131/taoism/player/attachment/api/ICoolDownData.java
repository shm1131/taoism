package io.github.shm1131.taoism.player.attachment.api;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public interface ICoolDownData {

    MapCodec<ICoolDownData> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        Codec.LONG.fieldOf("drop_cool_down").forGetter(ICoolDownData::getDropCoolDown)
    ).apply(instance, CoolDownData::new));

    long getDropCoolDown();

    ICoolDownData EMPTY = new CoolDownData(0L); // ⭐ 0 → 0L

    record CoolDownData(long dropCoolDown) implements ICoolDownData {
        @Override
        public long getDropCoolDown() { return dropCoolDown; }
    }
}
