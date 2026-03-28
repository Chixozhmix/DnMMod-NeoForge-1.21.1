package net.chixozhmix.dnmmod.entity.spell.acid_projectile;

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
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

import java.util.Optional;

public class AcidProjectile extends AbstractMagicProjectile {
    public AcidProjectile(EntityType<? extends AcidProjectile> entityType, Level level) {
        super(entityType, level);
        this.setNoGravity(true);
    }

    public AcidProjectile(EntityType<? extends AcidProjectile> entityType, Level levelIn, LivingEntity shooter) {
        this(entityType, levelIn);
        this.setOwner(shooter);
    }

    public AcidProjectile(Level levelIn, LivingEntity shooter) {
        this(ModEntityType.ACID_PROJECTILE.get(), levelIn, shooter);
    }

    @Override
    public void trailParticles() {
        for(int i = 0; i < 1; ++i) {
            double speed = 0.05;
            double dx = Utils.random.nextDouble() * (double)2.0F * speed - speed;
            double dy = Utils.random.nextDouble() * (double)2.0F * speed - speed;
            double dz = Utils.random.nextDouble() * (double)2.0F * speed - speed;
            this.level().addParticle((ParticleOptions)(Utils.random.nextDouble() < 0.3 ? ParticleHelper.ACID : ParticleHelper.ACID_BUBBLE), this.getX() + dx, this.getY() + dy, this.getZ() + dz, dx, dy, dz);
        }
    }

    @Override
    public void impactParticles(double v, double v1, double v2) {
        MagicManager.spawnParticles(this.level(), ParticleHelper.ACID, v, v1, v2, 25, (double)0.0F, (double)0.0F, (double)0.0F, 0.18, true);

    }

    @Override
    public float getSpeed() {
        return 1.4f;
    }

    @Override
    public Optional<Holder<SoundEvent>> getImpactSound() {
        return Optional.empty();
    }

    @Override
    protected void onHitBlock(BlockHitResult pResult) {
        super.onHitBlock(pResult);
        this.discard();
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        super.onHitEntity(pResult);

        DamageSource damageSource = ((AbstractSpell) RegistrySpells.CAUSTIC_BREW.get()).getDamageSource(this, this.getOwner());

        if (this.level().isClientSide) return;

        DamageSources.applyDamage(pResult.getEntity(), 8, damageSource);

        if(pResult.getEntity() instanceof LivingEntity livingEntity){
            applyAcidEffect(livingEntity);
        }

    }

    private void applyAcidEffect(LivingEntity target) {
//        MobEffectInstance acidEffect = new MobEffectInstance(
//                ModEffects.ACID.get(),
//                60, // 3 секунды
//                0,   // Уровень эффекта (0 = базовый)
//                false, // Является ли эффектом амбьента
//                true,  // Показывать частицы
//                true   // Показывать иконку
//        );
//        target.addEffect(acidEffect);
    }
}
