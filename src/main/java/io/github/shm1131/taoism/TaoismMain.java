package io.github.shm1131.taoism;

import com.mojang.serialization.MapCodec;
import io.github.shm1131.taoism.advancement.ModAdvancementSubProvider;
import io.github.shm1131.taoism.block.BlockRegister;
import io.github.shm1131.taoism.effect.EffectRegister;
import io.github.shm1131.taoism.entity.EntityRegister;
import io.github.shm1131.taoism.item.ItemRegister;
import io.github.shm1131.taoism.level.ModWorldGenProvider;
import io.github.shm1131.taoism.level.biomes.BiomeSourceRegister;
import io.github.shm1131.taoism.loot.HerbDropModifier;
import io.github.shm1131.taoism.player.data.attachment.TaoismAttachments;
import io.github.shm1131.taoism.player.data.cultivation.CultivationAttachment;
import io.github.shm1131.taoism.player.network.SyncCultivationDataPayload;
import io.github.shm1131.taoism.player.network.SyncTaoismDataPayload;
import net.minecraft.data.advancements.AdvancementProvider;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;

import io.github.shm1131.taoism.datagen.biomes.BiomeSourceRegister;
import io.github.shm1131.taoism.init.*;

import io.github.shm1131.taoism.network.SyncCultivationDataPayload;
import io.github.shm1131.taoism.network.SyncTaoismDataPayload;
import io.github.shm1131.taoism.player.attachment.cultivation.CultivationAttachment;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;
import io.github.shm1131.taoism.datagen.DataGenProvider;
import io.github.shm1131.taoism.network.handler.NetworkHandlerClient;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

import java.util.List;
import java.util.function.Supplier;

@Mod(TaoismMain.MODID)
public class TaoismMain {


    public static final String MODID = "taoism";

    public static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> GLM_SERIALIZERS =
            DeferredRegister.create(NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, TaoismMain.MODID);

    public static final Supplier<MapCodec<HerbDropModifier>> HERB_DROP =
            GLM_SERIALIZERS.register("herb_drop", () -> HerbDropModifier.CODEC);

    public TaoismMain(IEventBus modEventBus, ModContainer modContainer) {

        BlockRegister.BLOCKS.register(modEventBus);
        BlockRegister.ITEMS.register(modEventBus);
        EffectRegister.EFFECTS.register(modEventBus);
        ItemRegister.ITEMS.register(modEventBus);
        EntityRegister.ENTITIES.register(modEventBus);
        BiomeSourceRegister.BIOME_SOURCES.register(modEventBus);
        CreativeTabRegister.TABS.register(modEventBus);

        TaoismAttachments.ATTACHMENT_TYPES.register(modEventBus);
        CultivationAttachment.ATTACHMENT_TYPES.register(modEventBus);

        GLM_SERIALIZERS.register(modEventBus);

        modEventBus.addListener(this::registerPayloads);
        modEventBus.addListener(this::onGatherData);
    }

    private void registerPayloads(RegisterPayloadHandlersEvent event) {
        var registrar = event.registrar(MODID);
        registrar.playToClient(
            SyncTaoismDataPayload.TYPE,
            SyncTaoismDataPayload.STREAM_CODEC
        );

        registrar.playToClient(
            SyncCultivationDataPayload.TYPE,
            SyncCultivationDataPayload.STREAM_CODEC
        );
    }

    public void onGatherData(GatherDataEvent.Client event) {
        if (event instanceof GatherDataEvent.Client) {
            event.createDatapackRegistryObjects(DataGenProvider.BUILDER);
        }
        event.createProvider((output, lookupProvider) ->
                new AdvancementProvider(
                        output,
                        lookupProvider,
                        List.of(new ModAdvancementSubProvider())
                )
        );
    }



}
