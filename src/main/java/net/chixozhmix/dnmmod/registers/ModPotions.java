package net.chixozhmix.dnmmod.registers;

import net.chixozhmix.dnmmod.DnMMod;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModPotions {
    public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(Registries.POTION, DnMMod.MOD_ID);

    public static final Holder<Potion> CORPSE_POISON = POTIONS.register("corpse_poison", () ->
            new Potion(new MobEffectInstance(ModEffects.CORPSE_POISON, 200, 0)));
    public static final Holder<Potion> PHANTOM_POTION = POTIONS.register("phantom_potion", () ->
            new Potion(new MobEffectInstance(ModEffects.PHANTOM_EFFECT, 900, 0)));

    public static void register(IEventBus eventBus) {
        POTIONS.register(eventBus);
    }
}
