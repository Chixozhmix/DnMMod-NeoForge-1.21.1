package net.chixozhmix.dnmmod.entity.storm_atronach;

import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.entity.mobs.IMagicSummon;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.entity.mobs.goals.*;
import io.redspace.ironsspellbooks.util.OwnerHelper;
import net.chixozhmix.dnmmod.registers.ModEntityType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;
import java.util.UUID;

public class StormAtronach extends AbstractSpellCastingMob implements IMagicSummon, GeoEntity {
    private static final EntityDataAccessor<Boolean> DATA_IS_ANIMATING_RISE = SynchedEntityData.defineId(StormAtronach.class, EntityDataSerializers.BOOLEAN);

    private static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("idle");
    private static final RawAnimation WALK_ANIM = RawAnimation.begin().thenLoop("walk");
    private static final RawAnimation RISE_ANIM = RawAnimation.begin().thenPlay("rise");

    protected LivingEntity cachedSummoner;
    protected UUID summonerUUID;
    private int riseAnimTime;
    private final AnimatableInstanceCache cache;

    private final AnimationController<StormAtronach> movementController;
    private final AnimationController<StormAtronach> riseController;

    public StormAtronach(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);

        this.riseAnimTime = 40;
        this.cache = GeckoLibUtil.createInstanceCache(this);
        this.xpReward = 0;

        this.movementController = new AnimationController<>(this, "movement", 0, this::movementPredicate);
        this.riseController = new AnimationController<>(this, "rise", 0, this::risePredicate);
    }

    public StormAtronach(Level level, LivingEntity owner, boolean playRiseAnimation) {
        this(ModEntityType.STORM_ATRONACH.get(), level);
        if (playRiseAnimation) {
            this.triggerRiseAnimation();
        }
    }

    public static AttributeSupplier.Builder prepareAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.ATTACK_DAMAGE, (double)4.0F)
                .add(Attributes.ATTACK_KNOCKBACK, (double)0.0F)
                .add(Attributes.MAX_HEALTH, (double)50.0F)
                .add(Attributes.FOLLOW_RANGE, (double)25.0F)
                .add(AttributeRegistry.SPELL_POWER, (double)0.5F)
                .add(Attributes.MOVEMENT_SPEED, (double)0.25F);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
        super.defineSynchedData(pBuilder);
        pBuilder.define(DATA_IS_ANIMATING_RISE, false);
    }

    @Override
    public boolean isAlliedTo(Entity pEntity) {
        return super.isAlliedTo(pEntity) || this.isAlliedHelper(pEntity);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(movementController);
        controllerRegistrar.add(riseController);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    private PlayState movementPredicate(AnimationState<StormAtronach> state) {
        if (this.isAnimatingRise() || this.isCasting())
            return PlayState.STOP;


        if (state.isMoving()) {
            state.getController().setAnimation(WALK_ANIM);
            return PlayState.CONTINUE;
        }

        state.getController().setAnimation(IDLE_ANIM);
        return PlayState.CONTINUE;
    }

    private PlayState risePredicate(AnimationState<StormAtronach> state) {
        if (!this.isAnimatingRise()) {
            return PlayState.STOP;
        }

        if (state.getController().getAnimationState() == AnimationController.State.STOPPED) {
            state.getController().setAnimation(RISE_ANIM);
        }

        return PlayState.CONTINUE;
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        return pSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY) || !this.isAnimatingRise() && !this.shouldIgnoreDamage(pSource) ? super.hurt(pSource, pAmount) : false;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, (new WizardAttackGoal(this, (double)1.25F, 20, 40))
                .setSpells(
                        List.of(SpellRegistry.LIGHTNING_BOLT_SPELL.get(),
                                SpellRegistry.ELECTROCUTE_SPELL.get()),
                        List.of(SpellRegistry.SHOCKWAVE_SPELL.get()),
                        List.of(),
                        List.of()));
        this.goalSelector.addGoal(7, new GenericFollowOwnerGoal(this, this::getSummoner, (double)1.0F, 9.0F, 4.0F, false, 25.0F));
        this.goalSelector.addGoal(8, new WaterAvoidingRandomStrollGoal(this, (double)1.0F));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.targetSelector.addGoal(1, new GenericOwnerHurtByTargetGoal(this, this::getSummoner));
        this.targetSelector.addGoal(2, new GenericOwnerHurtTargetGoal(this, this::getSummoner));
        this.targetSelector.addGoal(3, new GenericCopyOwnerTargetGoal(this, this::getSummoner));
        this.targetSelector.addGoal(4, (new GenericHurtByTargetGoal(this, (entity) -> entity == this.getSummoner())).setAlertOthers(new Class[0]));
    }

    @Override
    public boolean fireImmune() {
        return true;
    }

    @Override
    public void tick() {
        super.tick();

        if (this.isAnimatingRise()) {

            if (--this.riseAnimTime < 0) {
                this.entityData.set(DATA_IS_ANIMATING_RISE, false);
                this.setXRot(0.0F);
                this.setOldPosAndRot();
            }
            return;
        }
    }

    @Override
    public double getTick(Object entity) {
        return (double) this.tickCount;
    }

    @Override
    public void die(DamageSource pDamageSource) {
        this.onDeathHelper();
        super.die(pDamageSource);
    }

    @Override
    public void onRemovedFromLevel() {
        this.onRemovedHelper(this);
        super.onRemovedFromLevel();
    }

    @Override
    public void remove(RemovalReason pReason) {
        super.remove(pReason);
    }

    @Override
    public void onUnSummon() {
        if (!this.level().isClientSide) {
            MagicManager.spawnParticles(this.level(), ParticleTypes.ELECTRIC_SPARK, this.getX(), this.getY(), this.getZ(),
                    50, 0.4, 0.8, 0.4, 0.03, true);
            this.discard();
        }
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return false;
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.summonerUUID = OwnerHelper.deserializeOwner(pCompound);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        OwnerHelper.serializeOwner(pCompound, this.summonerUUID);
    }

    public boolean isAnimatingRise() {
        return this.entityData.get(DATA_IS_ANIMATING_RISE);
    }

    public void triggerRiseAnimation() {
        this.entityData.set(DATA_IS_ANIMATING_RISE, true);
    }

    @Override
    public boolean isPushable() {
        return super.isPushable() && !this.isAnimatingRise();
    }

    @Override
    protected boolean isImmobile() {
        return super.isImmobile() || this.isAnimatingRise();
    }

    @Override
    public boolean shouldBeExtraAnimated() {
        return this.isCasting();
    }

    @Override
    public boolean addEffect(MobEffectInstance pEffectInstance, @Nullable Entity pEntity) {
        if(pEffectInstance.getEffect() == MobEffects.POISON) //|| pEffectInstance.getEffect() == ModEffects.CORPSE_POISON.get())
            return false;

        return super.addEffect(pEffectInstance, pEntity);
    }
}
