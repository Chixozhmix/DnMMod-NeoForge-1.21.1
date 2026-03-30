package net.chixozhmix.dnmmod.api.registers;


import io.redspace.ironsspellbooks.api.attribute.MagicRangedAttribute;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

@EventBusSubscriber
public class DnMAttributes {
    public static final DeferredRegister<Attribute> ATTRIBUTES;
    public static final Holder<Attribute> NECRO_MAGIC_RESIST;
    public static final Holder<Attribute> AQUA_MAGIC_RESIST;
    public static final Holder<Attribute> NECRO_SPELL_POWER;
    public static final Holder<Attribute> AQUA_SPELL_POWER;

    public static Holder<Attribute> necroResistanceAttribute(String id) {
        return ATTRIBUTES.register(id + "_magic_resist", () -> (new MagicRangedAttribute("attribute.dnmmod." + id + "_magic_resist", (double)1.0F, (double)-100.0F, (double)100.0F)).setSyncable(true));
    }

    public static Holder<Attribute> necroPowerAttribute(String id) {
        return ATTRIBUTES.register(id + "_spell_power", () -> (new MagicRangedAttribute("attribute.dnmmod." + id + "_spell_power", (double)1.0F, (double)-100.0F, (double)100.0F)).setSyncable(true));
    }

    @SubscribeEvent
    public static void onEntityAttributeModification(EntityAttributeModificationEvent event) {
        event.getTypes().forEach((entity) -> {
            event.add(entity, NECRO_SPELL_POWER);
            event.add(entity, AQUA_SPELL_POWER);
            event.add(entity, NECRO_MAGIC_RESIST);
            event.add(entity, AQUA_MAGIC_RESIST);
        });
    }

    static {
        ATTRIBUTES = DeferredRegister.create(BuiltInRegistries.ATTRIBUTE, "dnmmod");
        NECRO_MAGIC_RESIST = necroResistanceAttribute("necro");
        AQUA_MAGIC_RESIST = necroResistanceAttribute("aqua");
        NECRO_SPELL_POWER = necroPowerAttribute("necro");
        AQUA_SPELL_POWER = necroPowerAttribute("aqua");
    }
}
