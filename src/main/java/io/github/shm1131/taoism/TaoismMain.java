package io.github.shm1131.taoism;

import com.mojang.serialization.MapCodec;
import io.github.shm1131.taoism.item.herb.base.ModDataComponents;
import io.github.shm1131.taoism.loot.HerbDropModifier;
import io.github.shm1131.taoism.network.SyncJingLiDataPayload;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import io.github.shm1131.taoism.datagen.biomes.BiomeSourceRegister;
import io.github.shm1131.taoism.init.*;
import io.github.shm1131.taoism.network.SyncCultivationDataPayload;
import io.github.shm1131.taoism.network.SyncTaoismDataPayload;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import org.slf4j.Logger;
import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

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
        ItemRegister.ITEMS.register(modEventBus);
        EffectRegister.EFFECTS.register(modEventBus);
        EntityRegister.ENTITIES.register(modEventBus);
        BiomeSourceRegister.BIOME_SOURCES.register(modEventBus);
        CreativeTabRegister.TABS.register(modEventBus);

        TaoismAttachments.ATTACHMENT_TYPES.register(modEventBus);

        GLM_SERIALIZERS.register(modEventBus);
        ModDataComponents.COMPONENTS.register(modEventBus);   //ADDED:注册component

        modEventBus.addListener(this::registerPayloads);

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

        registrar.playToClient(
            SyncJingLiDataPayload.TYPE,
            SyncJingLiDataPayload.STREAM_CODEC
        );


    }

}
