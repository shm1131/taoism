package io.github.shm1131.taoism.datagen;

import io.github.shm1131.taoism.init.BlockRegister;
import io.github.shm1131.taoism.init.ItemRegister;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.advancements.criterion.PlayerTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.advancements.AdvancementSubProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Items;

import java.util.function.Consumer;

public class ModAdvancementSubProvider implements AdvancementSubProvider {

    @Override
    public void generate(HolderLookup.Provider registries, Consumer<AdvancementHolder> saver) {

        AdvancementHolder root = Advancement.Builder.advancement()
            .display(
                ItemRegister.ICON_ITEM.get(),
                Component.translatable("advancements.taoism.root.title"),
                Component.translatable("advancements.taoism.root.desc"),
                Identifier.withDefaultNamespace("textures/gui/advancements/backgrounds/stone.png"),
                AdvancementType.TASK,
                true,
                true,
                false
            )
            .addCriterion("entered_world", PlayerTrigger.TriggerInstance.tick())
            .save(saver, "taoism:root");


        AdvancementHolder herbDiscovery = Advancement.Builder.advancement()
            .parent(root)
            .display(
                Items.BOOK,
                Component.translatable("advancements.taoism.herb_discovery.title"),
                Component.translatable("advancements.taoism.herb_discovery.desc"),
                null,
                AdvancementType.TASK,
                true, true, false
            )

            .addCriterion("entered_world", PlayerTrigger.TriggerInstance.tick())
            .save(saver, "taoism:herb_discovery");


        Advancement.Builder.advancement()
            .parent(herbDiscovery)
            .display(
                BlockRegister.ALCHEMY_FURNACE_BLOCK.get(),
                Component.translatable("advancements.taoism.alchemy_furnace.title"),
                Component.translatable("advancements.taoism.alchemy_furnace.desc"),
                null,
                AdvancementType.GOAL,
                true, true, false
            )
            .addCriterion("has_alchemy_furnace",
                InventoryChangeTrigger.TriggerInstance.hasItems(BlockRegister.ALCHEMY_FURNACE_BLOCK.get()))
            .save(saver, "taoism:alchemy_furnace");
    }
}
