package net.chixozhmix.dnmmod.entity.flame_atronach;

import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class FlameAtronachrenderer  extends AbstractSpellCastingMobRenderer {
    public FlameAtronachrenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new FlameAtronachModel());
    }
}
