package io.github.shm1131.taoism.block.alchemy.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.shm1131.taoism.init.RecipeSerializersRegister;
import io.github.shm1131.taoism.init.RecipeTypesRegister;
import io.github.shm1131.taoism.item.herb.PropertiesHelper;
import io.github.shm1131.taoism.item.herb.base.Flavor;
import io.github.shm1131.taoism.item.herb.base.HerbProperties;
import io.github.shm1131.taoism.item.herb.base.Nature;
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
        HerbProperties p1 = PropertiesHelper.getProperties(input.inputs().get(0));
        HerbProperties p2 = PropertiesHelper.getProperties(input.inputs().get(1));
        HerbProperties p3 = PropertiesHelper.getProperties(input.inputs().get(2));

        Flavor dominantFlavor = getDominant(p1.flavor(), p2.flavor(), p3.flavor());
        Nature dominantNature = getDominant(p1.nature(), p2.nature(), p3.nature());
        float avgToxicity = Math.round((p1.toxicity() + p2.toxicity() + p3.toxicity()) / 3f * 10f) / 10f;
        float avgPotency  = Math.round((p1.potency()  + p2.potency()  + p3.potency())  / 3f * 10f) / 10f;
//TODO:毒性为什么一直是0，其他的数据都对
        HerbProperties resultProps = new HerbProperties(dominantFlavor, dominantNature, avgToxicity, avgPotency);

        ItemStack result = this.resultTemplate.create().copy();
        PropertiesHelper.setProperties(result, resultProps);
        return result;
    }

    private static <T> T getDominant(T a, T b, T c) {
        if (a == b || a == c) return a;
        if (b == c) return b;
        return a;
    }

    @Override
    public PlacementInfo placementInfo() {
        return PlacementInfo.NOT_PLACEABLE;
    }

    @Override
    public boolean showNotification() {
        return commonInfo.showNotification();
    }

    @Override
    public String group() {
        return "";
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return RecipeBookCategories.CRAFTING_MISC;
    }

    public ItemStackTemplate getResultTemplate() {
        return this.resultTemplate;
    }

    @Override
    public RecipeSerializer<? extends Recipe<AlchemyFurnaceInput>> getSerializer() {
        return RecipeSerializersRegister.ALCHEMY_FURNACE.get();
    }

    @Override
    public RecipeType<? extends Recipe<AlchemyFurnaceInput>> getType() {
        return RecipeTypesRegister.ALCHEMY_FURNACE_TYPE.get();
    }


    public static final MapCodec<AlchemyFurnaceRecipe> CODEC = RecordCodecBuilder.mapCodec(instance ->
        instance.group(
            CommonInfo.MAP_CODEC.forGetter(AlchemyFurnaceRecipe::commonInfo),
            Ingredient.CODEC.fieldOf("fuel").forGetter(AlchemyFurnaceRecipe::fuelIngredient),
            Ingredient.CODEC.listOf(3, 3).fieldOf("ingredients").forGetter(AlchemyFurnaceRecipe::inputs),
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
