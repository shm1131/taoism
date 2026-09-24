package io.github.shm1131.taoism.entity.text;

import io.github.shm1131.taoism.TaoismMain;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;

public class TextEntityRenderer extends MobRenderer<TextEntity, TextEntityRenderState, TextEntityModel> {

    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(
        TaoismMain.MODID, "textures/entity/text_entity.png"
    );

    public TextEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new TextEntityModel(context.bakeLayer(TextEntityModel.LAYER_LOCATION)), 0.5F); // 0.5F = 阴影半径
    }

    @Override
    public Identifier getTextureLocation(TextEntityRenderState state) {
        return TEXTURE;
    }

    @Override
    public TextEntityRenderState createRenderState() {
        return new TextEntityRenderState();
    }

}
