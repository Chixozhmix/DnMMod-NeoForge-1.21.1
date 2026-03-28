package net.chixozhmix.neodnm.entity.goblin_warior;

import net.chixozhmix.neodnm.DnMMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class GoblinWariorModel extends GeoModel<GoblinWariorEntity> {
    @Override
    public ResourceLocation getModelResource(GoblinWariorEntity goblinWariorEntity) {
        return ResourceLocation.fromNamespaceAndPath(DnMMod.MOD_ID, "geo/goblin_warior_geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(GoblinWariorEntity goblinWariorEntity) {
        return ResourceLocation.fromNamespaceAndPath(DnMMod.MOD_ID, "textures/entity/goblin_warior.png");
    }

    @Override
    public ResourceLocation getAnimationResource(GoblinWariorEntity goblinWariorEntity) {
        return ResourceLocation.fromNamespaceAndPath(DnMMod.MOD_ID, "animations/goblin_warior_animation.json");
    }

    @Override
    public void setCustomAnimations(GoblinWariorEntity animatable, long instanceId, AnimationState<GoblinWariorEntity> animationState) {
        GeoBone head = getAnimationProcessor().getBone("head");

        if( head != null) {
            EntityModelData entityModelData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);

            head.setRotX(entityModelData.headPitch() * Mth.DEG_TO_RAD);
            head.setRotY(entityModelData.netHeadYaw() * Mth.DEG_TO_RAD);
        }
    }
}
