package io.github.shm1131.taoism.datagen;

import io.github.shm1131.taoism.TaoismMain;
import io.github.shm1131.taoism.block.alchemy.recipe.AlchemyFurnaceRecipe;
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

import java.util.List;

public class AlchemyFurnaceRecipeProvider extends RecipeProvider {

    protected AlchemyFurnaceRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output);
    }

    @Override
    protected void buildRecipes() {
        var itemRegistry = this.registries.lookupOrThrow(Registries.ITEM);
    }

    /**
     * 便捷构建器：自动处理路径、ResourceKey 和 ItemStackTemplate
     *
     * @param name       配方ID（自动添加 alchemy_furnace/ 前缀）
     * @param fuel       燃料/引子药材
     * @param input1     主药槽位1
     * @param input2     主药槽位2
     * @param input3     主药槽位3
     * @param resultItem 产出物品（通常为 PILL）
     * @param count      产出数量
     * @param burnTime   炼制时间（ticks）
     * @param experience 炼制经验
     */
    private void addRecipe(String name,
                           Ingredient fuel,
                           Ingredient input1, Ingredient input2, Ingredient input3,
                           Item resultItem, int count,
                           int burnTime, float experience) {
        ResourceKey<Recipe<?>> key = ResourceKey.create(
            Registries.RECIPE,
            Identifier.fromNamespaceAndPath(TaoismMain.MODID, "alchemy_furnace/" + name)
        );

        AlchemyFurnaceRecipe recipe = new AlchemyFurnaceRecipe(
            new Recipe.CommonInfo(true),
            fuel,
            List.of(input1, input2, input3),
            new ItemStackTemplate(resultItem, count),
            burnTime,
            experience
        );

        output.accept(key, recipe, null);
    }

    // ==================== Runner（DataGen 入口） ====================

    public static class Runner extends RecipeProvider.Runner {
        public Runner(net.minecraft.data.PackOutput packOutput,
                      java.util.concurrent.CompletableFuture<HolderLookup.Provider> registries) {
            super(packOutput, registries);
        }

        @Override
        protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
            return new AlchemyFurnaceRecipeProvider(registries, output);
        }

        @Override
        public String getName() {
            return "Alchemy Furnace recipe generation for " + TaoismMain.MODID;
        }
    }
}
