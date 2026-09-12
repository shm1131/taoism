package io.github.shm1131.taoism.init;

import io.github.shm1131.taoism.TaoismMain;
import io.github.shm1131.taoism.block.alchemy.AlchemyFurnaceBlockEntity;
import io.github.shm1131.taoism.block.incubator.IncubatorBlockEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
        DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, TaoismMain.MODID);

    @SuppressWarnings("unchecked")
    public static final Supplier<BlockEntityType<IncubatorBlockEntity>> INCUBATOR =
        BLOCK_ENTITIES.register("incubator", () -> new BlockEntityType<>(
            IncubatorBlockEntity::new,
            BlockRegister.INCUBATOR.get()
        ));

    public static final Supplier<BlockEntityType<AlchemyFurnaceBlockEntity>> ALCHEMY_FURNACE =
        BLOCK_ENTITIES.register("alchemy_furnace", () ->new BlockEntityType<>(
            AlchemyFurnaceBlockEntity::new,
            BlockRegister.ALCHEMY_FURNACE_BLOCK.get()
        ));
}
