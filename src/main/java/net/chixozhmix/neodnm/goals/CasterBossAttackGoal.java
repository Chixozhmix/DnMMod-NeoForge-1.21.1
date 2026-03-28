package net.chixozhmix.neodnm.goals;

import io.redspace.ironsspellbooks.api.entity.IMagicEntity;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.entity.mobs.goals.WizardAttackGoal;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public class CasterBossAttackGoal extends WizardAttackGoal {


    public CasterBossAttackGoal(IMagicEntity abstractSpellCastingMob, double pSpeedModifier, int pAttackInterval, int i) {
        super(abstractSpellCastingMob, pSpeedModifier, pAttackInterval, i);
    }

    public CasterBossAttackGoal setSpells(List<AbstractSpell> attackSpells, List<AbstractSpell> defenseSpells,
                                          List<AbstractSpell> movementSpells, List<AbstractSpell> supportSpells) {
        return (CasterBossAttackGoal) super.setSpells(attackSpells, defenseSpells, movementSpells, supportSpells);
    }

    public CasterBossAttackGoal setSingleUseSpell(AbstractSpell abstractSpell, int minDelay, int maxDelay, int minLevel, int maxLevel) {
        return (CasterBossAttackGoal) super.setSingleUseSpell(abstractSpell, minDelay, maxDelay, minLevel, maxLevel);
    }

    @Override
    public boolean canUse() {
        LivingEntity target = this.mob.getTarget();
        if (target != null && target.isAlive() && !isCreativePlayer(target)) {
            this.target = target;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean canContinueToUse() {
        LivingEntity target = this.mob.getTarget();
        if (target == null || !target.isAlive() || isCreativePlayer(target)) {
            return false;
        }

        double distance = this.mob.distanceToSqr(target.getX(), target.getY(), target.getZ());
        return distance <= this.getFollowDistance() * this.getFollowDistance() * 2.0;
    }

    @Override
    protected void handleAttackLogic(double distanceSquared) {
        if (--this.spellAttackDelay == 0) {
            this.resetSpellAttackTimer(distanceSquared);
            if (!this.spellCastingMob.isCasting() && !this.spellCastingMob.isDrinkingPotion()) {
                this.doSpellAction();
            }
        } else if (this.spellAttackDelay < 0) {
            this.spellAttackDelay = Mth.floor(Mth.lerp(Math.sqrt(distanceSquared) / (double)this.spellcastingRange,
                    (double)this.spellAttackIntervalMin, (double)this.spellAttackIntervalMax));
        }

        if (this.spellCastingMob.isCasting()) {
            if (this.target.isDeadOrDying()) {
                this.spellCastingMob.cancelCast();
            }
        }
    }

    @Override
    protected void doMovement(double distanceSquared) {
        double speed = (double)(this.spellCastingMob.isCasting() ? 0.75F : 1.0F) * this.movementSpeed();
        this.mob.lookAt(this.target, 30.0F, 30.0F);

        float fleeDist = 0.275F;
        if (this.allowFleeing && !this.spellCastingMob.isCasting() && this.spellAttackDelay > 10 &&
                --this.fleeCooldown <= 0 && distanceSquared < (double)(this.spellcastingRangeSqr * fleeDist * fleeDist)) {

            super.doMovement(distanceSquared);
        } else if (distanceSquared < (double)this.spellcastingRangeSqr * 2.0 && this.seeTime >= -20) {
            this.mob.getNavigation().stop();

            if (++this.strafeTime > 25 && this.mob.getRandom().nextDouble() < 0.1) {
                this.strafingClockwise = !this.strafingClockwise;
                this.strafeTime = 0;
            }

            float strafeForward = (distanceSquared * (double)6.0F < (double)this.spellcastingRangeSqr ? -1.0F : 0.5F) * 0.2F * (float)this.speedModifier;
            int strafeDir = this.strafingClockwise ? 1 : -1;
            this.mob.getMoveControl().strafe(strafeForward, (float)speed * (float)strafeDir);

            if (this.mob.horizontalCollision && this.mob.getRandom().nextFloat() < 0.1F) {
                this.tryJump();
            }
        } else if (this.mob.tickCount % 5 == 0) {
            if (this.isFlying) {
                this.mob.getMoveControl().setWantedPosition(this.target.getX(), this.target.getY() + (double)2.0F, this.target.getZ(), this.speedModifier);
            } else {
                this.mob.getNavigation().moveTo(this.target, this.speedModifier);
            }
        }
    }

    @Override
    protected int getAttackWeight() {
        int baseWeight = 80;
        if (this.target != null) {
            float targetHealth = this.target.getHealth() / this.target.getMaxHealth();
            int targetHealthWeight = (int)((1.0F - targetHealth) * (float)baseWeight * 0.75F);
            double distanceSquared = this.mob.distanceToSqr(this.target.getX(), this.target.getY(), this.target.getZ());
            int distanceWeight = (int)((double)1.0F - distanceSquared / (double)this.spellcastingRangeSqr * (double)-60.0F);

            int losWeight = this.hasLineOfSight ? 0 : -30;

            return Math.max(10, baseWeight + targetHealthWeight + distanceWeight + losWeight);
        } else {
            return 0;
        }
    }

    @Override
    protected int getMovementWeight() {
        if (this.target == null) {
            return 0;
        } else {
            double distanceSquared = this.mob.distanceToSqr(this.target.getX(), this.target.getY(), this.target.getZ());
            double distancePercent = Mth.clamp(distanceSquared / (double)this.spellcastingRangeSqr, (double)0.0F, (double)1.0F);
            int distanceWeight = (int)(distancePercent * (double)50.0F);

            int losWeight = this.hasLineOfSight ? 0 : 60;

            float healthInverted = 1.0F - this.mob.getHealth() / this.mob.getMaxHealth();
            float distanceInverted = (float)((double)1.0F - distancePercent);
            int runWeight = (int)(400.0F * healthInverted * healthInverted * distanceInverted * distanceInverted);

            return distanceWeight + losWeight + runWeight;
        }
    }

    private boolean isCreativePlayer(LivingEntity entity) {
        if (entity instanceof Player player) {
            return player.isCreative();
        }
        return false;
    }

    private double getFollowDistance() {
        return this.mob.getAttributeValue(net.minecraft.world.entity.ai.attributes.Attributes.FOLLOW_RANGE);
    }
}
