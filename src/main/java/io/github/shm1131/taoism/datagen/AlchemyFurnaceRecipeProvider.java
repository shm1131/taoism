package io.github.shm1131.taoism.datagen;

import io.github.shm1131.taoism.TaoismMain;
import io.github.shm1131.taoism.block.alchemy.recipe.AlchemyFurnaceRecipe;
import io.github.shm1131.taoism.init.ItemRegister;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
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
        Ingredient commonFuel = Ingredient.of(itemRegistry.getOrThrow(ItemTags.COALS));

        // ========== 植物草药炼丹 ==========
        addRecipe("pill_ren_shen_ling_zhi_gan_cao",
            commonFuel,
            Ingredient.of(ItemRegister.HERBS.get("ling_zhi").get()),
            Ingredient.of(ItemRegister.HERBS.get("gan_cao").get()),
            Ingredient.of(ItemRegister.HERBS.get("da_zao").get()),
            ItemRegister.PILL.get(), 1, 200, 0.5f
        );

        addRecipe("pill_ju_hua_di_huang_gou_qi",
            commonFuel,
            Ingredient.of(ItemRegister.HERBS.get("di_huang").get()),
            Ingredient.of(ItemRegister.HERBS.get("gou_qi").get()),
            Ingredient.of(ItemRegister.HERBS.get("hei_zhi_ma").get()),
            ItemRegister.PILL.get(), 1, 200, 0.4f
        );

        // ========== 矿物/特殊药材炼丹 ==========
        addRecipe("pill_zhu_sha_xiong_huang_liu_huang",
            commonFuel,
            Ingredient.of(ItemRegister.MINERAL_HERBS.get("xiong_huang").get()),
            Ingredient.of(ItemRegister.MINERAL_HERBS.get("liu_huang").get()),
            Ingredient.of(ItemRegister.MINERAL_HERBS.get("ci_huang").get()),
            ItemRegister.PILL.get(), 1, 400, 1.0f
        );

        addRecipe("pill_jin_fen_yun_mu_yin_fen",
            commonFuel,
            Ingredient.of(ItemRegister.MINERAL_HERBS.get("yun_mu_fen").get()),
            Ingredient.of(ItemRegister.MINERAL_HERBS.get("yin_fen").get()),
            Ingredient.of(ItemRegister.MINERAL_HERBS.get("qian_fen").get()),
            ItemRegister.PILL.get(), 1, 300, 0.8f
        );
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
