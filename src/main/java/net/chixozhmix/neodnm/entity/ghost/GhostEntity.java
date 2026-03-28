package net.chixozhmix.neodnm.entity.ghost;

import net.chixozhmix.neodnm.registers.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.fluids.FluidType;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.EnumSet;

public class GhostEntity extends Monster implements GeoEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private final RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("idle");
    private final RawAnimation WALK_ANIM = RawAnimation.begin().thenLoop("walk");
    private final RawAnimation ATTACK_ANIM = RawAnimation.begin().thenPlay("attack");

    private boolean isCharging = false;

    private int invisibilityCooldown = 0;
    private boolean isInvisibleFromAbility = false;
    private static final double INVISIBILITY_CHANCE = 0.3; // 30% шанс
    private static final int INVISIBILITY_DURATION = 60; // 3 секунды (20 тиков = 1 секунда)
    private static final int INVISIBILITY_COOLDOWN = 100; // 5 секунд кулдауна

    private int attackAnimationTick = 0;

    private static final AttributeSupplier ATTRIBUTES = Monster.createMobAttributes()
            .add(Attributes.MAX_HEALTH, 15.0D)
            .add(Attributes.ARMOR, 2.0D)
            .add(Attributes.MOVEMENT_SPEED, 0.29D)
            .add(Attributes.ATTACK_DAMAGE, 2.0D)
            .add(Attributes.FOLLOW_RANGE, 35.0D)
            .add(Attributes.KNOCKBACK_RESISTANCE, 0.7D)
            .add(Attributes.FLYING_SPEED, 0.29D).build();

    public GhostEntity(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.noPhysics = true;
        this.moveControl = new FlyingMoveControl(this, 10, true);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 5, this::predicate));
    }

    private <T extends GeoAnimatable> PlayState predicate(AnimationState<T> state) {
        var controller = state.getController();

        if (this.attackAnimationTick > 0) {
            controller.setAnimation(ATTACK_ANIM);
            return PlayState.CONTINUE;
        }

        if (state.isMoving()) {
            state.getController().setAnimation(WALK_ANIM);
            return  PlayState.CONTINUE;
        }

        state.getController().setAnimation(IDLE_ANIM);
        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    public static AttributeSupplier createAttributes () {
        return ATTRIBUTES;
    }

    @Override
    public void tick() {
        super.tick();

        // Проверяем, горит ли моб на солнце
        if (!this.level().isClientSide) {
            if (this.isSunBurnTick()) {
                this.setRemainingFireTicks(3 * 20);
            }
        }

        if (this.attackAnimationTick > 0) {
            this.attackAnimationTick--;
        }

        // Обновляем кулдаун невидимости
        if (this.invisibilityCooldown > 0) {
            this.invisibilityCooldown--;
        }

        // Проверяем, нужно ли снять эффект невидимости
        if (this.isInvisibleFromAbility && this.invisibilityCooldown <= INVISIBILITY_COOLDOWN - INVISIBILITY_DURATION) {
            spawnInvisibilityParticles();
            this.isInvisibleFromAbility = false;
            this.setInvisible(false);
        }

        if (this.swinging) {
            this.getNavigation().stop();
            this.attackAnimationTick = 5;
            this.swinging = false;
        }
    }

    @Override
    protected boolean isSunBurnTick() {
        if (this.level().isDay() && !this.level().isClientSide) {
            BlockPos blockpos = this.blockPosition();
            if (this.level().canSeeSky(blockpos)) {
                float f = this.getLightLevelDependentMagicValue();
                boolean flag = this.isInWaterRainOrBubble() || this.isInPowderSnow || this.wasInPowderSnow;

                if (f > 0.5F && this.random.nextFloat() * 30.0F < (f - 0.4F) * 2.0F && !flag) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public float getLightLevelDependentMagicValue() {
        return this.level().getBrightness(LightLayer.SKY, this.blockPosition()) / 15.0F;
    }

    @Override
    public boolean doHurtTarget(Entity pEntity) {
        this.swing(InteractionHand.MAIN_HAND);

        if (pEntity instanceof Player && this.invisibilityCooldown <= 0 && this.random.nextDouble() < INVISIBILITY_CHANCE) {
            activateInvisibility();
        }
        return super.doHurtTarget(pEntity);
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if (this.invisibilityCooldown <= 0 && this.random.nextDouble() < INVISIBILITY_CHANCE) {
            activateInvisibility();
        }
        return super.hurt(pSource, pAmount);
    }

    private void activateInvisibility() {
        spawnInvisibilityParticles();

        this.playSound(SoundEvents.ILLUSIONER_MIRROR_MOVE,
                2.0f,
                1.0f);

        this.isInvisibleFromAbility = true;
        this.setInvisible(true);
        this.invisibilityCooldown = INVISIBILITY_COOLDOWN;
    }

    private void spawnInvisibilityParticles() {
        if (this.level() instanceof ServerLevel serverLevel) {
            // Используем серверный метод для спавна частиц
            serverLevel.sendParticles(
                    ParticleTypes.SMOKE,
                    this.getX(),
                    this.getY() + this.getEyeHeight() / 2,
                    this.getZ(),
                    40, // количество частиц
                    this.getBbWidth() / 2, // разброс по X
                    this.getBbHeight() / 2, // разброс по Y
                    this.getBbWidth() / 2, // разброс по Z
                    0.1 // скорость
            );
        } else {
            for (int i = 0; i < 40; i++) {
                this.level().addParticle(ParticleTypes.SMOKE,
                        this.getX() + (this.random.nextDouble() - 0.5) * this.getBbWidth(),
                        this.getY() + this.random.nextDouble() * this.getBbHeight(),
                        this.getZ() + (this.random.nextDouble() - 0.5) * this.getBbWidth(),
                        0, 0.1, 0);
            }
        }
    }

    @Override
    public boolean isInvisible() {
        return this.isInvisibleFromAbility || super.isInvisible();
    }

    public boolean isInvisibleFromAbility() {
        return this.isInvisibleFromAbility;
    }

    public int getInvisibilityCooldown() {
        return this.invisibilityCooldown;
    }

    @Override
    protected PathNavigation createNavigation(Level pLevel) {
        FlyingPathNavigation flyingpathnavigation = new FlyingPathNavigation(this, pLevel){
            @Override
            public boolean isStableDestination(BlockPos pos) {
                // Можем летать через любые блоки
                return true;
            }

            @Override
            public boolean canCutCorner(PathType pPathType) {
                return true;
            }
        };
        flyingpathnavigation.setCanOpenDoors(false);
        flyingpathnavigation.setCanFloat(true);
        flyingpathnavigation.setCanPassDoors(true);
        return flyingpathnavigation;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new GhostChargeAttackGoal());
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 30.0F));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));

        this.goalSelector.addGoal(5, new RandomStrollGoal(this, 1.0D, 20));

        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource damageSource, boolean recentlyHit) {
        super.dropCustomDeathLoot(level, damageSource, recentlyHit);

        RandomSource randomSource = this.random;

        this.spawnAtLocation(new ItemStack(ModItems.ECTOPLASM.get(), randomSource.nextInt(3)));
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return true;
    }

    @Override
    public boolean hasLineOfSight(Entity pEntity) {
        return true;
    }

//    @Override
//    protected @Nullable SoundEvent getAmbientSound() {
//        return SoundsRegistry.GHOST_AMBIENT.get();
//    }

    @Override
    protected float getSoundVolume() {
        return 2.0f;
    }

//    @Override
//    public MobType getMobType() {
//        return MobType.UNDEAD;
//    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    public boolean isPushedByFluid(FluidType type) {
        return false;
    }

    @Override
    public boolean isIgnoringBlockTriggers() {
        return true;
    }

    @Override
    public void move(MoverType type, Vec3 movement) {
        super.move(type, movement);
    }

    class GhostChargeAttackGoal extends Goal {
        public GhostChargeAttackGoal() {
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        public boolean canUse() {
            LivingEntity target = GhostEntity.this.getTarget();
            return target != null &&
                    target.isAlive() &&
                    GhostEntity.this.distanceToSqr(target) <= 35.0 * 35.0;
        }

        public boolean canContinueToUse() {
            return GhostEntity.this.getTarget() != null &&
                    GhostEntity.this.getTarget().isAlive();
        }

        public void start() {
            GhostEntity.this.isCharging = true;
        }

        public void stop() {
            GhostEntity.this.isCharging = false;
        }

        public void tick() {
            LivingEntity target = GhostEntity.this.getTarget();
            if (target != null) {
                double targetY = target.getY() + target.getBbHeight() * 0.5;
                double offsetX = (GhostEntity.this.random.nextDouble() - 0.5) * 0.8;
                double offsetZ = (GhostEntity.this.random.nextDouble() - 0.5) * 0.8;

                Vec3 attackPosition = new Vec3(
                        target.getX() + offsetX,
                        targetY,
                        target.getZ() + offsetZ
                );
                double speed = 5.0f;

                GhostEntity.this.moveControl.setWantedPosition(
                        attackPosition.x,
                        attackPosition.y,
                        attackPosition.z,
                        speed
                );

                if (distanceTo(target) <= 2.0f) {
                    GhostEntity.this.doHurtTarget(target);
                }
            }
        }
    }
}


