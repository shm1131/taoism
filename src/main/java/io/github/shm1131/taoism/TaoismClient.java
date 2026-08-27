package io.github.shm1131.taoism;

import io.github.shm1131.taoism.entity.text.TextEntityModel;
import io.github.shm1131.taoism.entity.text.TextEntityRenderer;
import io.github.shm1131.taoism.init.EntityRegister;
import io.github.shm1131.taoism.client.screen.AttachmentScreen;
import io.github.shm1131.taoism.client.screen.JingLiHudRenderer;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

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
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EntityRegister.TEXT_ENTITY.get(), TextEntityRenderer::new);
    }

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(TextEntityModel.LAYER_LOCATION, TextEntityModel::createBodyLayer);
    }
}
