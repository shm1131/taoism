package io.github.kanybd1.taoism.init;

import io.github.kanybd1.taoism.TaoismMain;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.neoforge.registries.DeferredRegister;

public class EffectRegister {
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, TaoismMain.MODID);
}
