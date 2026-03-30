package net.chixozhmix.dnmmod.effect.custom;

import net.chixozhmix.dnmmod.registers.ModEffects;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

@EventBusSubscriber
public class AgathysArmor extends MobEffect {

    public AgathysArmor() {
        super(MobEffectCategory.BENEFICIAL, 0x32a852);
    }

    @Override
    public void onEffectStarted(LivingEntity entity, int amplifier) {
        entity.setAbsorptionAmount(entity.getAbsorptionAmount() + (float)(amplifier + 1));
        super.onEffectStarted(entity, amplifier);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        return true;
    }

    @SubscribeEvent
    public static void onLivingIncomingDamage(LivingIncomingDamageEvent event) {
        LivingEntity target = event.getEntity();
        DamageSource source = event.getSource();

        // Проверяем наличие эффекта и что атакующий - живое существо
        if (target.hasEffect(ModEffects.AGATHYS_ARMOR) &&
                source.getEntity() instanceof LivingEntity attacker) {

            // Проверяем, является ли атака рукопашной
            if (!isMeleeAttack(source)) {
                return;
            }

            float absorptionHearts = target.getAbsorptionAmount();

            if (absorptionHearts > 0) {
                // Наносим ответный урон, равный половине absorption сердец
                float retaliationDamage = absorptionHearts / 2;
                attacker.hurt(target.damageSources().thorns(target), retaliationDamage);

                // Визуальные и звуковые эффекты
                if (target.level() instanceof ServerLevel serverLevel) {
                    serverLevel.playSound(null, target.getX(), target.getY(), target.getZ(),
                            SoundEvents.SNOW_HIT, target.getSoundSource(), 1.0F, 1.0F);

                    serverLevel.sendParticles(ParticleTypes.SNOWFLAKE,
                            attacker.getX(), attacker.getY() + 1.0, attacker.getZ(),
                            8, 0.5, 0.5, 0.5, 0.1);
                }
            }

            // Если absorption hearts закончились, убираем эффект
            if (absorptionHearts <= 0) {
                target.removeEffect(ModEffects.AGATHYS_ARMOR);
            }
        }
    }

    private static boolean isMeleeAttack(DamageSource source) {
        return source.is(DamageTypes.MOB_ATTACK) ||
                source.is(DamageTypes.PLAYER_ATTACK) ||
                source.is(DamageTypes.MOB_ATTACK_NO_AGGRO);
    }
}
