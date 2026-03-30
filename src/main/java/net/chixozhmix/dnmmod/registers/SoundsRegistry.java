package net.chixozhmix.dnmmod.registers;

import net.chixozhmix.dnmmod.DnMMod;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class SoundsRegistry {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, DnMMod.MOD_ID);

    public static final Supplier<SoundEvent> GHOST_AMBIENT = registerSoundEvent("ghost_ambient");
    public static final Supplier<SoundEvent> SUMMON_UNDEAD_SPIRIT = registerSoundEvent("summon_undead_spirit");
    public static final Supplier<SoundEvent> UNDEAD_SPIRIT = registerSoundEvent("undead_spirit");
    public static final Supplier<SoundEvent> RAVEN_AMBIENT = registerSoundEvent("raven_ambient");
    public static final Supplier<SoundEvent> SUMMON_RAVEN = registerSoundEvent("summon_raven");
    public static final Supplier<SoundEvent> GOBLIN_AMBIENT = registerSoundEvent("goblin_ambient");
    public static final Supplier<SoundEvent> GOBLIN_HURT = registerSoundEvent("goblin_hurt");
    public static final Supplier<SoundEvent> LESHY_AMBIENT = registerSoundEvent("leshy_ambient");
    public static final Supplier<SoundEvent> GREEMON_AMBIENT = registerSoundEvent("greemon_ambient");
    public static final Holder<SoundEvent> NECRO_MAGIC = registerSoundEventHolder("necro_magic");
    public static final Supplier<SoundEvent> TALL_THE_DEAD = registerSoundEvent("tall_the_dead");


    private static Supplier<SoundEvent> registerSoundEvent(String name) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(DnMMod.MOD_ID, name);
        return SOUNDS.register(name, () -> SoundEvent.createVariableRangeEvent(id));
    }

    private static Holder<SoundEvent> registerSoundEventHolder(String name) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(DnMMod.MOD_ID, name);
        return SOUNDS.register(name, () -> SoundEvent.createVariableRangeEvent(id));
    }

    public static void register(IEventBus eventBus) {
        SOUNDS.register(eventBus);
    }
}
