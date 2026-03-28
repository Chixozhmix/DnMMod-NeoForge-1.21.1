package net.chixozhmix.dnmmod.entity.leshy;

import com.mojang.blaze3d.vertex.PoseStack;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.cache.object.BakedGeoModel;

public class LeshyRenderer extends AbstractSpellCastingMobRenderer {
    public LeshyRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new LeshyModel());
    }

    @Override
    public void scaleModelForRender(float widthScale, float heightScale, PoseStack poseStack, AbstractSpellCastingMob animatable, BakedGeoModel model, boolean isReRender, float partialTick, int packedLight, int packedOverlay) {
        float scale = 1.2F;
        poseStack.scale(scale, scale, scale);
    }
}
