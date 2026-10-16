package io.github.shm1131.taoism.entity.text;

import io.github.shm1131.taoism.TaoismMain;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.Identifier;

public class TextEntityModel extends EntityModel<TextEntityRenderState> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
        Identifier.fromNamespaceAndPath(TaoismMain.MODID, "modentity"), "main"
    );

    private final ModelPart bbMain;

    public TextEntityModel(ModelPart root) {
        super(root);
        this.bbMain = root.getChild("bb_main");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition root = meshDefinition.getRoot();

        root.addOrReplaceChild("bb_main",
            CubeListBuilder.create()
                .texOffs(0, 0)
                .addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, CubeDeformation.NONE),
            PartPose.offset(0.0F, 24.0F, 0.0F)
        );

        return LayerDefinition.create(meshDefinition, 16, 16);
    }

    @Override
    public void setupAnim(TextEntityRenderState state) {
        super.setupAnim(state);
    }
}
