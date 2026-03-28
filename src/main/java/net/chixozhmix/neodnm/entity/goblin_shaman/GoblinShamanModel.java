package net.chixozhmix.neodnm.entity.goblin_shaman;

import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMobModel;
import net.minecraft.resources.ResourceLocation;

public class GoblinShamanModel extends AbstractSpellCastingMobModel {

    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath("dnmmod", "textures/entity/goblin_shaman.png");
    private static final ResourceLocation MODEL = ResourceLocation.fromNamespaceAndPath("dnmmod", "geo/goblin_shaman_geo.json");

    public GoblinShamanModel(){
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
