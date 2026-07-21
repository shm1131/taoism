package io.github.shm1131.taoism.data.cultivation;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum CultivationRealm {
    // ID使用标准拼音，避免中文直接作为序列化键
    ZHU_JI("zhu_ji", "筑基", 0, 500),
    LIAN_JING("lian_jing_hua_qi", "炼精化气", 1, 2000),
    LIAN_QI("lian_qi_hua_shen", "炼气化神", 2, 8000),
    LIAN_SHEN("lian_shen_huan_xu", "炼神还虚", 3, 30000),
    LIAN_XU("lian_xu_he_dao", "炼虚合道", 4, 100000);

    private final String id;
    private final String displayName;
    private final int tier;
    private final int maxSpiritualPower;

    CultivationRealm(String id, String displayName, int tier, int maxSp) {
        this.id = id;
        this.displayName = displayName;
        this.tier = tier;
        this.maxSpiritualPower = maxSp;
    }

    public String getId() { return id; }
    public String getDisplayName() { return displayName; }
    public int getTier() { return tier; }
    public int getMaxSpiritualPower() { return maxSpiritualPower; }

    private static final Map<String, CultivationRealm> BY_ID =
            Arrays.stream(values())
                    .collect(Collectors.toMap(CultivationRealm::getId, Function.identity()));

    public static Optional<CultivationRealm> byId(String id) {
        return Optional.ofNullable(BY_ID.get(id));
    }

    public Optional<CultivationRealm> next() {
        CultivationRealm[] realms = values();
        int nextOrdinal = this.ordinal() + 1;
        return nextOrdinal < realms.length
                ? Optional.of(realms[nextOrdinal])
                : Optional.empty();
    }

    public static final Codec<CultivationRealm> CODEC =
            Codec.STRING.xmap(
                    id -> byId(id).orElseThrow(() ->
                            new IllegalArgumentException("Unknown cultivation realm: " + id)),
                    CultivationRealm::getId
            );

    public static final StreamCodec<ByteBuf, CultivationRealm> STREAM_CODEC =
            ByteBufCodecs.STRING_UTF8.map(
                    id -> byId(id).orElse(ZHU_JI),
                    CultivationRealm::getId
            );
}