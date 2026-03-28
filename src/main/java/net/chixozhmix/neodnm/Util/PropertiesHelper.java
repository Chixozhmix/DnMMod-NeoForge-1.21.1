package net.chixozhmix.neodnm.Util;


import net.minecraft.world.item.Item;


public class PropertiesHelper {
    public static Item.Properties itemProperties() {
        return new Item.Properties();
    }

    public static Item.Properties durabilityItemProperties(int durability) {
        return new Item.Properties()
                .durability(durability);
    }

    public static Item.Properties stackItemProperties(int size) {
        return new Item.Properties()
                .stacksTo(size);
    }

//    public static void addOptionalItem(CreativeModeTab.Output output,
//                                       Optional<RegistryObject<Item>> optionalItem) {
//        optionalItem.ifPresent(regObj -> {
//            if (regObj.isPresent()) {
//                output.accept(regObj.get());
//            }
//        });
//    }
}
