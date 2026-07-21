package io.github.shm1131.taoism.entity;

<<<<<<<< HEAD:src/main/java/io/github/shm1131/taoism/entity/TextZombie.java
package io.github.shm1131.taoism.entity;
========
package io.github.kanybd1.taoism.entity;
>>>>>>>> 15c76e4 (🐺📚):src/main/java/io/github/kanybd1/taoism/entity/TextZombie.java

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

public class TextZombie extends Monster {

    public TextZombie(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.23D)
                .add(Attributes.ATTACK_DAMAGE,3.0D);
    }
}
