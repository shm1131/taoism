package io.github.shm1131.taoism.entity.ghost;

import io.github.shm1131.taoism.TaoismMain;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.Identifier;

public class TextGuiEntityModel extends EntityModel<TextGuiEntityRenderState> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
        Identifier.fromNamespaceAndPath(TaoismMain.MODID, "text_gui_entity"), "main"
    );

    private final ModelPart guiRoot;
    private final ModelPart head;
    private final ModelPart rightArm;
    private final ModelPart leftArm;
    private final ModelPart leg;

    public TextGuiEntityModel(ModelPart root) {
        super(root);
        this.guiRoot = root.getChild("Gui");
        this.leg = this.guiRoot.getChild("Leg");
        ModelPart leg1 = this.leg.getChild("Leg1");
        ModelPart leg2 = leg1.getChild("Leg2");
        ModelPart body = leg2.getChild("Body");
        this.rightArm = body.getChild("RightArm");
        this.leftArm = body.getChild("LeftArm");
        this.head = body.getChild("Head");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition Gui = partdefinition.addOrReplaceChild("Gui", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
        PartDefinition Leg = Gui.addOrReplaceChild("Leg", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition Leg1 = Leg.addOrReplaceChild("Leg1", CubeListBuilder.create()
                .texOffs(32, 4).addBox(-1.0F, -1.0F, -1.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
                .texOffs(32, 6).addBox(0.0F, -3.0F, -1.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
                .texOffs(32, 8).addBox(-1.0F, -4.0F, -2.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
                .texOffs(32, 12).addBox(1.0F, -6.0F, -3.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
                .texOffs(32, 10).addBox(-2.0F, -6.0F, 0.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE),
            PartPose.offset(0.0F, -2.0F, 1.0F));

        PartDefinition Leg2 = Leg1.addOrReplaceChild("Leg2", CubeListBuilder.create()
                .texOffs(32, 14).addBox(2.0F, 0.0F, -2.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
                .texOffs(28, 33).addBox(-3.0F, -1.0F, -2.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
                .texOffs(24, 33).addBox(0.0F, -1.0F, 0.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
                .texOffs(28, 35).addBox(-4.0F, -3.0F, -2.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
                .texOffs(24, 35).addBox(3.0F, -3.0F, -2.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
                .texOffs(32, 33).addBox(-1.0F, -3.0F, -1.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
                .texOffs(32, 35).addBox(0.0F, -4.0F, -3.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
                .texOffs(36, 4).addBox(2.0F, -4.0F, -1.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
                .texOffs(36, 6).addBox(-3.0F, -4.0F, 0.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE),
            PartPose.offset(0.0F, -8.0F, 0.0F));

        PartDefinition Body = Leg2.addOrReplaceChild("Body", CubeListBuilder.create()
                .texOffs(0, 29).addBox(-4.0F, -24.0F, -2.0F, 8.0F, 10.0F, 4.0F, CubeDeformation.NONE),
            PartPose.offset(0.0F, 10.0F, -1.0F));

        PartDefinition RightArm = Body.addOrReplaceChild("RightArm", CubeListBuilder.create()
                .texOffs(0, 16).addBox(-8.0F, -24.0F, -7.0F, 4.0F, 4.0F, 9.0F, CubeDeformation.NONE)
                .texOffs(36, 33).addBox(-7.0F, -24.0F, -9.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
                .texOffs(36, 35).addBox(-5.0F, -21.0F, -9.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
                .texOffs(36, 10).addBox(-6.0F, -23.0F, -8.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
                .texOffs(36, 8).addBox(-8.0F, -21.0F, -8.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE),
            PartPose.offset(0.0F, 0.0F, 0.0F));
        RightArm.addOrReplaceChild("cube_r1", CubeListBuilder.create()
                .texOffs(25, 30).addBox(-2.0F, 0.0F, -1.0F, 4.0F, 0.0F, 3.0F, CubeDeformation.NONE),
            PartPose.offsetAndRotation(-6.0F, -18.0F, -6.0F, 1.5708F, 0.0F, 0.0F));

        PartDefinition LeftArm = Body.addOrReplaceChild("LeftArm", CubeListBuilder.create()
                .texOffs(26, 16).addBox(4.0F, -24.0F, -7.0F, 4.0F, 4.0F, 9.0F, CubeDeformation.NONE)
                .texOffs(24, 37).addBox(4.0F, -21.0F, -9.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
                .texOffs(36, 12).addBox(7.0F, -21.0F, -8.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
                .texOffs(28, 37).addBox(6.0F, -24.0F, -9.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
                .texOffs(36, 14).addBox(5.0F, -23.0F, -8.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE),
            PartPose.offset(0.0F, 0.0F, 0.0F));
        LeftArm.addOrReplaceChild("cube_r2", CubeListBuilder.create()
                .texOffs(33, 1).addBox(-2.0F, 0.0F, -1.0F, 4.0F, 0.0F, 3.0F, CubeDeformation.NONE),
            PartPose.offsetAndRotation(6.0F, -18.0F, -6.0F, 1.5708F, 0.0F, 0.0F));

        PartDefinition Head = Body.addOrReplaceChild("Head", CubeListBuilder.create()
                .texOffs(0, 0).addBox(-4.0F, -32.0F, -4.0F, 8.0F, 8.0F, 8.0F, CubeDeformation.NONE),
            PartPose.offset(0.0F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(TextGuiEntityRenderState state) {
        super.setupAnim(state);

        // 基础行走动画：腿部摆动
        float limbSwing = state.walkAnimationPos;
        float limbSwingAmount = state.walkAnimationSpeed;

        // 头部跟随视角
        this.head.yRot = state.yRot * 0.017453292F;
        this.head.xRot = state.xRot * 0.017453292F;

        // 手臂摆动
        this.rightArm.xRot = (float) Math.cos(limbSwing * 0.6662F + Math.PI) * 2.0F * limbSwingAmount * 0.5F;
        this.leftArm.xRot = (float) Math.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F;

    }
}
