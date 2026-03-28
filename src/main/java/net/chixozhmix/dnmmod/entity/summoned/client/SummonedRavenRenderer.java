package net.chixozhmix.dnmmod.entity.summoned.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.chixozhmix.dnmmod.DnMMod;
import net.chixozhmix.dnmmod.entity.summoned.SummonedRavenEntity;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class SummonedRavenRenderer extends MobRenderer<SummonedRavenEntity, SummonedRavenModel> {
    public static final ModelLayerLocation MODEL_LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(DnMMod.MOD_ID, "summon_raven"), "main");

    public SummonedRavenRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new SummonedRavenModel(pContext.bakeLayer(MODEL_LAYER_LOCATION)), 0.25f);
    }

    @Override
    public ResourceLocation getTextureLocation(SummonedRavenEntity entity) {
        return ResourceLocation.fromNamespaceAndPath(DnMMod.MOD_ID, "textures/entity/raven.png");
    }

    @Override
    protected void scale(SummonedRavenEntity entitylivingbaseIn, @NotNull PoseStack matrixStackIn, float partialTickTime) {
        float f = 1;
        if (entitylivingbaseIn.isBaby()) {
            f *= 0.5f;
            this.shadowRadius = 0.125F;
        } else {
            this.shadowRadius = 0.25F;
        }

        matrixStackIn.scale(f, f, f);
    }
}
