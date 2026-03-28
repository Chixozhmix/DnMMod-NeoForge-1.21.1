package net.chixozhmix.dnmmod.entity.spell.ice_dagger;


import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.AbstractMagicProjectile;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import net.chixozhmix.dnmmod.registers.ModEntityType;
import net.chixozhmix.dnmmod.registers.RegistrySpells;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Optional;

public class IceDagger extends AbstractMagicProjectile {


    public IceDagger(EntityType<? extends IceDagger> entityType, Level level) {
        super(entityType, level);
        this.setNoGravity(true);
    }

    public IceDagger(EntityType<? extends IceDagger> entityType, Level levelIn, LivingEntity shooter) {
        this(entityType, levelIn);
        this.setOwner(shooter);
    }

    public IceDagger(Level levelIn, LivingEntity shooter) {
        this(ModEntityType.ICE_DAGGER.get(), levelIn, shooter);
    }

    @Override
    public void trailParticles() {
        for(int i = 0; i < 1; ++i) {
            double speed = 0.05;
            double dx = Utils.random.nextDouble() * (double)2.0F * speed - speed;
            double dy = Utils.random.nextDouble() * (double)2.0F * speed - speed;
            double dz = Utils.random.nextDouble() * (double)2.0F * speed - speed;
            this.level().addParticle((ParticleOptions)(Utils.random.nextDouble() < 0.3 ? ParticleHelper.SNOWFLAKE : ParticleTypes.SNOWFLAKE), this.getX() + dx, this.getY() + dy, this.getZ() + dz, dx, dy, dz);
        }
    }

    @Override
    public void impactParticles(double v, double v1, double v2) {
        MagicManager.spawnParticles(this.level(), ParticleTypes.SNOWFLAKE, v, v1, v2, 25, (double)0.0F, (double)0.0F, (double)0.0F, 0.18, true);
    }

    @Override
    public float getSpeed() {
        return 2.5f;
    }

    @Override
    public Optional<Holder<SoundEvent>> getImpactSound() {
        return Optional.empty();
    }

    @Override
    protected void onHitBlock(BlockHitResult pResult) {
        super.onHitBlock(pResult);
        explodeAtPosition(pResult.getLocation());
        this.kill();
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        super.onHitEntity(pResult);
        explodeAtPosition(pResult.getLocation());
        this.discard();
    }

    private void explodeAtPosition(Vec3 position) {
        if (this.level().isClientSide) return;

        // Получаем источник урона
        DamageSource damageSource = ((AbstractSpell) RegistrySpells.ICE_DAGGER.get()).getDamageSource(this, this.getOwner());

        // Определяем область взрыва (радиус 3 блока)
        AABB explosionArea = new AABB(
                position.x - 4.0, position.y - 4.0, position.z - 4.0,
                position.x + 4.0, position.y + 4.0, position.z + 4.0
        );

        // Получаем все сущности в области
        List<LivingEntity> entitiesInRange = this.level().getEntitiesOfClass(
                LivingEntity.class,
                explosionArea
        );

        // Наносим урон всем сущностям в радиусе
        for (LivingEntity entity : entitiesInRange) {
            // Можно добавить затухание урона в зависимости от расстояния
            double distance = entity.distanceToSqr(position);
            if (distance <= 16.0) { // 3 блока в квадрате
                // Базовый урон (можно настроить затухание)
                float finalDamage = this.damage * (float)(1.0 - (Math.sqrt(distance) / 3.0) * 0.5);

                DamageSources.applyDamage(entity, finalDamage, damageSource);
            }
        }

        // Спавним частицы взрыва
        impactParticles(position.x, position.y, position.z);
    }

}
