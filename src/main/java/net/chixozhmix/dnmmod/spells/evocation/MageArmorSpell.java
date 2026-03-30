package net.chixozhmix.dnmmod.spells.evocation;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import net.chixozhmix.dnmmod.DnMMod;
import net.chixozhmix.dnmmod.registers.ModEffects;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.List;

public class MageArmorSpell extends AbstractSpell {

    private static final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(DnMMod.MOD_ID, "mage_armor");

    public MageArmorSpell() {
        this.manaCostPerLevel = 10;
        this.baseSpellPower = 1;
        this.spellPowerPerLevel = 1;
        this.castTime = 20;
        this.baseManaCost = 35;
    }

    private DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.UNCOMMON)
            .setSchoolResource(SchoolRegistry.EVOCATION_RESOURCE)
            .setMaxLevel(7)
            .setCooldownSeconds(120)
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
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(
                Component.translatable("ui.irons_spellbooks.effect_length", "60s")
        );
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
        entity.addEffect(
                new MobEffectInstance(
                        ModEffects.MAGE_ARMOR,
                        1200,
                        (int) this.getSpellPower(spellLevel, entity) / 2,
                        false,
                        false,
                        true
        ));
    }
}
