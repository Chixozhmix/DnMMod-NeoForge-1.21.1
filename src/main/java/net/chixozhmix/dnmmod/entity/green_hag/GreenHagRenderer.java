package net.chixozhmix.dnmmod.entity.green_hag;

import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class GreenHagRenderer extends AbstractSpellCastingMobRenderer {
    public GreenHagRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new GreenHagModel());
    }
}
