package io.github.shm1131.taoism.client.screen;

import io.github.shm1131.taoism.TaoismMain;
import io.github.shm1131.taoism.client.data.ClientJingLiCache;
import io.github.shm1131.taoism.datagen.dim.ModLevelStems;
import io.github.shm1131.taoism.player.attachment.api.IJingLiData;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.GameType;

public class JingLiHudRenderer {
    private static final String NS = TaoismMain.MODID;

    private static final Identifier FULL  = Identifier.fromNamespaceAndPath(NS, "textures/gui/sprite/hud/full_energy.png");
    private static final Identifier HALF  = Identifier.fromNamespaceAndPath(NS, "textures/gui/sprite/hud/half_energy.png");
    private static final Identifier EMPTY = Identifier.fromNamespaceAndPath(NS, "textures/gui/sprite/hud/empty_energy.png");

    private static final int ICON_SIZE = 9;

    public static void render(GuiGraphicsExtractor guiGraphics, DeltaTracker deltaTracker) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.options.hideGui) return;

        GameType gameType = mc.player.connection.getPlayerInfo(mc.player.getUUID()) != null
            ? mc.player.connection.getPlayerInfo(mc.player.getUUID()).getGameMode()
            : mc.player.gameMode();
        if (gameType == GameType.CREATIVE || gameType == GameType.SPECTATOR) return;

        if (mc.player.level().dimension().equals(ModLevelStems.TAOISM_REALM_KEY)) return;

        IJingLiData data = ClientJingLiCache.get();
        float totalValue = data.getJingLiEat() + data.getJingLiSleep();
        float fullCount = totalValue / 2;
        boolean hasHalf = (totalValue % 2) != 0;

        int screenWidth = guiGraphics.guiWidth();
        int screenHeight = guiGraphics.guiHeight();

        int startX = screenWidth / 2 - 91;
        int y = screenHeight - 49;
        float totalSlots = IJingLiData.TOTAL_JING_LI / 2;

        for (int i = 0; i < totalSlots; i++) {
            drawIcon(guiGraphics, EMPTY, startX, y, i);
        }

        int slotIndex = 0;

        for (int i = 0; i < fullCount && slotIndex < totalSlots; i++, slotIndex++) {
            drawIcon(guiGraphics, FULL, startX, y, slotIndex);
        }

        if (hasHalf && slotIndex < totalSlots) {
            drawIcon(guiGraphics, HALF, startX, y, slotIndex);
        }
    }

    private static void drawIcon(GuiGraphicsExtractor g, Identifier tex, int startX, int y, int slotIndex) {
        int x = startX + slotIndex * 8;
        g.blit(RenderPipelines.GUI_TEXTURED, tex, x, y, 0, 0, ICON_SIZE, ICON_SIZE, 256, 256);
    }
}
