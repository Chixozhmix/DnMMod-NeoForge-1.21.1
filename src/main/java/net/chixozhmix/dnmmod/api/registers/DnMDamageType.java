package net.chixozhmix.dnmmod.api.registers;

import net.chixozhmix.dnmmod.DnMMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;

public class DnMDamageType {
    public static final ResourceKey<DamageType> NECRO_MAGIC;
    public static final ResourceKey<DamageType> AQUA_MAGIC;

    static {
        NECRO_MAGIC = ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(DnMMod.MOD_ID, "necro_magic"));
        AQUA_MAGIC = ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(DnMMod.MOD_ID, "aqua_magic"));
    }
}
