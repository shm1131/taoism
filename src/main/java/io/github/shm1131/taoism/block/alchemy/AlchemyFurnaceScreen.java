package io.github.shm1131.taoism.block.alchemy;

import io.github.shm1131.taoism.TaoismMain;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

public class AlchemyFurnaceScreen extends AbstractContainerScreen<AlchemyFurnaceMenu> {

    private static final Identifier TEXTURE =
        Identifier.fromNamespaceAndPath(TaoismMain.MODID, "textures/gui/container/alchemy_furnace.png");

    private static final int TEXTURE_WIDTH = 256;
    private static final int TEXTURE_HEIGHT = 256;

    private static final int IMAGE_WIDTH = 176;
    private static final int IMAGE_HEIGHT = 222;

    private static final int BURN_ICON_X = 84;
    private static final int BURN_ICON_Y = 93;
    private static final int BURN_U = 177;
    private static final int BURN_V = 0;
    private static final int BURN_WIDTH = 11;
    private static final int BURN_HEIGHT = 12;

    private static final int PROGRESS_BAR_X = 52;
    private static final int PROGRESS_BAR_Y = 123;
    private static final int PROGRESS_U = 94;
    private static final int PROGRESS_V = 222;
    private static final int PROGRESS_WIDTH = 70;
    private static final int PROGRESS_HEIGHT = 5;

    public AlchemyFurnaceScreen(AlchemyFurnaceMenu menu, Inventory playerInventory, Component title) {
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

        float fuelFraction = this.menu.getFuelFraction();
        if (fuelFraction > 0.0F) {
            int burnHeight = Math.round(BURN_HEIGHT * fuelFraction);
            int vOffset = BURN_HEIGHT - burnHeight;
            graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE,
                this.leftPos + BURN_ICON_X,
                this.topPos + BURN_ICON_Y + vOffset,
                (float) BURN_U,
                (float) (BURN_V + vOffset),
                BURN_WIDTH,
                burnHeight,
                TEXTURE_WIDTH, TEXTURE_HEIGHT
            );
        }

        float progressFraction = this.menu.getBurnProgressFraction();
        if (progressFraction > 0.0F) {
            int barWidth = Math.round(PROGRESS_WIDTH * progressFraction);
            graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE,
                this.leftPos + PROGRESS_BAR_X,
                this.topPos + PROGRESS_BAR_Y,
                (float) PROGRESS_U,
                (float) PROGRESS_V,
                barWidth,
                PROGRESS_HEIGHT,
                TEXTURE_WIDTH, TEXTURE_HEIGHT
            );
        }

        super.extractContents(graphics, mouseX, mouseY, partialTick);
    }
}
