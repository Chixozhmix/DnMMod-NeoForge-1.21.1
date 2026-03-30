package net.chixozhmix.dnmmod.spells.blood;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.capabilities.magic.TargetEntityCastData;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.damage.SpellDamageSource;
import io.redspace.ironsspellbooks.particle.BlastwaveParticleOptions;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import net.chixozhmix.dnmmod.DnMMod;
import net.chixozhmix.dnmmod.entity.spell.contagion_ray.ContagionRay;
import net.chixozhmix.dnmmod.registers.ModEffects;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class ContagionSpell extends AbstractSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(DnMMod.MOD_ID, "contagion");

    public ContagionSpell (){
        this.manaCostPerLevel = 10;
        this.baseSpellPower = 11;
        this.spellPowerPerLevel = 1;
        this.castTime = 15;
        this.baseManaCost = 40;
    }

    private DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.UNCOMMON)
            .setMaxLevel(5)
            .setSchoolResource(SchoolRegistry.BLOOD_RESOURCE)
            .setCooldownSeconds(45)
            .build();

    @Override
    public ResourceLocation getSpellResource() {
        return spellId;
    }

    @Override
    public DefaultConfig getDefaultConfig() {
        return defaultConfig;
    }

    @Override
    public CastType getCastType() {
        return CastType.LONG;
    }

    @Override
    public AnimationHolder getCastStartAnimation() {
        return SpellAnimations.CHARGE_RAISED_HAND;
    }

    @Override
    public AnimationHolder getCastFinishAnimation() {
        return SpellAnimations.ANIMATION_INSTANT_CAST;
    }

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(Component.translatable("ui.irons_spellbooks.damage", new Object[]{Utils.stringTruncation((double)this.getDamage(spellLevel, caster), 1)}),
                Component.translatable("ui.irons_spellbooks.effect_length", new Object[]{Utils.timeFromTicks(this.getDurability(spellLevel, caster), 2)}),
                Component.translatable("ui.irons_spellbooks.radius", new Object[]{Utils.stringTruncation(getRadius(spellLevel, caster), 3)}));

    }

    @Override
    public boolean checkPreCastConditions(Level level, int spellLevel, LivingEntity entity, MagicData playerMagicData) {
        return Utils.preCastTargetHelper(level, entity, playerMagicData, this, 32, 0.35F);
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
        ContagionRay ray = new ContagionRay(level);

        ICastData var7 = playerMagicData.getAdditionalCastData();
        if (var7 instanceof TargetEntityCastData castTargetingData) {
            LivingEntity target = castTargetingData.getTarget((ServerLevel) level);
            ray.setTarget(castTargetingData.getTarget((ServerLevel)level));
            Vec3 spawn = target.position();

            if(target != null) {
                ray.setOwner(entity);
                ray.moveTo(spawn);
                ray.setDamage(0);
                level.addFreshEntity(ray);

                MagicManager.spawnParticles(level, ParticleHelper.BLOOD, spawn.x, spawn.y, spawn.z, 30, (double)1.0F, (double)1.0F, (double)1.0F, (double)1.0F, true);
                MagicManager.spawnParticles(level, ParticleHelper.BLOOD, spawn.x, spawn.y + (double)1.0F, spawn.z, 30, (double)0.25F, (double)1.5F, (double)0.25F, (double)1.0F, true);
                MagicManager.spawnParticles(level, new BlastwaveParticleOptions(((SchoolType)SchoolRegistry.BLOOD.get()).getTargetingColor(),
                                getRadius(spellLevel, entity)), target.getX(), target.getBoundingBox().getCenter().y + 0.4, target.getZ(), 1,
                        (double)0.0F, (double)0.0F, (double)0.0F, (double)0.0F, true);

                target.addEffect(new MobEffectInstance(ModEffects.CORPSE_POISON, this.getDurability(spellLevel, entity)));

                SpellDamageSource source = this.getDamageSource(entity);
                DamageSources.applyDamage(target, this.getDamage(spellLevel, entity), source);
                DamageSources.ignoreNextKnockback(target);

                List<LivingEntity> livingEntities = target.level().getEntitiesOfClass(
                        LivingEntity.class,
                        target.getBoundingBox().inflate(getRadius(spellLevel, entity)),
                        e -> e != target && e.isAlive()
                );
                for (LivingEntity living : livingEntities) {
                    living.addEffect(new MobEffectInstance(
                            MobEffects.POISON,
                            this.getDurability(spellLevel, entity),
                            1
                    ));
                }
            }
        }

    }

    private static float getRadius(int level, LivingEntity caster) {
        return 3 + level;
    }

    private float getDamage(int spellLevel, LivingEntity caster) {
        return this.getSpellPower(spellLevel, caster);
    }

    private int getDurability (int level, LivingEntity caster) {
        return 100 + (level * 20);
    }
}
