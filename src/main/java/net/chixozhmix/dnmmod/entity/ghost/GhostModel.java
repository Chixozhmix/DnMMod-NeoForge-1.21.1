package net.chixozhmix.dnmmod.entity.ghost;

import net.chixozhmix.dnmmod.DnMMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class GhostModel extends GeoModel<GhostEntity> {
    @Override
    public ResourceLocation getModelResource(GhostEntity ghostEntity) {
        return ResourceLocation.fromNamespaceAndPath(DnMMod.MOD_ID, "geo/ghost_geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(GhostEntity ghostEntity) {
        return ResourceLocation.fromNamespaceAndPath(DnMMod.MOD_ID, "textures/entity/ghost/ghost.png");
    }

    @Override
    public ResourceLocation getAnimationResource(GhostEntity ghostEntity) {
        return ResourceLocation.fromNamespaceAndPath(DnMMod.MOD_ID, "animations/ghost_animation.json");
    }

    @Override
    public void setCustomAnimations(GhostEntity animatable, long instanceId, AnimationState<GhostEntity> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);

        GeoBone head = getAnimationProcessor().getBone("head");

        if( head != null) {
            EntityModelData entityModelData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);

            head.setRotX(entityModelData.headPitch() * Mth.DEG_TO_RAD);
            head.setRotY(entityModelData.netHeadYaw() * Mth.DEG_TO_RAD);
        }
    }
}
