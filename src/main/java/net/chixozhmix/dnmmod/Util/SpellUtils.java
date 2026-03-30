package net.chixozhmix.dnmmod.Util;

import net.chixozhmix.dnmmod.items.custom.ComponentBag;
import net.chixozhmix.dnmmod.items.custom.MediumComponentBag;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;

public class SpellUtils {
    public static boolean checkSpellComponent(LivingEntity entity, Item item) {
        // Проверяем руки
        if (entity.getItemInHand(InteractionHand.MAIN_HAND).getItem() == item ||
                entity.getItemInHand(InteractionHand.OFF_HAND).getItem() == item) {
            return true;
        }

        // Если это не игрок, то только руки
        if (!(entity instanceof Player player)) {
            return false;
        }

        // Проверяем сумку компонентов
        if (checkComponentBag(player, item)) {
            return true;
        }

        // Если компонент не найден, показываем сообщение
        player.displayClientMessage(Component.translatable("ui.dnmmod.component_loss"), true);
        return false;
    }

    private static boolean checkComponentBag(Player player, Item item) {
        // Проверяем все предметы в инвентаре
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (stack.getItem() instanceof ComponentBag || stack.getItem() instanceof MediumComponentBag) {
                if (containsItemInBag(stack, item)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean containsItemInBag(ItemStack bagStack, Item item) {
        if (bagStack.isEmpty()) {
            return false;
        }

        // Получаем содержимое из компонента контейнера (новый способ в NeoForge 1.21.1)
        ItemContainerContents contents = bagStack.get(DataComponents.CONTAINER);
        if (contents != null && contents != ItemContainerContents.EMPTY) {
            // Используем stream() для итерации
            for (ItemStack stackInSlot : contents.stream().toList()) {
                if (!stackInSlot.isEmpty() && stackInSlot.getItem() == item) {
                    return true;
                }
            }
        }

        // Для обратной совместимости также проверяем через Capability
        // (на случай, если где-то используется старый способ)
        IItemHandler handler = bagStack.getCapability(Capabilities.ItemHandler.ITEM);
        if (handler != null) {
            for (int i = 0; i < handler.getSlots(); i++) {
                ItemStack stackInSlot = handler.getStackInSlot(i);
                if (!stackInSlot.isEmpty() && stackInSlot.getItem() == item) {
                    return true;
                }
            }
        }

        return false;
    }

    public static MutableComponent getComponentName(Item item) {
        return Component.translatable(item.getDescriptionId());
    }
}
