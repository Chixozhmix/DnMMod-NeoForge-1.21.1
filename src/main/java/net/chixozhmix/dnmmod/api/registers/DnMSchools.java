package net.chixozhmix.dnmmod.api.registers;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.SchoolType;
import net.chixozhmix.dnmmod.DnMMod;
import net.chixozhmix.dnmmod.Util.ModTags;
import net.chixozhmix.dnmmod.registers.SoundsRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import java.util.function.Supplier;

public class DnMSchools {

    public static final ResourceLocation NECRO_RESOURCE = DnMMod.id("necro");
    public static final ResourceLocation AQUA_RESOURCE = DnMMod.id("aqua");

    private static final DeferredRegister<SchoolType> SCHOOLS = DeferredRegister.create(SchoolRegistry.SCHOOL_REGISTRY_KEY, DnMMod.MOD_ID);

    public static void register(IEventBus eventBus) {
        SCHOOLS.register(eventBus);
    }

    private static Supplier<SchoolType> registerSchool(SchoolType schoolType) {
        return SCHOOLS.register(schoolType.getId().getPath(), () -> schoolType);
    }

    public static final Supplier<SchoolType> NECRO = registerSchool(new SchoolType(
            NECRO_RESOURCE,
            ModTags.NECRO_FOCUS,
            Component.translatable("school.dnmmod.necro").withStyle(Style.EMPTY.withColor(0x59147e)),
            DnMAttributes.NECRO_SPELL_POWER,
            DnMAttributes.NECRO_MAGIC_RESIST,
            SoundsRegistry.NECRO_MAGIC,
            DnMDamageType.NECRO_MAGIC
    ));
    public static final Supplier<SchoolType> AQUA = registerSchool(new SchoolType(
            AQUA_RESOURCE,
            ModTags.AQUA_FOCUS,
            Component.translatable("school.dnmmod.aqua").withStyle(ChatFormatting.AQUA),
            DnMAttributes.AQUA_SPELL_POWER,
            DnMAttributes.AQUA_MAGIC_RESIST,
            SoundsRegistry.NECRO_MAGIC,
            DnMDamageType.AQUA_MAGIC
    ));
}
