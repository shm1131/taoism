package io.github.shm1131.taoism;

import io.github.shm1131.taoism.network.SyncCultivationDataPayload;
import io.github.shm1131.taoism.network.SyncTaoismDataPayload;
import io.github.shm1131.taoism.network.handler.NetworkHandlerClient;
import io.github.shm1131.taoism.player.screen.AttachmentScreen;
import io.github.shm1131.taoism.player.screen.JingLiHudRenderer;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RegisterTextureAtlasesEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.client.network.event.RegisterClientPayloadHandlersEvent;

import static io.github.shm1131.taoism.TaoismMain.MODID;

@Mod(value = MODID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = MODID, value = Dist.CLIENT)
public class TaoismClient {
    public TaoismClient(ModContainer container) {

        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    public static void onRegisterLayers(RegisterGuiLayersEvent event) {
        event.registerAboveAll(
            Identifier.fromNamespaceAndPath(MODID, "attachment_screen"),
            new AttachmentScreen()
        );

        event.registerAbove(
            VanillaGuiLayers.FOOD_LEVEL,
            Identifier.fromNamespaceAndPath(MODID, "jing_li_screen"),
            JingLiHudRenderer::render
        );
    }

    @SubscribeEvent
    public static void registerClientPayload(final RegisterClientPayloadHandlersEvent event) {
        event.register(SyncTaoismDataPayload.TYPE, NetworkHandlerClient::handle);
        event.register(SyncCultivationDataPayload.TYPE, NetworkHandlerClient::handle);
    }
}
