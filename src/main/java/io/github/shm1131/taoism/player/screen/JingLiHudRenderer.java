package io.github.shm1131.taoism.player.screen;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import io.github.shm1131.taoism.TaoismMain;
import io.github.shm1131.taoism.player.attachment.api.ITaoismData;
import io.github.shm1131.taoism.player.data.ClientTaoismCache;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;

public class JingLiHudRenderer {
    private static final String NS = TaoismMain.MODID;

    private static final Identifier FULL   = Identifier.fromNamespaceAndPath(NS, "gui/sprite/hud/full_energy.png");
    private static final Identifier HALF   = Identifier.fromNamespaceAndPath(NS, "gui/sprite/hud/half_energy.png");
    private static final Identifier EMPTY   = Identifier.fromNamespaceAndPath(NS,"gui/sprite/hud/empty_energy.png");


    private static final int ICON_SIZE = 9;

    public static void render(GuiGraphicsExtractor guiGraphics, DeltaTracker deltaTracker) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.options.hideGui) return;

        ITaoismData data = ClientTaoismCache.get();
        int eat = data.getJingLiEat();
        int sleep = data.getJingLiSleep();

        int screenWidth = guiGraphics.guiWidth();
        int screenHeight = guiGraphics.guiHeight();

        int startX = screenWidth / 2 + 91;
        int y = screenHeight - 49;

        int totalSlots = ITaoismData.TOTAL_JING_LI / 2;
        int eatFull = eat / 2;
        boolean eatHalf = (eat % 2) != 0;
        int sleepFull = sleep / 2;
        boolean sleepHalf = (sleep % 2) != 0;

        int slotIndex = 0;

        for (int i = 0; i < eatFull && slotIndex < totalSlots; i++, slotIndex++) {
            drawIcon(guiGraphics, FULL, startX, y, slotIndex);
        }
        if (eatHalf && slotIndex < totalSlots) {
            drawIcon(guiGraphics, HALF, startX, y, slotIndex);
            slotIndex++;
        }

        for (int i = 0; i < sleepFull && slotIndex < totalSlots; i++, slotIndex++) {
            drawIcon(guiGraphics, FULL, startX, y, slotIndex);
        }
        if (sleepHalf && slotIndex < totalSlots) {
            drawIcon(guiGraphics, HALF, startX, y, slotIndex);
            slotIndex++;
        }
    }


    private static void drawIcon(GuiGraphicsExtractor g, Identifier tex, int startX, int y, int slotIndex) {
        int x = startX - slotIndex * 8 - ICON_SIZE;
        g.blitSprite(RenderPipelines.GUI_TEXTURED,tex, x, y, ICON_SIZE, ICON_SIZE);
    }
}
