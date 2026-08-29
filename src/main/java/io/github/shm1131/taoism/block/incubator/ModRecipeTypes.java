package io.github.shm1131.taoism.block.incubator;

import io.github.shm1131.taoism.TaoismMain;
import io.github.shm1131.taoism.block.incubator.recipe.IncubatorRecipe;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModRecipeTypes {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
        DeferredRegister.create(BuiltInRegistries.RECIPE_TYPE, TaoismMain.MODID);

    public static final Supplier<RecipeType<IncubatorRecipe>> INCUBATOR_TYPE =
        RECIPE_TYPES.register("incubator", () ->
            RecipeType.simple(Identifier.fromNamespaceAndPath(TaoismMain.MODID, "incubator"))
        );
}
