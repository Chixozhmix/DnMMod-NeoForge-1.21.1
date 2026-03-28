package net.chixozhmix.dnmmod.entity.green_hag;

import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMobModel;
import net.chixozhmix.dnmmod.DnMMod;
import net.minecraft.resources.ResourceLocation;

public class GreenHagModel extends AbstractSpellCastingMobModel {

    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(DnMMod.MOD_ID, "textures/entity/green_hag.png");
    private static final ResourceLocation MODEL = ResourceLocation.fromNamespaceAndPath(DnMMod.MOD_ID, "geo/green_hag_geo.json");

    public GreenHagModel() {

    }

    @Override
    public ResourceLocation getModelResource(AbstractSpellCastingMob object) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(AbstractSpellCastingMob abstractSpellCastingMob) {
        return TEXTURE;
    }
}
