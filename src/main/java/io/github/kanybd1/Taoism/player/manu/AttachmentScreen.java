package io.github.kanybd1.Taoism.player.manu;

import io.github.kanybd1.Taoism.player.client.ClientTaoismCache;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.neoforged.neoforge.client.gui.GuiLayer;

public class AttachmentScreen implements GuiLayer {
    @Override
    public void render(GuiGraphicsExtractor guiGraphics, DeltaTracker deltaTracker) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null) return;

        Font font = mc.font;
        int padding = 5;
        int screenWidth = guiGraphics.guiWidth();
        int screenHeight = guiGraphics.guiHeight();

        int shouMing = ClientTaoismCache.get().getShouMing();
        String displayText = "寿命：" + formatTickDuration(shouMing);

        int textWidth = font.width(displayText);
        int x = screenWidth - textWidth - padding;
        int y = screenHeight - font.lineHeight - padding;

        guiGraphics.text(font, displayText, x, y, 0xFFFFFFFF, true);
    }

    private static String formatTickDuration(int ticks) {
        if (ticks <= 0) return "0秒";

        long totalSeconds = ticks / 20L;
        long days = totalSeconds / 86400L;
        long hours = (totalSeconds % 86400L) / 3600L;
        long minutes = (totalSeconds % 3600L) / 60L;
        long seconds = totalSeconds % 60L;

        StringBuilder sb = new StringBuilder();
        if (days > 0) sb.append(days).append("天");
        if (hours > 0) sb.append(hours).append("时");
        if (minutes > 0) sb.append(minutes).append("分");
        if (seconds > 0 || sb.isEmpty()) sb.append(seconds).append("秒");

        return sb.toString();
    }
}