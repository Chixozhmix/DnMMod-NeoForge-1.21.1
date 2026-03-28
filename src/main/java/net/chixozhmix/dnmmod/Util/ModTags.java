package net.chixozhmix.dnmmod.Util;

import net.chixozhmix.dnmmod.DnMMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class ModTags {
//    public static final TagKey<Item> NECRO_FOCUS;
    public static final TagKey<Item> COMPONENT;

    static {
//        NECRO_FOCUS = TagKey.create(Registries.ITEM, new ResourceLocation(DnMmod.MOD_ID, "necro_focus"));
        COMPONENT = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(DnMMod.MOD_ID, "component"));
    }
}
