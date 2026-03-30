package net.chixozhmix.dnmmod.registers;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import net.chixozhmix.dnmmod.DnMMod;
import net.chixozhmix.dnmmod.effect.custom.*;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModEffects {
    public static final DeferredRegister<MobEffect> EFFECTS =
            DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, DnMMod.MOD_ID);

    public static final Holder<MobEffect> MAGE_ARMOR =
            EFFECTS.register("mage_armor", MageArmorEffect::new);
    public static final Holder<MobEffect> ACID =
            EFFECTS.register("acid_effect", () -> new AcidEffect(MobEffectCategory.HARMFUL, 0x4CAF50, 1.0f));
    public static final Holder<MobEffect> PHANTOM_EFFECT =
            EFFECTS.register("phantom_effect", () -> new PhantomEffect(MobEffectCategory.BENEFICIAL, 0x78938c));
    public static final Holder<MobEffect> SHRINK_EFFECT = EFFECTS.register("shrink_effect", () -> new ShrinkEffect());
    public static final Holder<MobEffect> AGATHYS_ARMOR = EFFECTS.register("agathys_armor", () -> (new AgathysArmor()).addAttributeModifier(Attributes.MAX_ABSORPTION, IronsSpellbooks.id("mobeffect_fortify"), (double)1.0F, AttributeModifier.Operation.ADD_VALUE));
    public static final Holder<MobEffect> THICK_OF_FIGHT = EFFECTS.register("thick_of_fight", ThickOfFight::new);
    public static final Holder<MobEffect> CORPSE_POISON = EFFECTS.register("corpse_poison", () -> new CorpsePoison(1));

    public static void register(IEventBus eventBus) {
        EFFECTS.register(eventBus);
    }
}
