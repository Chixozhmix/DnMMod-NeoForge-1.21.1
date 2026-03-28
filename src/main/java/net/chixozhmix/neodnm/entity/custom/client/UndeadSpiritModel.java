package net.chixozhmix.neodnm.entity.custom.client;

import net.chixozhmix.neodnm.DnMMod;
import net.chixozhmix.neodnm.entity.custom.UndeadSpiritEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class UndeadSpiritModel extends GeoModel<UndeadSpiritEntity> {
    @Override
    public ResourceLocation getModelResource(UndeadSpiritEntity undeadSpiritEntity) {
        return ResourceLocation.fromNamespaceAndPath(DnMMod.MOD_ID, "geo/undead_spirit_geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(UndeadSpiritEntity undeadSpiritEntity) {
        return ResourceLocation.fromNamespaceAndPath(DnMMod.MOD_ID, "textures/entity/undead_spirit.png");
    }

    @Override
    public ResourceLocation getAnimationResource(UndeadSpiritEntity undeadSpiritEntity) {
        return ResourceLocation.fromNamespaceAndPath(DnMMod.MOD_ID, "animations/undead_spirit_animation.json");
    }

    @Override
    public void setCustomAnimations(UndeadSpiritEntity animatable, long instanceId, AnimationState<UndeadSpiritEntity> animationState) {
        GeoBone head = getAnimationProcessor().getBone("head");

        if( head != null) {
            EntityModelData entityModelData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);

            head.setRotX(entityModelData.headPitch() * Mth.DEG_TO_RAD);
            head.setRotY(entityModelData.netHeadYaw() * Mth.DEG_TO_RAD);
        }
    }
}
