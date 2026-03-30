package net.chixozhmix.dnmmod.entity.greemon;

import net.chixozhmix.dnmmod.registers.ModEffects;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

public class GreemonEntity extends Monster implements GeoEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("idle");
    private static final RawAnimation WALK_ANIM = RawAnimation.begin().thenLoop("walk");
    private static final RawAnimation ATTACK_ANIM = RawAnimation.begin().thenPlay("attack");

    private int attackAnimationTick = 0;

    public GreemonEntity(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.xpReward = 10;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 5, this::predicate));
    }

    private <T extends GeoAnimatable> PlayState predicate(AnimationState<T> state) {

        if (this.attackAnimationTick > 0) {
            return state.setAndContinue(ATTACK_ANIM);
        }

        else if (state.isMoving()) {
            return state.setAndContinue(WALK_ANIM);
        }

        return state.setAndContinue(IDLE_ANIM);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    public static AttributeSupplier createAttributes () {
        return Monster.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 40.0D)
                .add(Attributes.ARMOR, 6.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.30D)
                .add(Attributes.ATTACK_DAMAGE, 4.0D)
                .add(Attributes.FOLLOW_RANGE, 40.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.2D).build();
    }

    @Override
    public boolean addEffect(MobEffectInstance pEffectInstance, @Nullable Entity pEntity) {
        if(pEffectInstance.getEffect() == MobEffects.POISON || pEffectInstance.getEffect() == ModEffects.CORPSE_POISON)
            return false;

        return super.addEffect(pEffectInstance, pEntity);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide()) {
            if (this.attackAnimationTick > 0) {
                this.attackAnimationTick--;
            }
        }

        if (this.swinging && this.attackAnimationTick <= 0) {
            this.attackAnimationTick = 10;
            this.swingTime = 0;
        }
    }

    @Override
    public boolean doHurtTarget(Entity pEntity) {
        this.swing(InteractionHand.MAIN_HAND);

        ((LivingEntity) pEntity).addEffect(new MobEffectInstance(ModEffects.CORPSE_POISON,
                100,
                0,
                false,
                true,
                   true));

        return super.doHurtTarget(pEntity);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.2F, true));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 30.0F));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, AbstractGolem.class, true));
    }

//    @Override
//    protected @Nullable SoundEvent getAmbientSound() {
//        return SoundsRegistry.GREEMON_AMBIENT.get();
//    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return true;
    }
}
