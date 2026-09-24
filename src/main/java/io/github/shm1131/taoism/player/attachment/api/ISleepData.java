package io.github.shm1131.taoism.player.attachment.api;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public interface ISleepData {

    MapCodec<ISleepData> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        Codec.BOOL.fieldOf("is_low_warning").forGetter(ISleepData::isLowWarning),
        Codec.BOOL.fieldOf("is_last_warning").forGetter(ISleepData::isLastWarning),
        Codec.BOOL.fieldOf("is_stay_up_late_warning").forGetter(ISleepData::isStayUpLateWarning),
        Codec.BOOL.fieldOf("is_stay_up_late").forGetter(ISleepData::isStayUpLate)
        ).apply(instance, SleepData::new));

    boolean isLowWarning();
    boolean isLastWarning();
    boolean isStayUpLateWarning();
    boolean isStayUpLate();

    ISleepData EMPTY = new SleepData(false,false,false,false);

    record SleepData(
        boolean isLowWarning, boolean isLastWarning,
        boolean isStayUpLateWarning, boolean isStayUpLate
    ) implements ISleepData {
        @Override
        public boolean isLowWarning() {return isLowWarning;}
        @Override
        public boolean isLastWarning() {return isLastWarning;}
        @Override
        public boolean isStayUpLateWarning() {return isStayUpLateWarning;}
        @Override
        public boolean isStayUpLate() {return isStayUpLate;}
    }
}
