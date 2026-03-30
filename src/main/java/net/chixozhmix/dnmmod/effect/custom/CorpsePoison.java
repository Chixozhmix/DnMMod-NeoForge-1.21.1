package net.chixozhmix.dnmmod.effect.custom;

import io.redspace.ironsspellbooks.util.ParticleHelper;
import net.chixozhmix.dnmmod.registers.ModEffects;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;

import java.util.List;


@EventBusSubscriber
public class CorpsePoison extends MobEffect {
    private final float damage;

    public CorpsePoison(float damage) {
        super(MobEffectCategory.HARMFUL, 0x0c5017);
        this.damage = damage;
    }

    @Override
    public boolean applyEffectTick(LivingEntity livingEntity, int amplifier) {
        if(livingEntity.level().getGameTime() % 20 == 0) {
            float applyDamage = damage * (amplifier + 1);
            livingEntity.hurt(livingEntity.damageSources().magic(), applyDamage);
        }

        return super.applyEffectTick(livingEntity, amplifier);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }

    @SubscribeEvent
    public static void poisonExploer(LivingDeathEvent event) {
        LivingEntity entity = event.getEntity();
        MobEffectInstance effect = entity.getEffect(ModEffects.CORPSE_POISON);

        if(effect != null) {
            Level level = entity.level();
            if (!level.isClientSide()) {
                // Отправляем пакет частиц на клиент
                ((ServerLevel) level).sendParticles(
                        ParticleHelper.POISON_CLOUD,  // Тип частиц для эффектов зелий
                        entity.getX(),                // X координата
                        entity.getY() + entity.getBbHeight() / 2,  // Центр существа
                        entity.getZ(),                // Z координата
                        20,                           // Количество частиц
                        entity.getBbWidth(),          // Разброс по X
                        entity.getBbHeight() / 2,     // Разброс по Y
                        entity.getBbWidth(),          // Разброс по Z
                        0.5                           // Скорость
                );
            }

            List<LivingEntity> livingEntities = entity.level().getEntitiesOfClass(
                    LivingEntity.class,
                    entity.getBoundingBox().inflate(5),
                    e -> e != entity && e.isAlive()
            );

            for (LivingEntity living : livingEntities) {
                living.addEffect(new MobEffectInstance(
                        MobEffects.POISON,
                        100,
                        1,
                        false,
                        true,
                        true
                ));
            }
        }

    }
}
