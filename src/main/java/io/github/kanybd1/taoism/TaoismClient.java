package io.github.kanybd1.taoism;

import io.github.kanybd1.taoism.client.manu.AttachmentScreen;
import io.github.kanybd1.taoism.network.ClientPacketHandler;
import io.github.kanybd1.taoism.network.SyncTaoismDataPayload;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.client.network.event.RegisterClientPayloadHandlersEvent;

import static io.github.kanybd1.taoism.TaoismMain.MODID;

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
    }

    @SubscribeEvent
    public static void registerClientPayloadEvent(final RegisterClientPayloadHandlersEvent event) {
        event.register(SyncTaoismDataPayload.TYPE, ClientPacketHandler::handleSyncTaoismDataPayload);
    }
}
