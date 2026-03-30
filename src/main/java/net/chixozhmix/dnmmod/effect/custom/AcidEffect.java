package net.chixozhmix.dnmmod.effect.custom;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class AcidEffect extends MobEffect {
    private final float damagePerTick;

    public AcidEffect(MobEffectCategory pCategory, int pColor, float damagePerTick) {
        super(pCategory, pColor);
        this.damagePerTick = damagePerTick;
    }

    @Override
    public boolean applyEffectTick(LivingEntity livingEntity, int amplifier) {
        if (livingEntity.level().getGameTime() % 20 == 0) {
            // Урон увеличивается с уровнем эффекта
            float damage = damagePerTick * (amplifier + 1);
            livingEntity.hurt(livingEntity.damageSources().magic(), damage);
        }

        return super.applyEffectTick(livingEntity, amplifier);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }
}
