package io.github.shm1131.taoism;

import io.github.shm1131.taoism.block.BlockRegister;
import io.github.shm1131.taoism.effect.EffectRegister;
import io.github.shm1131.taoism.entity.EntityRegister;
import io.github.shm1131.taoism.item.ItemRegister;
import io.github.shm1131.taoism.level.ModWorldGenProvider;
import io.github.shm1131.taoism.level.biomes.BiomeSourceRegister;
import io.github.shm1131.taoism.player.data.attachment.TaoismAttachments;
import io.github.shm1131.taoism.player.data.cultivation.CultivationAttachment;
import io.github.shm1131.taoism.player.network.SyncCultivationDataPayload;
import io.github.shm1131.taoism.player.network.SyncTaoismDataPayload;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;

@Mod(TaoismMain.MODID)
public class TaoismMain {


    public static final String MODID = "taoism";

    public static final Logger LOGGER = LogUtils.getLogger();


    public TaoismMain(IEventBus modEventBus, ModContainer modContainer) {

        BlockRegister.BLOCKS.register(modEventBus);
        EffectRegister.EFFECTS.register(modEventBus);
        ItemRegister.ITEMS.register(modEventBus);
        EntityRegister.ENTITIES.register(modEventBus);
        BiomeSourceRegister.BIOME_SOURCES.register(modEventBus);

        TaoismAttachments.ATTACHMENT_TYPES.register(modEventBus);
        CultivationAttachment.ATTACHMENT_TYPES.register(modEventBus);

        modEventBus.addListener(this::registerPayloads);
        modEventBus.addListener(this::onGatherData);
    }

    private void registerPayloads(RegisterPayloadHandlersEvent event) {
        var registrar = event.registrar(MODID);
        registrar.playToClient(SyncTaoismDataPayload.TYPE, SyncTaoismDataPayload.STREAM_CODEC, SyncTaoismDataPayload::handle);
        registrar.playToClient(SyncCultivationDataPayload.TYPE, SyncCultivationDataPayload.STREAM_CODEC, SyncCultivationDataPayload::handle);
    }

    public void onGatherData(GatherDataEvent.Client event) {
        if (event instanceof GatherDataEvent.Client) {
            event.createDatapackRegistryObjects(ModWorldGenProvider.BUILDER);
        }
    }


}
