package net.chixozhmix.neodnm.spells.lightning;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.particle.BlastwaveParticleOptions;
import net.chixozhmix.neodnm.DnMMod;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Optional;

public class ThunderwaveSpell extends AbstractSpell {

    private static ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(DnMMod.MOD_ID, "thunderwave");

    public ThunderwaveSpell() {
        this.manaCostPerLevel = 10;
        this.baseSpellPower = 1;
        this.spellPowerPerLevel = 1;
        this.baseManaCost = 30;
    }

    private DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.UNCOMMON)
            .setSchoolResource(SchoolRegistry.LIGHTNING_RESOURCE)
            .setMaxLevel(10)
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
        return CastType.INSTANT;
    }

    @Override
    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.of(SoundEvents.LIGHTNING_BOLT_THUNDER);
    }

    @Override
    public AnimationHolder getCastFinishAnimation() {
        return SpellAnimations.CAST_T_POSE;
    }

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(Component.translatable("ui.irons_spellbooks.damage", new Object[]{Utils.stringTruncation
                ((double)this.getDamage(spellLevel, caster), 2)}));
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        super.onCast(level, spellLevel, entity, castSource, playerMagicData);

        AABB area = new AABB(entity.blockPosition()).inflate(5);

        List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, area);

        for(LivingEntity target : entities) {
            if (target == entity) continue;

            DamageSources.applyDamage(target, getDamage(spellLevel, entity), getDamageSource(entity));

            knockbackEntity(entity, target);
        }

        MagicManager.spawnParticles(level, new BlastwaveParticleOptions(((SchoolType)SchoolRegistry.LIGHTNING.get()).getTargetingColor(),
                5), entity.getX(), entity.getBoundingBox().getCenter().y, entity.getZ(), 1,
                (double)0.0F, (double)0.0F, (double)0.0F, (double)0.0F, true);
    }

    private void knockbackEntity(LivingEntity caster, LivingEntity target) {
        Vec3 direction = target.position().subtract(caster.position()).normalize();

        float knockbackStrength = 1.2F;

        target.setDeltaMovement(target.getDeltaMovement().add(
                direction.x * knockbackStrength,
                Math.min(0.5, knockbackStrength * 0.3), // Небольшой подъем
                direction.z * knockbackStrength
        ));
        target.hurtMarked = true;
    }

    private float getDamage(int spellLevel, LivingEntity entity) {
        return this.getSpellPower(spellLevel, entity) * 1.2F;
    }
}
