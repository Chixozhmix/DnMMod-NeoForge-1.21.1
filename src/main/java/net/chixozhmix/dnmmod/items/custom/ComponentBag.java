package net.chixozhmix.dnmmod.items.custom;

import net.chixozhmix.dnmmod.screen.component_bag.ComponentBagMenu;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuConstructor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.ItemStackHandler;

import java.util.List;

public class ComponentBag extends Item {
    private static final int INVENTORY_SIZE = 9;

    public ComponentBag(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);

        if (!level.isClientSide()) {
            // Создаём обработчик для хранения предметов
            // В NeoForge 1.21.1 используем ItemContainerContents для хранения в компонентах
            ItemContainerContents contents = itemStack.get(DataComponents.CONTAINER);

            if (contents == null) {
                // Если нет компонента контейнера, создаём пустой
                contents = ItemContainerContents.EMPTY;
            }

            // Создаём обработчик на основе компонента
            ItemStackHandler handler = new ItemStackHandler(INVENTORY_SIZE);

            // Загружаем существующие предметы в обработчик
            if (contents != ItemContainerContents.EMPTY) {
                List<ItemStack> items = contents.stream().toList();
                for (int i = 0; i < Math.min(items.size(), INVENTORY_SIZE); i++) {
                    if (!items.get(i).isEmpty()) {
                        handler.setStackInSlot(i, items.get(i).copy());
                    }
                }
            }

            // Создаём меню и открываем его
            MenuConstructor menuConstructor = (containerId, playerInventory, serverPlayer) ->
                    new ComponentBagMenu(containerId, playerInventory, itemStack);

            player.openMenu(new SimpleMenuProvider(menuConstructor,
                            Component.translatable("container.component_bag")),
                    buf -> {
                        ItemStack.STREAM_CODEC.encode((RegistryFriendlyByteBuf) buf, itemStack);
                    });
        }

        return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide());
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return false;
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return false;
    }
}
