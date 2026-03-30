package net.chixozhmix.dnmmod.spells.blood;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import net.chixozhmix.dnmmod.DnMMod;
import net.chixozhmix.dnmmod.entity.spell.ray_of_enfeeblement.RayOfEnfeeblement;
import net.chixozhmix.dnmmod.registers.SoundsRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.List;
import java.util.Optional;

public class RayOfEnfeeblementSpell extends AbstractSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(DnMMod.MOD_ID, "ray_of_enfeeblement");

    public RayOfEnfeeblementSpell (){
        this.manaCostPerLevel = 15;
        this.baseSpellPower = 6;
        this.spellPowerPerLevel = 1;
        this.castTime = 0;
        this.baseManaCost = 25;
    }

    private DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.UNCOMMON)
            .setMaxLevel(6)
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
        return CastType.INSTANT;
    }

    @Override
    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.of(SoundsRegistry.NECRO_MAGIC.value());
    }

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(Component.translatable("ui.irons_spellbooks.damage", new Object[]{Utils.stringTruncation((double)this.getDamage(spellLevel, caster), 1)}));
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        HitResult hitResult = Utils.raycastForEntity(level, entity, getRange(spellLevel, entity), true, 0.15F);
        level.addFreshEntity(new RayOfEnfeeblement(level, entity.getEyePosition(), hitResult.getLocation(), entity));
        if (hitResult.getType() == HitResult.Type.ENTITY) {
            Entity target = ((EntityHitResult)hitResult).getEntity();
            DamageSources.applyDamage(target, this.getDamage(spellLevel, entity), this.getDamageSource(entity));
            MagicManager.spawnParticles(level, ParticleHelper.BLOOD, hitResult.getLocation().x, target.getY(), hitResult.getLocation().z, 4, (double)0.0F, (double)0.0F, (double)0.0F, 0.3, true);

            if(target instanceof LivingEntity) {
                ((LivingEntity)target).addEffect(new MobEffectInstance(
                        MobEffects.WEAKNESS,
                        200,
                        0
                ));
            }

        } else if (hitResult.getType() == HitResult.Type.BLOCK) {
            MagicManager.spawnParticles(level, ParticleHelper.BLOOD, hitResult.getLocation().x, hitResult.getLocation().y, hitResult.getLocation().z, 4, (double)0.0F, (double)0.0F, (double)0.0F, 0.3, true);
        }

        MagicManager.spawnParticles(level, ParticleHelper.BLOOD, hitResult.getLocation().x, hitResult.getLocation().y, hitResult.getLocation().z, 50, (double)0.0F, (double)0.0F, (double)0.0F, 0.3, false);
        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    public static float getRange(int level, LivingEntity caster) {
        return 30.0F;
    }

    private float getDamage(int spellLevel, LivingEntity caster) {
        return 1.6F + this.getSpellPower(spellLevel, caster) * 1.5F;
    }
}
