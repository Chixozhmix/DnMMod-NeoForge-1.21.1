package net.chixozhmix.dnmmod.registers;

import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import net.chixozhmix.dnmmod.DnMMod;
import net.chixozhmix.dnmmod.spells.blood.ContagionSpell;
import net.chixozhmix.dnmmod.spells.blood.RayOfEnfeeblementSpell;
import net.chixozhmix.dnmmod.spells.blood.TallTheDead;
import net.chixozhmix.dnmmod.spells.evocation.CausticBrewSpell;
import net.chixozhmix.dnmmod.spells.evocation.ChromaticOrbSpell;
import net.chixozhmix.dnmmod.spells.evocation.NightVisionSpell;
import net.chixozhmix.dnmmod.spells.fire.SummonFlameAtronach;
import net.chixozhmix.dnmmod.spells.ice.IceDaggerSpell;
import net.chixozhmix.dnmmod.spells.lightning.SummonStormAtronach;
import net.chixozhmix.dnmmod.spells.lightning.ThunderwaveSpell;
import net.chixozhmix.dnmmod.spells.nature.SummonRavenSpell;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class RegistrySpells {
    public static final DeferredRegister<AbstractSpell> SPELLS = DeferredRegister.create(io.redspace.ironsspellbooks.api.registry.SpellRegistry.SPELL_REGISTRY_KEY, DnMMod.MOD_ID);

    public static Supplier<AbstractSpell> registerSpell(AbstractSpell spell) {
        return SPELLS.register(spell.getSpellName(), () -> spell);
    }

//    public static final DeferredRegister<AbstractSpell> MAGE_ARMOR = registerSpell(new MageArmorSpell());
    public static final Supplier<AbstractSpell> NIGHT_VISION = registerSpell(new NightVisionSpell());
    public static final Supplier<AbstractSpell> ICE_DAGGER = registerSpell(new IceDaggerSpell());
    public static final Supplier<AbstractSpell> THUNDERWAVE = registerSpell(new ThunderwaveSpell());
    public static final Supplier<AbstractSpell> CAUSTIC_BREW = registerSpell(new CausticBrewSpell());
    public static final Supplier<AbstractSpell> CHROMATIC_ORB = registerSpell(new ChromaticOrbSpell());
//    public static final Supplier<AbstractSpell> SUMMON_UNDEAD_SPIRIT = registerSpell(new SummonUndeadSpiritSpell());
    public static final Supplier<AbstractSpell> SUMMON_RAVEN = registerSpell(new SummonRavenSpell());
//    public static final Supplier<AbstractSpell> SHRINKING = registerSpell(new ShrinkingSpell());
//    public static final Supplier<AbstractSpell> AGATHYS_ARMOR_SPELL = registerSpell(new AgathysArmorSpell());
//    public static final Supplier<AbstractSpell> THICK_OF_FIGHT = registerSpell(new ThickOfFightSpell());
    public static final Supplier<AbstractSpell> RAY_OF_ENFEEBLEMENT = registerSpell(new RayOfEnfeeblementSpell());
    public static final Supplier<AbstractSpell> SUMMON_FLAME_ATRONACH = registerSpell(new SummonFlameAtronach());
    public static final Supplier<AbstractSpell> CONTAGION_SPELL = registerSpell(new ContagionSpell());
    public static final Supplier<AbstractSpell> SUMMON_STORM_ATRONACH = registerSpell(new SummonStormAtronach());
    public static final Supplier<AbstractSpell> TALL_THE_DEAD = registerSpell(new TallTheDead());

    public static void register(IEventBus eventBus) {
        SPELLS.register(eventBus);
    }
}