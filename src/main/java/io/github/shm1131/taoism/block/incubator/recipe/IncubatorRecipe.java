package io.github.shm1131.taoism.block.incubator.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.shm1131.taoism.block.incubator.ModRecipeTypes;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

public class IncubatorRecipe extends SingleItemRecipe {

    private final int incubationTime;

    public IncubatorRecipe(CommonInfo commonInfo, Ingredient input, ItemStackTemplate result, int incubationTime) {
        super(commonInfo, input, result);
        this.incubationTime = incubationTime;
    }

    public int getIncubationTime() {
        return incubationTime;
    }

    public ItemStackTemplate getResultTemplate() {
        return this.result();
    }

    @Override
    public RecipeSerializer<? extends SingleItemRecipe> getSerializer() {
        return ModRecipeSerializers.INCUBATOR_SERIALIZER.get();
    }

    @Override
    public RecipeType<? extends SingleItemRecipe> getType() {
        return ModRecipeTypes.INCUBATOR_TYPE.get();
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return RecipeBookCategories.CRAFTING_MISC;
    }

    @Override
    public boolean matches(SingleRecipeInput input, Level level) {
        return super.matches(input, level);
    }

    @Override
    public ItemStack assemble(SingleRecipeInput input) {
        return super.assemble(input);
    }

    @Override
    public String group() {
        return "";
    }


    public static final MapCodec<IncubatorRecipe> CODEC = RecordCodecBuilder.mapCodec(instance ->
        instance.group(
            CommonInfo.MAP_CODEC.forGetter(recipe -> recipe.commonInfo),
            Ingredient.CODEC.fieldOf("ingredient").forGetter(SingleItemRecipe::input),
            ItemStackTemplate.CODEC.fieldOf("result").forGetter(IncubatorRecipe::getResultTemplate),
            Codec.INT.fieldOf("incubation_time").forGetter(IncubatorRecipe::getIncubationTime)
        ).apply(instance, IncubatorRecipe::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, IncubatorRecipe> STREAM_CODEC =
        StreamCodec.composite(
            CommonInfo.STREAM_CODEC, recipe -> recipe.commonInfo,
            Ingredient.CONTENTS_STREAM_CODEC, SingleItemRecipe::input,
            ItemStackTemplate.STREAM_CODEC, IncubatorRecipe::getResultTemplate,
            ByteBufCodecs.INT, IncubatorRecipe::getIncubationTime,
            IncubatorRecipe::new
        );
}
