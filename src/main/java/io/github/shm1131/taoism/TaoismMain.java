package io.github.shm1131.taoism;

import com.mojang.logging.LogUtils;
import io.github.shm1131.taoism.datagen.DataGeneratorProvider;
import io.github.shm1131.taoism.datagen.world.biomes.BiomeSourceRegister;
import io.github.shm1131.taoism.init.EffectRegister;
import io.github.shm1131.taoism.network.SyncTaoismDataPayload;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.HandlerThread;
import org.slf4j.Logger;

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

    TaoismAttachments.register(modEventBus);

    modEventBus.addListener(this::registerPayloads);
    modEventBus.addListener(this::onGatherData);
  }

  private void registerPayloads(RegisterPayloadHandlersEvent event) {
    var registrar = event.registrar(MODID);
    registrar = registrar.executesOn(HandlerThread.NETWORK);

    // NOTE: S2C Packet can't be handling by main payload register.
      // Used {@link RegisterClientPayloadHandlersEvent }.
      // Only C2S Packet can handle on here.
    registrar.playToClient(
        SyncTaoismDataPayload.TYPE,
        SyncTaoismDataPayload.STREAM_CODEC
    );
  }

  public void onGatherData(GatherDataEvent.Client event) {
    event.createDatapackRegistryObjects(DataGeneratorProvider.BUILDER);
  }
}
