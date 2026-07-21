package io.github.shm1131.taoism.init;

<<<<<<<< HEAD:src/main/java/io/github/shm1131/taoism/entity/EntityRegister.java
package io.github.shm1131.taoism.entity;

import io.github.shm1131.taoism.TaoismMain;
========
package io.github.kanybd1.taoism.init;

import io.github.kanybd1.taoism.entity.TextZombie;
>>>>>>>> 15c76e4 (🐺📚):src/main/java/io/github/kanybd1/taoism/init/EntityRegister.java
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;


public class EntityRegister {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(Registries.ENTITY_TYPE, TaoismMain.MODID);

    public static final Supplier<EntityType<TextZombie>> TEXT_ZOMBIE =
            ENTITIES.register("text_zombie",
                    ()-> EntityType.Builder.of(TextZombie::new , MobCategory.MONSTER)
                            .sized(1.8f,0.9f)
                            .build(ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(TaoismMain.MODID,"text_zombie")))
            );
}


