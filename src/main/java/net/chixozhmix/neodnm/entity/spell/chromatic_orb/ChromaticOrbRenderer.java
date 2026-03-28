package net.chixozhmix.neodnm.entity.spell.chromatic_orb;

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

public class ChromaticOrbRenderer extends EntityRenderer<ChromaticOrb> {

    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(DnMMod.MOD_ID, "textures/entity/chromatic_orb.png");
    public static final ModelLayerLocation MODEL_LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(DnMMod.MOD_ID, "chromatic_orb_model"), "main");
    private final ModelPart body;

    public ChromaticOrbRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
        ModelPart modelpart = pContext.bakeLayer(MODEL_LAYER_LOCATION);
        this.body = modelpart.getChild("body");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();


        partdefinition.addOrReplaceChild("body", CubeListBuilder.create()
                .texOffs(0, 0)
                .addBox(-1.0F, -1.0F, -12.0F,
                        6.0F, 6.0F, 12.0F), PartPose.ZERO);

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @Override
    public void render(ChromaticOrb pEntity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        super.render(pEntity, pEntityYaw, pPartialTick, pPoseStack, pBuffer, pPackedLight);

        pPoseStack.pushPose();

        // Вычисление вращения на основе движения снаряда
        Vec3 motion = pEntity.getDeltaMovement();
        float xRot = -((float)(Mth.atan2(motion.horizontalDistance(), motion.y) * (double)(180F / (float)Math.PI)) - 90.0F);
        float yRot = -((float)(Mth.atan2(motion.z, motion.x) * (double)(180F / (float)Math.PI)) + 90.0F);

        // Применение вращения
        pPoseStack.mulPose(Axis.YP.rotationDegrees(yRot));
        pPoseStack.mulPose(Axis.XP.rotationDegrees(xRot));

        // Небольшое вращение вокруг оси для эффекта полета
        float spin = (pEntity.tickCount + pPartialTick) * 20F;
        pPoseStack.mulPose(Axis.ZP.rotationDegrees(spin));

        // Масштабирование если нужно
        pPoseStack.scale(0.8F, 0.8F, 0.8F);

        VertexConsumer consumer = pBuffer.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));

        // Рендеринг всех частей меча
        this.body.render(pPoseStack, consumer, pPackedLight, OverlayTexture.NO_OVERLAY);

        pPoseStack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(ChromaticOrb chromaticOrb) {
        return ResourceLocation.fromNamespaceAndPath(DnMMod.MOD_ID, "textures/entity/chromatic_orb.png");
    }
}
