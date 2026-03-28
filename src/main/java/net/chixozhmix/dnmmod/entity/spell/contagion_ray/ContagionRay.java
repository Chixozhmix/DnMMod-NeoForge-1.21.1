package net.chixozhmix.dnmmod.entity.spell.contagion_ray;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.entity.mobs.AntiMagicSusceptible;
import io.redspace.ironsspellbooks.entity.spells.AoeEntity;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import net.chixozhmix.dnmmod.registers.ModEntityType;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class ContagionRay extends AoeEntity implements AntiMagicSusceptible {
    @Nullable LivingEntity target;
    public static final int WARMUP_TIME = 15;

    public ContagionRay(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setRadius((float)(this.getBoundingBox().getXsize() * (double)0.5F));
        this.setNoGravity(true);
    }

    public ContagionRay(Level level) {
        this((EntityType) ModEntityType.CONTAGION_RAY.get(), level);
    }

    @Override
    public void onAntiMagic(MagicData magicData) {
        this.discard();
    }

    @Override
    public void tick() {
        this.setOldPosAndRot();
        if (this.tickCount == 15 && !this.level().isClientSide) {
            this.checkHits();
            MagicManager.spawnParticles(this.level(), ParticleHelper.BLOOD, this.getX(), this.getY() + 0.06, this.getZ(), 25, (double)(this.getRadius() * 0.7F), (double)0.2F, (double)(this.getRadius() * 0.7F), (double)0.6F, true);
        }

        if (this.tickCount > 15) {
            this.discard();
        }
    }

    public void setTarget(@Nullable LivingEntity target) {
        this.target = target;
    }

    @Override
    protected boolean canHitTargetForGroundContext(LivingEntity target) {
        return true;
    }

    @Override
    protected Vec3 getInflation() {
        return new Vec3((double)2.0F, (double)2.0F, (double)2.0F);
    }

    @Override
    public void applyEffect(LivingEntity livingEntity) {

    }

    @Override
    public float getParticleCount() {
        return 0.0F;
    }

    @Override
    public Optional<ParticleOptions> getParticle() {
        return Optional.empty();
    }
}
