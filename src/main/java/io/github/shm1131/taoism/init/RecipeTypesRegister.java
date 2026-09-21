package io.github.shm1131.taoism.init;

import io.github.shm1131.taoism.TaoismMain;
import io.github.shm1131.taoism.block.alchemy.recipe.AlchemyFurnaceRecipe;
import io.github.shm1131.taoism.block.incubator.recipe.IncubatorRecipe;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class RecipeTypesRegister {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
        DeferredRegister.create(BuiltInRegistries.RECIPE_TYPE, TaoismMain.MODID);

    public static final Supplier<RecipeType<IncubatorRecipe>> INCUBATOR_TYPE =
        RECIPE_TYPES.register("incubator", () ->
            RecipeType.simple(Identifier.fromNamespaceAndPath(TaoismMain.MODID, "incubator"))
        );

    public static final Supplier<RecipeType<AlchemyFurnaceRecipe>> ALCHEMY_FURNACE_TYPE =
        RECIPE_TYPES.register("alchemy_furnace", () ->
            RecipeType.simple(Identifier.fromNamespaceAndPath(TaoismMain.MODID, "alchemy_furnace"))
        );
}
