package net.chixozhmix.neodnm.entity.leshy;

import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMobModel;
import net.minecraft.resources.ResourceLocation;

public class LeshyModel extends AbstractSpellCastingMobModel {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath("dnmmod", "textures/entity/leshy.png");
    private static final ResourceLocation MODEL = ResourceLocation.fromNamespaceAndPath("dnmmod", "geo/leshy_geo.json");

    public LeshyModel() {

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
