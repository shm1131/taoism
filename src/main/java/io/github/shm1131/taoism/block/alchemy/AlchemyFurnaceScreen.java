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

        super.extractContents(graphics, mouseX, mouseY, partialTick);
    }
}
