package net.chixozhmix.dnmmod.entity.flame_atronach;

import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMobModel;
import net.chixozhmix.dnmmod.DnMMod;
import net.minecraft.resources.ResourceLocation;

public class FlameAtronachModel extends AbstractSpellCastingMobModel {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(DnMMod.MOD_ID, "textures/entity/flame_atronach/flame_atronach.png");
    private static final ResourceLocation MODEL = ResourceLocation.fromNamespaceAndPath(DnMMod.MOD_ID, "geo/flame_atronach_geo.json");
    private static final ResourceLocation ANIM = ResourceLocation.fromNamespaceAndPath(DnMMod.MOD_ID, "animations/flame_atronach_animation.json");

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
