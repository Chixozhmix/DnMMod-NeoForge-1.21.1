package net.chixozhmix.dnmmod.goals;

import io.redspace.ironsspellbooks.api.entity.IMagicEntity;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.entity.mobs.goals.WizardAttackGoal;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.EnumSet;
import java.util.List;

public class MeleeCasterAtackGoal extends WizardAttackGoal {
    private static final double MELEE_RANGE = 2.0;
    private static final double MELEE_RANGE_SQ = MELEE_RANGE * MELEE_RANGE;
    private int meleeAttackTime;
    private int meleeAttackCooldown = 20; // 1 секунда перезарядки для ближней атаки

    public MeleeCasterAtackGoal(IMagicEntity abstractSpellCastingMob, double pSpeedModifier, int pAttackInterval, int i) {
        super(abstractSpellCastingMob, pSpeedModifier, pAttackInterval, i);
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK)); // Добавляем флаг для ближней атаки
    }

    public MeleeCasterAtackGoal setSpells(List<AbstractSpell> attackSpells, List<AbstractSpell> defenseSpells,
                                          List<AbstractSpell> movementSpells, List<AbstractSpell> supportSpells) {
        return (MeleeCasterAtackGoal) super.setSpells(attackSpells, defenseSpells, movementSpells, supportSpells);
    }

    public MeleeCasterAtackGoal setSingleUseSpell(AbstractSpell abstractSpell, int minDelay, int maxDelay, int minLevel, int maxLevel) {
        return (MeleeCasterAtackGoal) super.setSingleUseSpell(abstractSpell, minDelay, maxDelay, minLevel, maxLevel);
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
    public void start() {
        super.start();
        this.meleeAttackTime = 0;
    }

    @Override
    public void stop() {
        super.stop();
        this.meleeAttackTime = 0;
    }

    @Override
    public void tick() {
        if (this.target == null || !this.target.isAlive()) {
            return;
        }

        double distanceSquared = this.mob.distanceToSqr(this.target.getX(), this.target.getY(), this.target.getZ());

        // Обновляем таймер ближней атаки
        if (this.meleeAttackTime > 0) {
            this.meleeAttackTime--;
        }

        // Проверяем возможность ближней атаки
        if (canMeleeAttack(distanceSquared)) {
            performMeleeAttack();
        } else {
            // Если не в ближней атаке, используем обычную логику
            super.tick();
        }
    }

    @Override
    protected void handleAttackLogic(double distanceSquared) {
        // Если цель в зоне ближней атаки, пропускаем магическую атаку
        if (distanceSquared <= MELEE_RANGE_SQ) {
            // Останавливаем текущее кастование
            if (this.spellCastingMob.isCasting()) {
                this.spellCastingMob.cancelCast();
            }
            return;
        }

        // Иначе используем обычную логику
        super.handleAttackLogic(distanceSquared);
    }

    @Override
    protected void doMovement(double distanceSquared) {
        // Если цель слишком близко, отступаем
        if (distanceSquared <= MELEE_RANGE_SQ && !this.spellCastingMob.isCasting()) {
            // Отступаем от цели
            double dx = this.mob.getX() - this.target.getX();
            double dz = this.mob.getZ() - this.target.getZ();
            double distance = Math.sqrt(dx * dx + dz * dz);

            if (distance > 0) {
                double speed = this.movementSpeed() * 1.2; // Быстрее отступаем
                double moveX = this.mob.getX() + (dx / distance) * speed;
                double moveZ = this.mob.getZ() + (dz / distance) * speed;

                this.mob.getNavigation().moveTo(moveX, this.target.getY(), moveZ, speed);
            }

            // Смотрим на цель
            this.mob.lookAt(this.target, 30.0F, 30.0F);
            return;
        }

        // Иначе используем обычную логику движения
        super.doMovement(distanceSquared);
    }

    @Override
    protected void doSpellAction() {
        double distanceSquared = this.mob.distanceToSqr(this.target.getX(), this.target.getY(), this.target.getZ());

        // Если цель слишком близко, не используем заклинания
        if (distanceSquared <= MELEE_RANGE_SQ) {
            return;
        }

        // Иначе используем обычную логику
        super.doSpellAction();
    }

    private boolean canMeleeAttack(double distanceSquared) {
        return distanceSquared <= MELEE_RANGE_SQ &&
                this.meleeAttackTime <= 0 &&
                this.hasLineOfSight &&
                !this.spellCastingMob.isCasting();
    }

    private void performMeleeAttack() {
        // Атакуем ближним ударом
        this.mob.swing(InteractionHand.MAIN_HAND);

        // Получаем урон из атрибута моба
        float attackDamage = (float)this.mob.getAttributeValue(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE);

        if (this.target.hurt(this.mob.damageSources().mobAttack(this.mob), attackDamage)) {
            // Наносим урон
            this.mob.doHurtTarget(this.target);

            // Эффект отбрасывания (также можно использовать атрибут ATTACK_KNOCKBACK)
            float knockback = (float)this.mob.getAttributeValue(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_KNOCKBACK);
            double dx = this.target.getX() - this.mob.getX();
            double dz = this.target.getZ() - this.mob.getZ();
            double distance = Math.sqrt(dx * dx + dz * dz);

            if (distance > 0) {
                this.target.push((dx / distance) * knockback, 0.1, (dz / distance) * knockback);
            }
        }

        // Устанавливаем перезарядку
        this.meleeAttackTime = this.meleeAttackCooldown;

        // Сбрасываем таймер магической атаки
        this.spellAttackDelay = Math.max(this.spellAttackDelay, 20);
    }

    @Override
    protected int getAttackWeight() {
        int baseWeight = 80;
        if (this.target != null) {
            double distanceSquared = this.mob.distanceToSqr(this.target.getX(), this.target.getY(), this.target.getZ());

            // Увеличиваем вес атаки при ближней дистанции
            int meleeBonus = 0;
            if (distanceSquared <= MELEE_RANGE_SQ && this.hasLineOfSight) {
                meleeBonus = 60; // Большой бонус для ближней атаки
            }

            float targetHealth = this.target.getHealth() / this.target.getMaxHealth();
            int targetHealthWeight = (int)((1.0F - targetHealth) * (float)baseWeight * 0.75F);
            int distanceWeight = (int)((double)1.0F - distanceSquared / (double)this.spellcastingRangeSqr * (double)-60.0F);

            int losWeight = this.hasLineOfSight ? 0 : -30;

            return Math.max(10, baseWeight + targetHealthWeight + distanceWeight + losWeight + meleeBonus);
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

            // Если слишком близко, увеличиваем вес движения для отступления
            int retreatBonus = 0;
            if (distanceSquared <= MELEE_RANGE_SQ) {
                retreatBonus = 100; // Большой бонус для отступления
            }

            double distancePercent = Mth.clamp(distanceSquared / (double)this.spellcastingRangeSqr, (double)0.0F, (double)1.0F);
            int distanceWeight = (int)(distancePercent * (double)50.0F);

            int losWeight = this.hasLineOfSight ? 0 : 60;

            float healthInverted = 1.0F - this.mob.getHealth() / this.mob.getMaxHealth();
            float distanceInverted = (float)((double)1.0F - distancePercent);
            int runWeight = (int)(400.0F * healthInverted * healthInverted * distanceInverted * distanceInverted);

            return distanceWeight + losWeight + runWeight + retreatBonus;
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
