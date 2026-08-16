package io.github.shm1131.taoism.player.attachment.api;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public interface IDeathInventoryData {

    MapCodec<IDeathInventoryData> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        ItemStack.CODEC.listOf().fieldOf("items").forGetter(IDeathInventoryData::getItems)
    ).apply(instance, DeathInventoryData::new));

    IDeathInventoryData EMPTY = new DeathInventoryData(List.of());

    List<ItemStack> getItems();

    record DeathInventoryData(List<ItemStack> items) implements IDeathInventoryData {
        @Override
        public List<ItemStack> getItems() {
            return this.items;
        }
    }
}
