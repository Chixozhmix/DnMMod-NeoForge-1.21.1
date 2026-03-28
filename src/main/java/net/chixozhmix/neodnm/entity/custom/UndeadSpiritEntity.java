package net.chixozhmix.neodnm.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

public class UndeadSpiritEntity extends Monster implements GeoEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("idle");
    private static final RawAnimation WALK_ANIM = RawAnimation.begin().thenLoop("walk");
    private static final RawAnimation ATTACK_ANIM = RawAnimation.begin().thenPlay("attack");

    private int attackAnimationTick = 0;

    public UndeadSpiritEntity(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
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

        else if (state.isMoving()) {
            controller.setAnimation(WALK_ANIM);
            return  PlayState.CONTINUE;
        }

        controller.setAnimation(IDLE_ANIM);
        return PlayState.CONTINUE;
    }


    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    public static AttributeSupplier createAttributes () {
        return Monster.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 30.0D)
                .add(Attributes.ARMOR, 4.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.20D)
                .add(Attributes.ATTACK_DAMAGE, 2.0D)
                .add(Attributes.FOLLOW_RANGE, 40.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.2D).build();
    }

    @Override
    public void tick() {
        super.tick();

        // Проверяем, горит ли моб на солнце
        if (!this.level().isClientSide) {
            if (this.isSunBurnTick()) {
                this.setRemainingFireTicks(3*20);
            }

            // Обновляем счетчик анимации атаки
            if (this.attackAnimationTick > 0) {
                this.attackAnimationTick--;
            }
        }

        if (this.swinging) {
            this.getNavigation().stop();
            this.attackAnimationTick = 5;
            this.swinging = false;
        }


    }

    @Override
    public boolean doHurtTarget(Entity pEntity) {
        this.swing(InteractionHand.MAIN_HAND);
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
    }

    @Override
    protected boolean isSunBurnTick() {
        if (this.level().isDay() && !this.level().isClientSide) {
            // Проверяем, находится ли моб под открытым небом
            BlockPos blockpos = this.blockPosition();
            if (this.level().canSeeSky(blockpos)) {
                // Проверяем уровень света (блокового и небесного)
                float f = this.getLightLevelDependentMagicValue();
                // Проверяем, что моб не находится в воде и не под дождем для охлаждения
                boolean flag = this.isInWaterRainOrBubble() || this.isInPowderSnow || this.wasInPowderSnow;

                // Уровень света должен быть достаточно высоким и моб не должен охлаждаться
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

//    @Override
//    protected @Nullable SoundEvent getAmbientSound() {
//        return SoundsRegistry.UNDEAD_SPIRIT.get();
//    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return true;
    }
}
