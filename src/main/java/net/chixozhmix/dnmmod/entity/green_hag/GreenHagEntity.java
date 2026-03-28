package net.chixozhmix.dnmmod.entity.green_hag;

import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.entity.mobs.goals.WizardRecoverGoal;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import net.chixozhmix.dnmmod.goals.CasterBossAttackGoal;
import net.chixozhmix.dnmmod.registers.ModItems;
import net.chixozhmix.dnmmod.registers.RegistrySpells;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.BossEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.Turtle;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GreenHagEntity extends AbstractSpellCastingMob implements Enemy {

    public GreenHagEntity(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.xpReward = 40;
    }

    private final ServerBossEvent bossEvent = new ServerBossEvent(Component.translatable("ui.dnmmod.green_hag_bossbar"),
            BossEvent.BossBarColor.GREEN, BossEvent.BossBarOverlay.NOTCHED_6);

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
        super.defineSynchedData(pBuilder);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(4, (new CasterBossAttackGoal(this, (double)1.25F, 35, 80))
                .setSpells(List.of(SpellRegistry.WITHER_SKULL_SPELL.get(),
                        SpellRegistry.POISON_ARROW_SPELL.get(),
                        RegistrySpells.ICE_DAGGER.get(),
                        SpellRegistry.BLOOD_SLASH_SPELL.get()),
                        List.of(RegistrySpells.THUNDERWAVE.get()),
                        List.of(),
                        List.of(SpellRegistry.FANG_WARD_SPELL.get(),
                                SpellRegistry.ROOT_SPELL.get()))
                .setSingleUseSpell(SpellRegistry.SUMMON_VEX_SPELL.get(), 80, 350, 3, 4)
                .setDrinksPotions());
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, (double)1.0F));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(10, new WizardRecoverGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, new Class[0]));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal(this, IronGolem.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal(this, Turtle.class, 10, true, false, Turtle.BABY_ON_LAND_SELECTOR));
    }

    public static AttributeSupplier.Builder prepareAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.ATTACK_DAMAGE, (double)6.0F)
                .add(Attributes.ATTACK_KNOCKBACK, (double)0.2F)
                .add(Attributes.MAX_HEALTH, (double)220.0F)
                .add(Attributes.FOLLOW_RANGE, (double)30.0F)
                .add(AttributeRegistry.SPELL_POWER, (double)1.15F)
                .add(Attributes.MOVEMENT_SPEED, (double)0.24F)
                .add(AttributeRegistry.SPELL_RESIST, 0.5f)
                .add(AttributeRegistry.SUMMON_DAMAGE, 0.4f);
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData) {
        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return false;
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource damageSource, boolean recentlyHit) {
        super.dropCustomDeathLoot(level, damageSource, recentlyHit);

        RandomSource randomSource = this.random;

        if(randomSource.nextFloat() == 0f) {
            this.spawnAtLocation(new ItemStack(ItemRegistry.NATURE_RUNE.get(), randomSource.nextInt(1, 3)));
        }

        if(randomSource.nextInt(0, 6) == 0)
            this.spawnAtLocation(new ItemStack(ModItems.HAG_EYE.get(), 1));

        else if(randomSource.nextFloat() == 1.0f) {
            this.spawnAtLocation(new ItemStack(ItemRegistry.BLOOD_RUNE.get(), randomSource.nextInt(1, 3)));
        }
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
}
