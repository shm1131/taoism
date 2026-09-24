package io.github.shm1131.taoism.client.screen;

import io.github.shm1131.taoism.client.data.ClientTaoismCache;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.neoforged.neoforge.client.gui.GuiLayer;

public class AttachmentScreen implements GuiLayer {

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

    @Override
    public void render(GuiGraphicsExtractor guiGraphics, DeltaTracker deltaTracker) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null) return;

        Font font = mc.font;
        int padding = 5;
        int screenWidth = guiGraphics.guiWidth();
        int screenHeight = guiGraphics.guiHeight();

        // 读取数据
        int shouMing = ClientTaoismCache.get().getShouMing();
        int chengFu = ClientTaoismCache.get().getChengFu();

        String shouMingText = "寿命：" + formatTickDuration(shouMing);
        String chengFuText = "承负：" + chengFu;

        int textWidth = Math.max(font.width(shouMingText), font.width(chengFuText));
        int x = screenWidth - textWidth - padding;

        // ⭐ 承负在上，寿命在下，两行间距为 font.lineHeight + 2
        int yShouMing = screenHeight - font.lineHeight - padding;
        int yChengFu = yShouMing - font.lineHeight - 2;

        // 承负使用淡红色(0xFFFFAAAA)以作警示，寿命保持白色
        guiGraphics.text(font, chengFuText, x, yChengFu, 0xFFFFAAAA, true);
        guiGraphics.text(font, shouMingText, x, yShouMing, 0xFFFFFFFF, true);
    }
}
