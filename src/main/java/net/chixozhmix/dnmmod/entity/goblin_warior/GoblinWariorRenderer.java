package net.chixozhmix.dnmmod.entity.goblin_warior;

import com.mojang.blaze3d.vertex.PoseStack;
import net.chixozhmix.dnmmod.DnMMod;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class GoblinWariorRenderer extends GeoEntityRenderer<GoblinWariorEntity> {
    public GoblinWariorRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new GoblinWariorModel());
    }

    @Override
    public ResourceLocation getTextureLocation(GoblinWariorEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(DnMMod.MOD_ID, "textures/entity/goblin_warior.png");
    }

    @Override
    public void scaleModelForRender(float widthScale, float heightScale, PoseStack poseStack, GoblinWariorEntity animatable, BakedGeoModel model, boolean isReRender, float partialTick, int packedLight, int packedOverlay) {
        float scale = 0.6F;
        poseStack.scale(scale, scale, scale);
    }
}
