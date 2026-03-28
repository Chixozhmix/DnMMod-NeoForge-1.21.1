package net.chixozhmix.dnmmod.entity.spell.ray_of_enfeeblement;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.chixozhmix.dnmmod.DnMMod;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class RayOfEnfeeblementRenderer extends EntityRenderer<RayOfEnfeeblement> {
    public static final ModelLayerLocation MODEL_LAYER_LOCATION = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath(DnMMod.MOD_ID, "ray_of_enfeeblement_model"), "main");
    private static final ResourceLocation TEXTURE_CORE =
            ResourceLocation.fromNamespaceAndPath(DnMMod.MOD_ID, "textures/entity/ray_of_enfeeblement/core.png");
    private static final ResourceLocation TEXTURE_OVERLAY =
            ResourceLocation.fromNamespaceAndPath(DnMMod.MOD_ID, "textures/entity/ray_of_enfeeblement/overlay.png");
    private final ModelPart body;

    public RayOfEnfeeblementRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
        ModelPart modelpart = pContext.bakeLayer(MODEL_LAYER_LOCATION);
        this.body = modelpart.getChild("body");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild("body",
                CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, -16.0F, -8.0F, 16.0F, 32.0F, 16.0F),
                PartPose.ZERO);
        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public boolean shouldRender(RayOfEnfeeblement pLivingEntity, Frustum pCamera, double pCamX, double pCamY, double pCamZ) {
        return true;
    }

    @Override
    public void render(RayOfEnfeeblement pEntity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack,
                       MultiBufferSource pBuffer, int pPackedLight) {
        pPoseStack.pushPose();
        float lifetime = 15.0F;
        float scalar = 0.25F;
        float length = 32.0F * scalar * scalar;
        float f = (float) pEntity.tickCount + pPartialTick;

        pPoseStack.translate(0.0F, pEntity.getBoundingBox().getYsize() * 0.5F, 0.0F);
        pPoseStack.mulPose(Axis.YP.rotationDegrees(-pEntity.getYRot() - 180.0F));
        pPoseStack.mulPose(Axis.XP.rotationDegrees(-pEntity.getXRot() - 90.0F));
        pPoseStack.scale(scalar, scalar, scalar);
        float alpha = Mth.clamp(1.0F - f / lifetime, 0.0F, 1.0F);

        for (float i = 0.0F; i < pEntity.distance * 4.0F; i += length) {
            pPoseStack.translate(0.0F, length, 0.0F);

            // Оверлейная текстура
            VertexConsumer overlayConsumer = pBuffer.getBuffer(RenderType.energySwirl(TEXTURE_OVERLAY, 0.0F, 0.0F));
            pPoseStack.pushPose();
            float expansion = Mth.clampedLerp(1.2F, 0.0F, f / lifetime);
            pPoseStack.mulPose(Axis.YP.rotationDegrees(f * 5.0F));
            pPoseStack.scale(expansion, 1.0F, expansion);
            pPoseStack.mulPose(Axis.YP.rotationDegrees(45.0F));

            // Исправленный вызов render с правильными параметрами
            int overlayColor = ((int)(alpha * 255) << 24) | (255 << 16) | (255 << 8) | 255;
            this.body.render(pPoseStack, overlayConsumer, 15728880, OverlayTexture.NO_OVERLAY, overlayColor);
            pPoseStack.popPose();

            // Основная текстура
            VertexConsumer coreConsumer = pBuffer.getBuffer(RenderType.energySwirl(TEXTURE_CORE, 0.0F, 0.0F));
            pPoseStack.pushPose();
            expansion = Mth.clampedLerp(1.0F, 0.0F, f / (lifetime - 8.0F));
            pPoseStack.scale(expansion, 1.0F, expansion);
            pPoseStack.mulPose(Axis.YP.rotationDegrees(f * -10.0F));

            // Исправленный вызов render для основной текстуры
            this.body.render(pPoseStack, coreConsumer, 15728880, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
            pPoseStack.popPose();
        }

        pPoseStack.popPose();
        super.render(pEntity, pEntityYaw, pPartialTick, pPoseStack, pBuffer, pPackedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(RayOfEnfeeblement rayOfEnfeeblement) {
        return TEXTURE_CORE;
    }
}
