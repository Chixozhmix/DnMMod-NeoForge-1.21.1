package net.chixozhmix.neodnm.registers;

import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import net.chixozhmix.neodnm.DnMMod;
import net.chixozhmix.neodnm.spells.evocation.NightVisionSpell;
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
//    public static final DeferredRegister<AbstractSpell> CLOUD_DAGGER = registerSpell(new CloudDaggerSpell());
//    public static final DeferredRegister<AbstractSpell> ICE_DAGGER = registerSpell(new IceDaggerSpell());
//    public static final DeferredRegister<AbstractSpell> THUNDERWAVE = registerSpell(new ThunderwaveSpell());
//    public static final DeferredRegister<AbstractSpell> CAUSTIC_BREW = registerSpell(new CausticBrewSpell());
//    public static final DeferredRegister<AbstractSpell> CHROMATIC_ORB = registerSpell(new ChromaticOrbSpell());
//    public static final DeferredRegister<AbstractSpell> SUMMON_UNDEAD_SPIRIT = registerSpell(new SummonUndeadSpiritSpell());
//    public static final DeferredRegister<AbstractSpell> SUMMON_RAVEN = registerSpell(new SummonRavenSpell());
//    public static final DeferredRegister<AbstractSpell> SHRINKING = registerSpell(new ShrinkingSpell());
//    public static final DeferredRegister<AbstractSpell> AGATHYS_ARMOR_SPELL = registerSpell(new AgathysArmorSpell());
//    public static final DeferredRegister<AbstractSpell> THICK_OF_FIGHT = registerSpell(new ThickOfFightSpell());
//    public static final DeferredRegister<AbstractSpell> RAY_OF_ENFEEBLEMENT = registerSpell(new RayOfEnfeeblementSpell());
//    public static final DeferredRegister<AbstractSpell> SUMMON_FLAME_ATRONACH = registerSpell(new SummonFlameAtronach());
//    public static final DeferredRegister<AbstractSpell> CONTAGION_SPELL = registerSpell(new ContagionSpell());
//    public static final DeferredRegister<AbstractSpell> SUMMON_STORM_ATRONACH = registerSpell(new SummonStormAtronach());
//    public static final DeferredRegister<AbstractSpell> TALL_THE_DEAD = registerSpell(new TallTheDead());

    public static void register(IEventBus eventBus) {
        SPELLS.register(eventBus);
    }
}