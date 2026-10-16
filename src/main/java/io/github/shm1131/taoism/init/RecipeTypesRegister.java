package io.github.shm1131.taoism.init;

import io.github.shm1131.taoism.TaoismMain;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class RecipeTypesRegister {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
        DeferredRegister.create(BuiltInRegistries.RECIPE_TYPE, TaoismMain.MODID);

}
