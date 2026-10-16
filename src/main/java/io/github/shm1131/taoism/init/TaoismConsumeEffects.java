package io.github.shm1131.taoism.init;

import io.github.shm1131.taoism.TaoismMain;
import io.github.shm1131.taoism.item.herb.pill.PillConsumeEffect;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.consume_effects.ConsumeEffect;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class TaoismConsumeEffects {

    public static final DeferredRegister<ConsumeEffect.Type<?>> CONSUME_EFFECTS =
        DeferredRegister.create(BuiltInRegistries.CONSUME_EFFECT_TYPE.key(), TaoismMain.MODID);

    public static final Supplier<ConsumeEffect.Type<PillConsumeEffect>> PILL_EFFECT_TYPE =
        CONSUME_EFFECTS.register("pill_effect", () ->
            new ConsumeEffect.Type<>(
                PillConsumeEffect.MAP_CODEC,
                PillConsumeEffect.STREAM_CODEC
            )
        );
}
