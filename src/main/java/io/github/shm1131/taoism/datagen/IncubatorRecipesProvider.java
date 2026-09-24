package io.github.shm1131.taoism.datagen;

import io.github.shm1131.taoism.TaoismMain;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;

public class IncubatorRecipesProvider extends RecipeProvider {

    private static final int DEFAULT_INCUBATION_TIME = 100;

    protected IncubatorRecipesProvider(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output);
    }


    @Override
    protected void buildRecipes() {

    }


    public static class Runner extends RecipeProvider.Runner {
        public Runner(net.minecraft.data.PackOutput packOutput, java.util.concurrent.CompletableFuture<HolderLookup.Provider> registries) {
            super(packOutput, registries);
        }

        @Override
        protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
            return new IncubatorRecipesProvider(registries, output);
        }

        @Override
        public String getName() {
            return "recipe generation for " + TaoismMain.MODID;
        }
    }
}
