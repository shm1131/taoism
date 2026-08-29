package io.github.shm1131.taoism.block.incubator.recipe;

import io.github.shm1131.taoism.TaoismMain;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModRecipeSerializers {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
        DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, TaoismMain.MODID);

    public static final Supplier<RecipeSerializer<IncubatorRecipe>> INCUBATOR_SERIALIZER =
        RECIPE_SERIALIZERS.register("incubator",
            () -> new RecipeSerializer<>(IncubatorRecipe.CODEC, IncubatorRecipe.STREAM_CODEC)
        );
}
