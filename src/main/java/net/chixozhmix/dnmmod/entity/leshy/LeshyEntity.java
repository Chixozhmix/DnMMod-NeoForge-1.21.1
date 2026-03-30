package net.chixozhmix.dnmmod.entity.leshy;

import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import net.chixozhmix.dnmmod.goals.MeleeCasterAtackGoal;
import net.chixozhmix.dnmmod.registers.ModItems;
import net.chixozhmix.dnmmod.registers.RegistrySpells;
import net.chixozhmix.dnmmod.registers.SoundsRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.BossEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class LeshyEntity extends AbstractSpellCastingMob implements Enemy {

    public LeshyEntity(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.xpReward = 60;
    }

    public final ServerBossEvent bossEvent = new ServerBossEvent(Component.translatable("ui.dnmmod.leshy_bossbar"),
            BossEvent.BossBarColor.GREEN, BossEvent.BossBarOverlay.NOTCHED_12);

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(4, new MeleeCasterAtackGoal(this, 1.25F, 35, 80)
                .setSpells(
                        List.of(SpellRegistry.ACID_ORB_SPELL.get(),
                                RegistrySpells.CAUSTIC_BREW.get(),
                                SpellRegistry.STOMP_SPELL.get(),
                                SpellRegistry.FIREFLY_SWARM_SPELL.get(),
                                SpellRegistry.BLOOD_STEP_SPELL.get()),
                        List.of(SpellRegistry.BLIGHT_SPELL.get(),
                                SpellRegistry.ROOT_SPELL.get()),
                        List.of(SpellRegistry.BLOOD_STEP_SPELL.get()),
                        List.of())
                .setSingleUseSpell(RegistrySpells.SUMMON_RAVEN.get(), 80, 350, 10, 10));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, (double)1.0F));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this).setAlertOthers());
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractGolem.class, true));
    }

    public static AttributeSupplier.Builder prepareAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.ATTACK_DAMAGE, 6.0)
                .add(Attributes.ATTACK_KNOCKBACK, 0.2)
                .add(Attributes.MAX_HEALTH, 300.0)
                .add(Attributes.FOLLOW_RANGE, 40.0)
                .add(AttributeRegistry.SPELL_POWER, 1.25)
                .add(Attributes.MOVEMENT_SPEED, 0.22)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.1)
                .add(Attributes.ARMOR, 15.0)
                .add(AttributeRegistry.SPELL_RESIST, 0.5)
                .add(AttributeRegistry.SUMMON_DAMAGE, 0.4);
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData) {
        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource damageSource, boolean recentlyHit) {
        super.dropCustomDeathLoot(level, damageSource, recentlyHit);

        RandomSource randomSource = this.random;

        if(randomSource.nextFloat() == 0f) {
            this.spawnAtLocation(new ItemStack(ItemRegistry.NATURE_RUNE.get(), randomSource.nextInt(1, 3)));
        }

        this.spawnAtLocation(new ItemStack(ModItems.FOREST_HEART.get(), 1));
    }

    @Override
    public boolean canAttack(LivingEntity target) {
        return true;
    }

    @Override
    public boolean doHurtTarget(Entity pEntity) {
        this.swing(InteractionHand.MAIN_HAND);

        if(pEntity instanceof LivingEntity) {
            ((LivingEntity) pEntity).addEffect(new MobEffectInstance(
                    MobEffects.MOVEMENT_SLOWDOWN,
                    100,
                    0,
                    false,
                    true,
                    true
            ));
        }

        return super.doHurtTarget(pEntity);
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return false;
    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return SoundsRegistry.LESHY_AMBIENT.get();
    }

    /* BOSS BAR */

    @Override
    public void startSeenByPlayer(ServerPlayer pServerPlayer) {
        super.startSeenByPlayer(pServerPlayer);

        this.bossEvent.addPlayer(pServerPlayer);
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer pServerPlayer) {
        super.stopSeenByPlayer(pServerPlayer);

        this.bossEvent.removePlayer(pServerPlayer);
    }

    @Override
    public void aiStep() {
        super.aiStep();

        this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
    }

    @Override
    public boolean addEffect(MobEffectInstance pEffectInstance, @Nullable Entity pEntity) {
        if(pEffectInstance.getEffect() == MobEffects.POISON)
            return false;

        return super.addEffect(pEffectInstance, pEntity);
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        // Вызываем супер метод для получения урона
        boolean hurt = super.hurt(pSource, pAmount);

        if (hurt && !this.level().isClientSide()) {
            if (pSource.getEntity() instanceof LivingEntity attacker &&
                    attacker != this && this.canAttack(attacker)) {

                // Проверяем, что это не креативный игрок
                if (attacker instanceof Player player) {
                    if (!player.isCreative()) {
                        this.setTarget((LivingEntity) attacker);
                    }
                } else {
                    this.setTarget((LivingEntity) attacker);
                }
            }
        }

        return hurt;
    }
}
