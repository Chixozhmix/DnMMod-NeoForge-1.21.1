package net.chixozhmix.dnmmod.spells.evocation;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.Utils;
import net.chixozhmix.dnmmod.DnMMod;
import net.chixozhmix.dnmmod.Util.SpellUtils;
import net.chixozhmix.dnmmod.entity.spell.chromatic_orb.ChromaticOrb;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class ChromaticOrbSpell extends AbstractSpell {

    private static ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(DnMMod.MOD_ID, "chromatic_orb");

    public ChromaticOrbSpell() {
        this.baseManaCost = 50;
        this.baseSpellPower = 6;
        this.castTime = 15;
        this.manaCostPerLevel = 10;
        this.spellPowerPerLevel = 1;
    }

    private DefaultConfig defaultConfig = new DefaultConfig()
            .setMaxLevel(10)
            .setCooldownSeconds(30)
            .setMinRarity(SpellRarity.UNCOMMON)
            .setSchoolResource(SchoolRegistry.EVOCATION_RESOURCE)
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
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(
                Component.translatable("ui.dnmmod.bounces", new Object[]{Utils.stringTruncation((double)this.getBounces(spellLevel), 1)}),
                Component.translatable("ui.irons_spellbooks.damage", new Object[]{Utils.stringTruncation((double)this.getDamage(spellLevel, caster), 2)}),
                Component.translatable("ui.dnmmod.spell_component", new Object[]{SpellUtils.getComponentName(Items.DIAMOND)})
        );
    }

    @Override
    public boolean checkPreCastConditions(Level level, int spellLevel, LivingEntity entity, MagicData playerMagicData) {
        return SpellUtils.checkSpellComponent(entity, Items.DIAMOND);
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        super.onCast(level, spellLevel, entity, castSource, playerMagicData);

        ChromaticOrb orb = new ChromaticOrb(level, entity);
        Vec3 spawn = entity.getEyePosition().add(entity.getForward().normalize());
        orb.setPos(spawn);
        Vec3 direction = entity.getLookAngle().normalize();
        orb.setDeltaMovement(direction.multiply(orb.getSpeed(), orb.getSpeed(), orb.getSpeed()));
        orb.setDamage(getDamage(spellLevel, entity));
        orb.setBounces(getBounces(spellLevel));
        level.addFreshEntity(orb);
    }

    private float getDamage(int spellLevel, LivingEntity entity) {
        return this.getSpellPower(spellLevel, entity) * 1.7F;
    }

    private int getBounces(int spellLevel) {
        return 2 + (spellLevel / 3);
    }
}
