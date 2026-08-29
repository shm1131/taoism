package io.github.shm1131.taoism.datagen;

import io.github.shm1131.taoism.TaoismMain;
import io.github.shm1131.taoism.block.incubator.recipe.IncubatorRecipe;
import io.github.shm1131.taoism.init.ItemRegister;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ModRecipesProvider extends RecipeProvider {

    private static final int DEFAULT_INCUBATION_TIME = 100;

    protected ModRecipesProvider(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output);
    }


    @Override
    protected void buildRecipes() {
        for (var entry : ItemRegister.HERBS.entrySet()) {
            String name = entry.getKey();
            DeferredHolder<?, ?> holder = entry.getValue();

            if (holder.get() instanceof net.minecraft.world.item.Item herbItem) {
                ResourceKey<Recipe<?>> recipeKey = ResourceKey.create(
                    Registries.RECIPE,
                    Identifier.fromNamespaceAndPath(TaoismMain.MODID, "incubator/herb_breed/" + name)
                );

                IncubatorRecipe recipe = createIncubator(
                    Ingredient.of(herbItem),
                    herbItem,
                    2,
                    DEFAULT_INCUBATION_TIME
                );

                output.accept(recipeKey, recipe, null);
            }
        }
    }


    private IncubatorRecipe createIncubator(Ingredient input, Item resultItem, int resultCount, int incubationTime) {
        return new IncubatorRecipe(
            new Recipe.CommonInfo(true),
            input,
            new ItemStackTemplate(resultItem, resultCount),
            incubationTime
        );
    }

    public static class Runner extends RecipeProvider.Runner {
        public Runner(net.minecraft.data.PackOutput packOutput, java.util.concurrent.CompletableFuture<HolderLookup.Provider> registries) {
            super(packOutput, registries);
        }

        @Override
        protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
            return new ModRecipesProvider(registries, output);
        }

        @Override
        public String getName() {
            return "recipe generation for " + TaoismMain.MODID;
        }
    }
}
