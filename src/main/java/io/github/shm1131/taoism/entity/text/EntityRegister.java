package io.github.shm1131.taoism.entity.text;

import io.github.shm1131.taoism.TaoismMain;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;


public class EntityRegister {
  public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(Registries.ENTITY_TYPE, TaoismMain.MODID);

  public static final Supplier<EntityType<TextEntity>> TEXT_ENTITY =
      ENTITIES.register("text_entity",
          () -> EntityType.Builder.of(TextEntity::new, MobCategory.MONSTER)
              .sized(0.9f, 1.8f)            //碰撞箱
              .build(ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(TaoismMain.MODID, "text_entity")))
      );
}


