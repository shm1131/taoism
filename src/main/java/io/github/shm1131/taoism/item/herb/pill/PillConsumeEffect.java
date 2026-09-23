package io.github.shm1131.taoism.item.herb.pill;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.shm1131.taoism.init.TaoismConsumeEffects;
import io.github.shm1131.taoism.item.herb.base.HerbProperties;
import io.github.shm1131.taoism.item.herb.base.Nature;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.consume_effects.ConsumeEffect;
import net.minecraft.world.level.Level;

public record PillConsumeEffect(HerbProperties properties) implements ConsumeEffect {

    public static final MapCodec<PillConsumeEffect> MAP_CODEC =
        RecordCodecBuilder.mapCodec(instance -> instance.group(
            HerbProperties.CODEC.fieldOf("properties").forGetter(PillConsumeEffect::properties)
        ).apply(instance, PillConsumeEffect::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, PillConsumeEffect> STREAM_CODEC =
        StreamCodec.composite(
            HerbProperties.STREAM_CODEC, PillConsumeEffect::properties,
            PillConsumeEffect::new
        );

    @Override
    public boolean apply(Level level, ItemStack stack, LivingEntity entity) {
        if (!(level instanceof ServerLevel)) return false;

        float potency  = properties.potency();
        float toxicity = properties.toxicity();
        Nature nature  = properties.nature();

        if (toxicity > 0.5f) {
            entity.addEffect(new MobEffectInstance(MobEffects.POISON,
                (int) (toxicity * 200), 1));
        }

        if (nature == Nature.WARM) {
            entity.heal(potency * 2.0f);
        }

        return true;
    }

    @Override
    public Type<? extends ConsumeEffect> getType() {
        return TaoismConsumeEffects.PILL_EFFECT_TYPE.get();
    }
}
