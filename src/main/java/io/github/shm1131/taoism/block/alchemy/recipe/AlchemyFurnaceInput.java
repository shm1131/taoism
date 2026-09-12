package io.github.shm1131.taoism.block.alchemy.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

import java.util.List;

public record AlchemyFurnaceInput(ItemStack fuel, List<ItemStack> inputs) implements RecipeInput {
    @Override public ItemStack getItem(int slot) {
        if (slot == 0) return fuel;
        if (slot >= 1 && slot <= 3) return inputs.get(slot - 1);
        return ItemStack.EMPTY;
    }
    @Override public int size() { return 4; }
}
