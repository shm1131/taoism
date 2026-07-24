package io.github.shm1131.taoism.advancement;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.advancements.AdvancementSubProvider;
import net.minecraft.world.item.Items;
import net.minecraft.network.chat.Component;
import java.util.function.Consumer;

public class ModAdvancementSubProvider implements AdvancementSubProvider {

    @Override
    public void generate(HolderLookup.Provider registries, Consumer<AdvancementHolder> saver) {

        // 1. 定义根进度 (Root)
        // display() 参数: icon, title, description, background, frameType, showToast, announceToChat, hidden
        AdvancementHolder root = Advancement.Builder.advancement()
                .display(
                        Items.COMPASS,
                        Component.translatable("advancements.modid.root.title"),
                        Component.translatable("advancements.modid.root.desc"),
                        null, // 背景纹理，null 使用默认
                        AdvancementType.TASK,
                        true, true, false
                )
                .addCriterion("tick", InventoryChangeTrigger.TriggerInstance.hasItems(Items.COMPASS))
                .save(saver, "taoism:root"); // 保存并获取 Holder

        // 2. 定义子进度 (Child)
        // parent(root) 表示依赖根进度
        Advancement.Builder.advancement()
                .parent(root)
                .display(
                        Items.CRAFTING_TABLE,
                        Component.translatable("advancements.modid.first_craft.title"),
                        Component.translatable("advancements.modid.first_craft.desc"),
                        null,
                        AdvancementType.TASK,
                        true, true, false
                )
                .addCriterion("craft_table", InventoryChangeTrigger.TriggerInstance.hasItems(Items.CRAFTING_TABLE))
                .save(saver, "taoism:first_craft");


    }
}