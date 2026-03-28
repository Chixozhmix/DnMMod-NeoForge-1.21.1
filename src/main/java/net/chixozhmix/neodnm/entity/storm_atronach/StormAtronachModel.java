package net.chixozhmix.neodnm.entity.storm_atronach;

import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMobModel;
import net.chixozhmix.neodnm.DnMMod;
import net.minecraft.resources.ResourceLocation;

public class StormAtronachModel extends AbstractSpellCastingMobModel {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(DnMMod.MOD_ID, "textures/entity/storm_atronach/storm_atronach.png");
    private static final ResourceLocation MODEL = ResourceLocation.fromNamespaceAndPath(DnMMod.MOD_ID, "geo/storm_atronach_geo.json");
    private static final ResourceLocation ANIM = ResourceLocation.fromNamespaceAndPath(DnMMod.MOD_ID, "animations/storm_atronach_animation.json");

    @Override
    public ResourceLocation getTextureResource(AbstractSpellCastingMob abstractSpellCastingMob) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getModelResource(AbstractSpellCastingMob object) {
        return MODEL;
    }

    @Override
    public ResourceLocation getAnimationResource(AbstractSpellCastingMob animatable) {
        return ANIM;
    }
}
