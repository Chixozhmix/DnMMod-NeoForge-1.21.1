package net.chixozhmix.dnmmod.Util;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;

public class SpellUtils {
    public static boolean checkSpellComponent(LivingEntity entity, Item item) {

        if(entity.getItemInHand(InteractionHand.MAIN_HAND).getItem() == item ||
                entity.getItemInHand(InteractionHand.OFF_HAND).getItem() == item || !(entity instanceof Player)) {
                return true;
        }

        // Если предмет не в руках, проверяем сумку компонентов
//        if(checkComponentBag(entity, item)) {
//            return true;
//        }
        if(entity instanceof Player player) {
            player.displayClientMessage(Component.translatable("ui.dnmmod.component_loss"), true);
        } else {
            entity.sendSystemMessage(Component.translatable("ui.dnmmod.component_loss"));
        }

        return false;
    }

//    private static boolean checkComponentBag(LivingEntity entity, Item item) {
//        if (!(entity instanceof Player player)) {
//            return false;
//        }
//
//        // Проверяем сумки в инвентаре
//        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
//            ItemStack stack = player.getInventory().getItem(i);
//            if (stack.getItem() instanceof ComponentBag || stack.getItem() instanceof MediumComponentBag) {
//                if (containsItemInBag(stack, item)) {
//                    return true;
//                }
//            }
//        }
//
//        return false;
//    }
//
//    private static boolean containsItemInBag(ItemStack bagStack, Item item) {
//        return bagStack.getCapability(ForgeCapabilities.ITEM_HANDLER).resolve()
//                .map(handler -> {
//                    // Проверяем все слоты сумки
//                    for (int i = 0; i < handler.getSlots(); i++) {
//                        ItemStack stackInSlot = handler.getStackInSlot(i);
//                        if (!stackInSlot.isEmpty() && stackInSlot.getItem() == item) {
//                            return true;
//                        }
//                    }
//                    return false;
//                })
//                .orElse(false);
//    }

    public static MutableComponent getComponentName(Item item) {
        return Component.translatable(item.getDescriptionId());
    }
}
