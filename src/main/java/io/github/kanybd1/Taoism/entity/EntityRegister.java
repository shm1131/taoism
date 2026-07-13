package io.github.kanybd1.Taoism.entity;

import io.github.kanybd1.Taoism.TaoismMain;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.registries.DeferredRegister;

public class EntityRegister {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(Registries.ENTITY_TYPE, TaoismMain.MODID);
}
