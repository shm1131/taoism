package io.github.shm1131.taoism.client.screen;

import io.github.shm1131.taoism.TaoismMain;
import io.github.shm1131.taoism.client.data.ClientJingLiCache;
import io.github.shm1131.taoism.player.attachment.api.IJingLiData;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.GameType;

public class JingLiHudRenderer {
    private static final String NS = TaoismMain.MODID;

    private static final Identifier YANG_ENERGY = Identifier.fromNamespaceAndPath(NS, "textures/gui/sprite/hud/yang_energy.png");
    private static final Identifier YIN_ENERGY = Identifier.fromNamespaceAndPath(NS, "textures/gui/sprite/hud/yin_energy.png");

    private static final Identifier WITHERED_YANG_ENERGY = Identifier.fromNamespaceAndPath(NS, "textures/gui/sprite/hud/withered_yang_energy.png");
    private static final Identifier WITHERED_YIN_ENERGY = Identifier.fromNamespaceAndPath(NS, "textures/gui/sprite/hud/withered_yin_energy.png");

    private static final int ICON_SIZE = 9;
    private static final int ICON_SPACING = 10;
    private static final int TOTAL_SLOTS = 10;

    public static void render(GuiGraphicsExtractor guiGraphics, DeltaTracker deltaTracker) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.options.hideGui) return;

        GameType gameType = mc.player.connection.getPlayerInfo(mc.player.getUUID()) != null
            ? mc.player.connection.getPlayerInfo(mc.player.getUUID()).getGameMode()
            : mc.player.gameMode();
        if (gameType == GameType.CREATIVE || gameType == GameType.SPECTATOR) return;

        IJingLiData data = ClientJingLiCache.get();

        int screenWidth = guiGraphics.guiWidth();
        int screenHeight = guiGraphics.guiHeight();

        int startX = (screenWidth / 2) - ((TOTAL_SLOTS * ICON_SPACING) / 2);
        int y = screenHeight - 49;

        float yinVal = data.getJingLiSleep();
        float yangVal = data.getJingLiEat();

        for (int i = 0; i < TOTAL_SLOTS; i++) {
            int x = startX + i * ICON_SPACING;
            float slotThreshold = (i + 1) * 2.0f;

            blitIcon(guiGraphics, WITHERED_YIN_ENERGY, x, y);
            blitIcon(guiGraphics, WITHERED_YANG_ENERGY, x, y);

            if (yinVal >= slotThreshold) {
                blitIcon(guiGraphics, YIN_ENERGY, x, y);
            }

            if (yangVal >= slotThreshold) {
                blitIcon(guiGraphics, YANG_ENERGY, x, y);
            }
        }
    }

    private static void blitIcon(GuiGraphicsExtractor g, Identifier tex, int x, int y) {
        g.blit(RenderPipelines.GUI_TEXTURED, tex, x, y, 0, 0, ICON_SIZE, ICON_SIZE, ICON_SIZE, ICON_SIZE);
    }
}
