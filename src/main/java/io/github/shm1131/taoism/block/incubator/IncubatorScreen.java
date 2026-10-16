package io.github.shm1131.taoism.block.incubator;

import io.github.shm1131.taoism.TaoismMain;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

public class IncubatorScreen extends AbstractContainerScreen<IncubatorMenu> {

    private static final Identifier TEXTURE =
        Identifier.fromNamespaceAndPath(TaoismMain.MODID, "textures/gui/container/incubator.png");

    private static final int TEXTURE_WIDTH = 256;
    private static final int TEXTURE_HEIGHT = 256;
    private static final int IMAGE_WIDTH = 176;
    private static final int IMAGE_HEIGHT = 222;

    private static final int PROGRESS_X = 80;
    private static final int PROGRESS_Y = 73;
    private static final int PROGRESS_U = 181;
    private static final int PROGRESS_V = 2;
    private static final int PROGRESS_WIDTH = 15;
    private static final int PROGRESS_HEIGHT = 13;

    private static final int WATER_X = 105;
    private static final int WATER_Y = 57;
    private static final int WATER_U = 186;
    private static final int WATER_V = 23;
    private static final int WATER_WIDTH = 7;
    private static final int WATER_HEIGHT = 43;

    public IncubatorScreen(IncubatorMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, IMAGE_WIDTH, IMAGE_HEIGHT);
        this.titleLabelY = 8;
        this.inventoryLabelY = 129;
    }

    @Override
    public void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        // 绘制背景
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE,
            this.leftPos, this.topPos,
            0.0F, 0.0F,
            IMAGE_WIDTH, IMAGE_HEIGHT,
            TEXTURE_WIDTH, TEXTURE_HEIGHT
        );

        int totalTime = this.menu.getMaxProgress();
        if (totalTime > 0) {
            int progress = this.menu.getProgress();
            int filledHeight = (int) (PROGRESS_HEIGHT * ((float) progress / totalTime));
            if (filledHeight > 0) {
                graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE,
                    this.leftPos + PROGRESS_X,
                    this.topPos + PROGRESS_Y + (PROGRESS_HEIGHT - filledHeight),
                    (float) PROGRESS_U,
                    (float) (PROGRESS_V + (PROGRESS_HEIGHT - filledHeight)),
                    PROGRESS_WIDTH,
                    filledHeight,
                    TEXTURE_WIDTH, TEXTURE_HEIGHT
                );
            }
        }

        int waterAmount = this.menu.getWaterAmount();
        int waterCapacity = this.menu.getWaterCapacity();
        if (waterCapacity > 0 && waterAmount > 0) {
            int filledWaterHeight = (int) (WATER_HEIGHT * ((float) waterAmount / waterCapacity));
            if (filledWaterHeight > 0) {

                graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE,
                    this.leftPos + WATER_X,
                    this.topPos + WATER_Y + (WATER_HEIGHT - filledWaterHeight),
                    (float) WATER_U,
                    (float) (WATER_V + (WATER_HEIGHT - filledWaterHeight)),
                    WATER_WIDTH,
                    filledWaterHeight,
                    TEXTURE_WIDTH, TEXTURE_HEIGHT
                );
            }
        }

        super.extractContents(graphics, mouseX, mouseY, partialTick);

        if (isHovering(WATER_X, WATER_Y, WATER_WIDTH, WATER_HEIGHT, mouseX, mouseY)) {
            graphics.setTooltipForNextFrame(
                Component.literal(waterAmount + " / " + waterCapacity + " mB"),
                mouseX, mouseY
            );
        }
    }
}
