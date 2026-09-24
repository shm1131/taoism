package io.github.shm1131.taoism.entity.ghost;

import io.github.shm1131.taoism.TaoismMain;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;

public class TextGuiEntityRenderer extends MobRenderer<TextGuiEntity, TextGuiEntityRenderState, TextGuiEntityModel> {

    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(
        TaoismMain.MODID, "textures/entity/gui.png"
    );

    public TextGuiEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new TextGuiEntityModel(context.bakeLayer(TextGuiEntityModel.LAYER_LOCATION)), 0.5F);
    }

    @Override
    public Identifier getTextureLocation(TextGuiEntityRenderState state) {
        return TEXTURE;
    }

    @Override
    public TextGuiEntityRenderState createRenderState() {
        return new TextGuiEntityRenderState();
    }
}
