package net.chixozhmix.neodnm.entity.spell.ice_dagger;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.chixozhmix.neodnm.DnMMod;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class IceDaggerRenderer extends EntityRenderer<IceDagger> {
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(DnMMod.MOD_ID, "textures/entity/ice_dagger.png");
    public static final ModelLayerLocation MODEL_LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(DnMMod.MOD_ID, "ice_dagger_model"), "main");
    private final ModelPart blade;
    private final ModelPart guard;
    private final ModelPart handle;

    public IceDaggerRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager);
        ModelPart modelpart = renderManager.bakeLayer(MODEL_LAYER_LOCATION);
        this.blade = modelpart.getChild("blade");
        this.guard = modelpart.getChild("guard");
        this.handle = modelpart.getChild("handle");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        // Клинок меча (лезвие)
        partdefinition.addOrReplaceChild("blade", CubeListBuilder.create()
                .texOffs(0, 0)
                .addBox(-1.0F, -1.0F, -12.0F, 2.0F, 2.0F, 12.0F), PartPose.ZERO);

        // Гарда (перекрестие)
        partdefinition.addOrReplaceChild("guard", CubeListBuilder.create()
                .texOffs(0, 14)
                .addBox(-2.0F, -1.5F, 0.0F, 4.0F, 3.0F, 1.0F), PartPose.ZERO);

        // Рукоять
        partdefinition.addOrReplaceChild("handle", CubeListBuilder.create()
                .texOffs(10, 14)
                .addBox(-1.0F, -1.5F, 1.0F, 2.0F, 3.0F, 4.0F), PartPose.ZERO);

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @Override
    public ResourceLocation getTextureLocation(IceDagger animatable) {
        return ResourceLocation.fromNamespaceAndPath(DnMMod.MOD_ID, "textures/entity/ice_dagger.png");
    }

    @Override
    public void render(IceDagger entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);

        poseStack.pushPose();

        // Вычисление вращения на основе движения снаряда
        Vec3 motion = entity.getDeltaMovement();
        float xRot = -((float)(Mth.atan2(motion.horizontalDistance(), motion.y) * (double)(180F / (float)Math.PI)) - 90.0F);
        float yRot = -((float)(Mth.atan2(motion.z, motion.x) * (double)(180F / (float)Math.PI)) + 90.0F);

        // Применение вращения
        poseStack.mulPose(Axis.YP.rotationDegrees(yRot));
        poseStack.mulPose(Axis.XP.rotationDegrees(xRot));

        // Небольшое вращение вокруг оси для эффекта полета
        float spin = (entity.tickCount + partialTick) * 20F;
        poseStack.mulPose(Axis.ZP.rotationDegrees(spin));

        // Масштабирование если нужно
        poseStack.scale(0.8F, 0.8F, 0.8F);

        VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));

        // Рендеринг всех частей меча
        this.blade.render(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY);
        this.guard.render(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY);
        this.handle.render(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY);

        poseStack.popPose();
    }
}
