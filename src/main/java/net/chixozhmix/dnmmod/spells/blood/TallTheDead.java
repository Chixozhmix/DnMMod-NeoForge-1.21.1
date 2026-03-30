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
import net.chixozhmix.dnmmod.api.registers.DnMSchools;
import net.chixozhmix.dnmmod.entity.spell.tall_the_dead.tallTheDead;
import net.chixozhmix.dnmmod.registers.SoundsRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Optional;

public class TallTheDead extends AbstractSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(DnMMod.MOD_ID, "tall_the_dead");

    public TallTheDead (){
        this.manaCostPerLevel = 10;
        this.baseSpellPower = 10;
        this.spellPowerPerLevel = 1;
        this.castTime = 10;
        this.baseManaCost = 10;
    }

    private DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.COMMON)
            .setMaxLevel(10)
            .setSchoolResource(DnMSchools.NECRO_RESOURCE)
            .setCooldownSeconds(20)
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
    public SchoolType getSchoolType() {
        return DnMSchools.NECRO.get();
    }

    @Override
    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.of(SoundsRegistry.TALL_THE_DEAD.get());
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
                Component.translatable("ui.dnmmod.damage_min_health", new Object[]{Utils.stringTruncation(this.getDamageHealth(spellLevel, caster), 2)}));
    }

    @Override
    public boolean checkPreCastConditions(Level level, int spellLevel, LivingEntity entity, MagicData playerMagicData) {
        return Utils.preCastTargetHelper(level, entity, playerMagicData, this, 32, 0.35F);
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
        tallTheDead ray = new tallTheDead(level);

        ICastData var7 = playerMagicData.getAdditionalCastData();
        if (var7 instanceof TargetEntityCastData castTargetingData) {
            LivingEntity target = castTargetingData.getTarget((ServerLevel) level);
            ray.setTarget(castTargetingData.getTarget((ServerLevel) level));
            Vec3 spawn = target.position();

            if (target != null) {
                ray.setOwner(entity);
                ray.moveTo(spawn);
                ray.setDamage(0);
                level.addFreshEntity(ray);

                MagicManager.spawnParticles(level, ParticleHelper.BLOOD, spawn.x, spawn.y, spawn.z, 30, (double) 1.0F, (double) 1.0F, (double) 1.0F, (double) 1.0F, true);
                MagicManager.spawnParticles(level, ParticleHelper.BLOOD, spawn.x, spawn.y + (double) 1.0F, spawn.z, 30, (double) 0.25F, (double) 1.5F, (double) 0.25F, (double) 1.0F, true);
                MagicManager.spawnParticles(level,
                        new BlastwaveParticleOptions(
                                new org.joml.Vector3f(
                                        ((0x52A859 >> 16) & 0xFF) / 255.0f,
                                        ((0x52A859 >> 8) & 0xFF) / 255.0f,
                                        (0x52A859 & 0xFF) / 255.0f
                                ),
                                4.0f
                        ), target.getX(), target.getBoundingBox().getCenter().y + 0.4, target.getZ(), 1, (double) 0.0F, (double) 0.0F, (double) 0.0F, (double) 0.0F, true);

                if(entity.getHealth() < entity.getMaxHealth())
                {
                    SpellDamageSource source = this.getDamageSource(entity);
                    DamageSources.applyDamage(target, this.getDamageHealth(spellLevel, entity), source);
                    DamageSources.ignoreNextKnockback(target);
                } else {
                    SpellDamageSource source = this.getDamageSource(entity);
                    DamageSources.applyDamage(target, this.getDamage(spellLevel, entity), source);
                    DamageSources.ignoreNextKnockback(target);
                }
            }
        }
    }

    private float getDamage(int spellLevel, LivingEntity caster) {
        return this.getSpellPower(spellLevel, caster);
    }

    private float getDamageHealth (int spellLevel, LivingEntity caster) {
        return this.getSpellPower(spellLevel, caster) + 2;
    }
}
