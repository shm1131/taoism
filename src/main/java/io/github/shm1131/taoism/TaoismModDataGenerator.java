package io.github.shm1131.taoism;

import io.github.shm1131.taoism.advancement.ModAdvancementSubProvider;
import io.github.shm1131.taoism.datagen.*;
import net.minecraft.data.advancements.AdvancementProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.List;
import java.util.Set;

@EventBusSubscriber(modid = TaoismMain.MODID)
public class TaoismModDataGenerator {
    @SubscribeEvent
    public static void GatherData(GatherDataEvent.Client event) {
        //成就
        event.createProvider((output, lookupProvider) ->
            new AdvancementProvider(
                output,
                lookupProvider,
                List.of(new ModAdvancementSubProvider())
            )
        );

        //战利品表
        event.createProvider(((output, lookupProvider) ->
            new LootTableProvider(output, Set.of(),List.of(new LootTableProvider.SubProviderEntry(
                ModBlockLootTablesProvider::new, LootContextParamSets.BLOCK
            )),lookupProvider)));

        //合成表
        event.createProvider(ModRecipesProvider.Runner::new);

        //标签
        event.createProvider(ModBlockTagsProvider::new);
        event.createProvider(ModItemTagProvider::new);

        //物品方块模型生成
        event.createProvider(ModModelsProvider::new);

        //语言文件
        event.createProvider(ModEnUsLangProvider::new);
        event.createProvider(ModZhCnLangProvider::new);


        //世界生成
        event.createProvider(((output, lookupProvider) ->
            new WorldGenProvider(output, lookupProvider)));

    }
}
