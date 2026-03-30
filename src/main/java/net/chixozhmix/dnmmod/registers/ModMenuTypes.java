package net.chixozhmix.dnmmod.registers;

import net.chixozhmix.dnmmod.DnMMod;
import net.chixozhmix.dnmmod.screen.component_bag.ComponentBagMenu;
import net.chixozhmix.dnmmod.screen.medium_bag.MediumBagMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.network.IContainerFactory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModMenuTypes {
    // Регистрируем DeferredRegister для MenuType
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(Registries.MENU, DnMMod.MOD_ID);

    public static final Supplier<MenuType<ComponentBagMenu>> COMPONENT_BAG_MENU =
            registerMenuType("component_bag_menu", ComponentBagMenu::new);
    public static final Supplier<MenuType<MediumBagMenu>> MEDIUM_COMPONENT_BAG_MENU =
            registerMenuType("medium_component_bag_menu", MediumBagMenu::new);

    // Метод для регистрации MenuType
    private static <T extends AbstractContainerMenu> DeferredHolder<MenuType<?>, MenuType<T>> registerMenuType(String name, IContainerFactory<T> factory) {
        return MENUS.register(name, () -> IMenuTypeExtension.create(factory));
    }

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}