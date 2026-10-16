package io.github.shm1131.taoism.item.herb.pill;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.shm1131.taoism.init.TaoismConsumeEffects;
import io.github.shm1131.taoism.item.herb.base.Flavor;
import io.github.shm1131.taoism.item.herb.base.HerbProperties;
import io.github.shm1131.taoism.item.herb.base.Nature;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
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
        Flavor flavor = properties.flavor();

        if (toxicity > 0.3f && toxicity < 0.7f) {
            entity.addEffect(new MobEffectInstance(MobEffects.POISON,
                (int) (toxicity * 200), 0));
        }

        if (toxicity >= 0.7f) {
            entity.addEffect(new MobEffectInstance(MobEffects.POISON,
                (int) (toxicity * 200), 1));
        }

        if (nature == Nature.WARM) {
            entity.heal(potency * 2.0f);
        }

        if (nature == Nature.COLD) {
            entity.addEffect(new MobEffectInstance(MobEffects.SLOWNESS,
                (int) (potency * 200),0));
        }

        if (nature == Nature.NEUTRAL) {
            if (entity instanceof Player player) {
                player.getFoodData().eat(1,0.5f);
            }
        }

        if (flavor == Flavor.SWEET) {
            if (entity instanceof Player player) {
                player.getFoodData().eat(2,0.5f);
                player.heal(potency * 4.0f);
            }
        }

        if (flavor == Flavor.BITTER) {
            entity.addEffect(new MobEffectInstance(MobEffects.HUNGER,
                (int) (potency * 200),0));
        }

        if (flavor == Flavor.SPICY) {
            entity.addEffect(new MobEffectInstance(MobEffects.HASTE,
                (int) (potency * 200),0));
            entity.addEffect(new MobEffectInstance(MobEffects.SPEED,
                (int) (potency * 200),0));
        }

        return true;
    }

    @Override
    public Type<? extends ConsumeEffect> getType() {
        return TaoismConsumeEffects.PILL_EFFECT_TYPE.get();
    }
}
