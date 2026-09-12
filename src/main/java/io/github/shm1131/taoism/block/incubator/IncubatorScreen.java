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

    private static final int PROGRESS_U = 176, PROGRESS_V = 0;
    private static final int PROGRESS_WIDTH = 64, PROGRESS_HEIGHT = 17;
    private static final int NO_WATER_U = 176, NO_WATER_V = 17, NO_WATER_SIZE = 16;

    public IncubatorScreen(IncubatorMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, IMAGE_WIDTH, IMAGE_HEIGHT);
        this.titleLabelY = 8;
        this.inventoryLabelY = 129;
    }

    @Override
    public void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE,
            this.leftPos, this.topPos,
            0.0F, 0.0F,
            IMAGE_WIDTH, IMAGE_HEIGHT,
            TEXTURE_WIDTH, TEXTURE_HEIGHT
        );

        int totalTime = this.menu.getTotalIncubationTime();
        if (totalTime > 0) {
            int progress = this.menu.getProgress();
            int barWidth = (int) (PROGRESS_WIDTH * ((float) progress / totalTime));
            graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE,
                this.leftPos + 46, this.topPos + 35,
                (float) PROGRESS_U, (float) PROGRESS_V,
                barWidth, PROGRESS_HEIGHT,
                TEXTURE_WIDTH, TEXTURE_HEIGHT
            );
        }

        if (!this.menu.hasWaterSource()) {
            graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE,
                this.leftPos + 51, this.topPos + 53,
                (float) NO_WATER_U, (float) NO_WATER_V,
                NO_WATER_SIZE, NO_WATER_SIZE,
                TEXTURE_WIDTH, TEXTURE_HEIGHT
            );
        }

        super.extractContents(graphics, mouseX, mouseY, partialTick);
    }
}
