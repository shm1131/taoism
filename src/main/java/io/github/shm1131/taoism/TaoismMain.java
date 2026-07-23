package io.github.shm1131.taoism;


import io.github.shm1131.taoism.datagen.biomes.BiomeSourceRegister;
import io.github.shm1131.taoism.init.*;

import io.github.shm1131.taoism.network.SyncCultivationDataPayload;
import io.github.shm1131.taoism.network.SyncTaoismDataPayload;
import io.github.shm1131.taoism.player.attachment.cultivation.CultivationAttachment;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;
import io.github.shm1131.taoism.datagen.DataGenProvider;
import io.github.shm1131.taoism.network.handler.NetworkHandlerClient;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(TaoismMain.MODID)
public class TaoismMain {


    public static final String MODID = "taoism";

    public static final Logger LOGGER = LogUtils.getLogger();


    public TaoismMain(IEventBus modEventBus, ModContainer modContainer) {

        BlockRegister.BLOCKS.register(modEventBus);
        BlockRegister.ITEMS.register(modEventBus);
        EffectRegister.EFFECTS.register(modEventBus);
        ItemRegister.ITEMS.register(modEventBus);
        EntityRegister.ENTITIES.register(modEventBus);
        BiomeSourceRegister.BIOME_SOURCES.register(modEventBus);
        //CreativeTabRegister.TABS.register(modEventBus);

        TaoismAttachments.ATTACHMENT_TYPES.register(modEventBus);
        CultivationAttachment.ATTACHMENT_TYPES.register(modEventBus);

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
    }
}
