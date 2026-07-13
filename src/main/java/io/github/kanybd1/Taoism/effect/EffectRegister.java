package io.github.kanybd1.Taoism.effect;

import io.github.kanybd1.Taoism.TaoismMain;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.neoforge.registries.DeferredRegister;

public class EffectRegister {
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, TaoismMain.MODID);
}
