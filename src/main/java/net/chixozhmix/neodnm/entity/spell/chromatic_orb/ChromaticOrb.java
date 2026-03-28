package net.chixozhmix.neodnm.entity.spell.chromatic_orb;

import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.AbstractMagicProjectile;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import net.chixozhmix.neodnm.registers.ModEntityType;
import net.chixozhmix.neodnm.registers.RegistrySpells;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class ChromaticOrb extends AbstractMagicProjectile {
    private int bouncesRemaining = 10;
    private float damage = 5f;

    public ChromaticOrb(EntityType<? extends ChromaticOrb> entityType, Level level) {
        super(entityType, level);
        this.setNoGravity(true);
    }

    public ChromaticOrb(EntityType<? extends ChromaticOrb> entityType, Level levelIn, LivingEntity shooter) {
        this(entityType, levelIn);
        this.setOwner(shooter);
    }

    public ChromaticOrb(Level levelIn, LivingEntity shooter) {
        this(ModEntityType.CHROMATIC_ORB.get(), levelIn, shooter);
    }

    // Метод для установки количества перескакиваний
    public void setBounces(int bounces) {
        this.bouncesRemaining = bounces;
    }

    @Override
    public void trailParticles() {
        for(int i = 0; i < 1; ++i) {
            double speed = 0.05;
            double dx = Utils.random.nextDouble() * (double)2.0F * speed - speed;
            double dy = Utils.random.nextDouble() * (double)2.0F * speed - speed;
            double dz = Utils.random.nextDouble() * (double)2.0F * speed - speed;
            this.level().addParticle((ParticleOptions)(Utils.random.nextDouble() < 0.3 ? ParticleHelper.EMBERS : ParticleHelper.ELECTRIC_SPARKS), this.getX() + dx, this.getY() + dy, this.getZ() + dz, dx, dy, dz);
        }
    }

    @Override
    public void impactParticles(double v, double v1, double v2) {
        MagicManager.spawnParticles(this.level(), ParticleHelper.ELECTRIC_SPARKS, v, v1, v2, 25, (double)0.0F, (double)0.0F, (double)0.0F, 0.18, true);

    }

    @Override
    public float getSpeed() {
        return 1.2f;
    }

    @Override
    public void setDamage(float damage) {
        super.setDamage(damage);

        this.damage = damage;
    }

    @Override
    public Optional<Holder<SoundEvent>> getImpactSound() {
        return Optional.empty();
    }

    @Override
    protected void onHitBlock(BlockHitResult pResult) {
        super.onHitBlock(pResult);
        this.kill();
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        super.onHitEntity(pResult);

        Entity target = pResult.getEntity();
        Entity owner = this.getOwner();

        // Наносим урон текущей цели
        if (owner instanceof LivingEntity livingOwner && target instanceof LivingEntity livingTarget) {
            DamageSource damageSource = ((AbstractSpell) RegistrySpells.CHROMATIC_ORB.get()).getDamageSource(this, this.getOwner());

            boolean damaged = DamageSources.applyDamage(livingTarget, damage, damageSource);

            if (damaged) {
                this.impactParticles(target.getX(), target.getY(), target.getZ());

                // Пытаемся найти следующую цель для перескакивания
                if (bouncesRemaining > 0) {
                    findAndBounceToNextTarget(livingTarget, livingOwner);
                } else {
                    this.kill();
                }
            } else {
                this.kill();
            }
        } else {
            this.kill();
        }
    }

    private void findAndBounceToNextTarget(LivingEntity currentTarget, LivingEntity owner) {
        // Уменьшаем количество оставшихся перескакиваний
        bouncesRemaining--;

        // Ищем ближайшие живые сущности в радиусе 10 блоков
        AABB searchArea = new AABB(
                this.getX() - 10, this.getY() - 10, this.getZ() - 10,
                this.getX() + 10, this.getY() + 10, this.getZ() + 10
        );

        List<LivingEntity> nearbyEntities = this.level().getEntitiesOfClass(
                LivingEntity.class,
                searchArea,
                entity -> isValidNextTarget(entity, currentTarget, owner)
        );

        // Ищем ближайшую цель
        LivingEntity closestTarget = null;
        double closestDistance = Double.MAX_VALUE;

        for (LivingEntity entity : nearbyEntities) {
            double distance = entity.distanceToSqr(this);
            if (distance < closestDistance) {
                closestDistance = distance;
                closestTarget = entity;
            }
        }

        // Если нашли следующую цель - перескакиваем на нее
        if (closestTarget != null) {
            bounceToTarget(closestTarget);
        } else {
            // Если целей больше нет - уничтожаем снаряд
            this.kill();
        }
    }

    private boolean isValidNextTarget(LivingEntity entity, LivingEntity currentTarget, LivingEntity owner) {
        // Проверяем, что сущность:
        // - не является текущей целью
        // - не является владельцем снаряда
        // - жива
        // - не является творческим игроком
        return entity != currentTarget &&
                entity != owner &&
                entity.isAlive() &&
                !(entity instanceof Player player && player.isCreative());
    }

    private void bounceToTarget(LivingEntity nextTarget) {
        // Вычисляем направление к следующей цели
        Vec3 currentPos = this.position();
        Vec3 targetPos = nextTarget.position().add(0, nextTarget.getEyeHeight() * 0.5, 0);
        Vec3 direction = targetPos.subtract(currentPos).normalize();

        // Обновляем позицию и движение снаряда
        this.setPos(currentPos);
        this.setDeltaMovement(direction.multiply(this.getSpeed(), this.getSpeed(), this.getSpeed()));

        // Создаем частицы при перескакивании
        this.impactParticles(currentPos.x, currentPos.y, currentPos.z);

        // Обновляем вращение снаряда
        this.updateRotation();
    }
}
