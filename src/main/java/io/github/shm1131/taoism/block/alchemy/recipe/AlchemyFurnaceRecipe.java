package io.github.shm1131.taoism.block.alchemy.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.shm1131.taoism.init.ModRecipeSerializers;
import io.github.shm1131.taoism.init.ModRecipeTypes;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public record AlchemyFurnaceRecipe(CommonInfo commonInfo, Ingredient fuelIngredient, List<Ingredient> inputs,
                                   ItemStackTemplate resultTemplate, int burnTime,
                                   float experience) implements Recipe<AlchemyFurnaceInput> {

    // ==================== Recipe 接口实现 ====================

    @Override
    public boolean matches(AlchemyFurnaceInput input, Level level) {
        if (!fuelIngredient.test(input.fuel())) return false;
        for (int i = 0; i < 3; i++) {
            if (!inputs.get(i).test(input.inputs().get(i))) return false;
        }
        return true;
    }

    @Override
    public ItemStack assemble(AlchemyFurnaceInput input) {
        return null;
    }

    @Override
    public boolean showNotification() {
        return commonInfo.showNotification();
    }

    @Override
    public String group() {
        return null;
    }

    @Override
    public PlacementInfo placementInfo() {
        return null;
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return null;
    }

    public ItemStackTemplate getResultTemplate() {
        return this.resultTemplate;
    }

    @Override
    public RecipeSerializer<? extends Recipe<AlchemyFurnaceInput>> getSerializer() {
        return ModRecipeSerializers.ALCHEMY_FURNACE.get();
    }

    @Override
    public RecipeType<? extends Recipe<AlchemyFurnaceInput>> getType() {
        return ModRecipeTypes.ALCHEMY_FURNACE_TYPE.get();
    }

    // ==================== Codec (完全对齐 IncubatorRecipe 模式) ====================

    public static final MapCodec<AlchemyFurnaceRecipe> CODEC = RecordCodecBuilder.mapCodec(instance ->
        instance.group(
            // ⭐ 与 IncubatorRecipe 完全一致的 CommonInfo 写法
            CommonInfo.MAP_CODEC.forGetter(AlchemyFurnaceRecipe::commonInfo),
            Ingredient.CODEC.fieldOf("fuel").forGetter(AlchemyFurnaceRecipe::fuelIngredient),
            Ingredient.CODEC.listOf(3, 3).fieldOf("ingredients").forGetter(AlchemyFurnaceRecipe::inputs),
            // ⭐ 使用 ItemStackTemplate.CODEC 而非 ItemStack.CODEC
            ItemStackTemplate.CODEC.fieldOf("result").forGetter(AlchemyFurnaceRecipe::resultTemplate),
            Codec.INT.fieldOf("burn_time").forGetter(AlchemyFurnaceRecipe::burnTime),
            Codec.FLOAT.optionalFieldOf("experience", 0f).forGetter(AlchemyFurnaceRecipe::experience)
        ).apply(instance, AlchemyFurnaceRecipe::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, AlchemyFurnaceRecipe> STREAM_CODEC =
        StreamCodec.composite(
            CommonInfo.STREAM_CODEC, AlchemyFurnaceRecipe::commonInfo,
            Ingredient.CONTENTS_STREAM_CODEC, AlchemyFurnaceRecipe::fuelIngredient,
            ByteBufCodecs.collection(ArrayList::new, Ingredient.CONTENTS_STREAM_CODEC, 3), AlchemyFurnaceRecipe::inputs,
            ItemStackTemplate.STREAM_CODEC, AlchemyFurnaceRecipe::resultTemplate,
            ByteBufCodecs.INT, AlchemyFurnaceRecipe::burnTime,
            ByteBufCodecs.FLOAT, AlchemyFurnaceRecipe::experience,
            AlchemyFurnaceRecipe::new
        );
}
