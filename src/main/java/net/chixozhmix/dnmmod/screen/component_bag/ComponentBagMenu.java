package net.chixozhmix.dnmmod.screen.component_bag;

import net.chixozhmix.dnmmod.Util.ModTags;
import net.chixozhmix.dnmmod.items.custom.ComponentBag;
import net.chixozhmix.dnmmod.items.custom.MediumComponentBag;
import net.chixozhmix.dnmmod.registers.ModMenuTypes;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

import java.util.ArrayList;
import java.util.List;


public class ComponentBagMenu extends AbstractContainerMenu {
    private final ItemStackHandler itemHandler;
    private final ItemStack bagItemStack;
    private final Player player;

    private static final int BAG_SLOT_COUNT = 9;
    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int BAG_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;

    public ComponentBagMenu(int containerId, Inventory playerInventory, RegistryFriendlyByteBuf extraData) {
        this(containerId, playerInventory, ItemStack.STREAM_CODEC.decode(extraData));
    }

    public ComponentBagMenu(int containerId, Inventory playerInventory, ItemStack bagItemStack) {
        super(ModMenuTypes.COMPONENT_BAG_MENU.get(), containerId);

        this.player = playerInventory.player;
        this.bagItemStack = bagItemStack;

        // Создаём обработчик и загружаем в него предметы из компонента
        this.itemHandler = new ItemStackHandler(BAG_SLOT_COUNT);

        // Загружаем предметы из компонента контейнера
        ItemContainerContents contents = bagItemStack.get(DataComponents.CONTAINER);
        if (contents != null && contents != ItemContainerContents.EMPTY) {
            java.util.List<ItemStack> items = contents.stream().toList();
            for (int i = 0; i < Math.min(items.size(), BAG_SLOT_COUNT); i++) {
                if (!items.get(i).isEmpty()) {
                    itemHandler.setStackInSlot(i, items.get(i).copy());
                }
            }
        }

        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
        addBagSlots();
    }

    private void addBagSlots() {
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 3; ++col) {
                int x = 62 + col * 18;
                int y = 17 + row * 18;
                int slotIndex = col + row * 3;
                this.addSlot(new SlotItemHandler(itemHandler, slotIndex, x, y) {
                    @Override
                    public boolean mayPlace(ItemStack stack) {
                        return stack.is(ModTags.COMPONENT) && !(stack.getItem() instanceof MediumComponentBag) &&
                                !(stack.getItem() instanceof ComponentBag);
                    }

                    @Override
                    public void set(ItemStack stack) {
                        super.set(stack);
                        saveToContainer();
                    }

                    @Override
                    public ItemStack remove(int amount) {
                        ItemStack result = super.remove(amount);
                        saveToContainer();
                        return result;
                    }
                });
            }
        }
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                int x = 8 + col * 18;
                int y = 84 + row * 18;
                int slotIndex = col + row * 9 + 9;
                this.addSlot(new Slot(playerInventory, slotIndex, x, y));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int col = 0; col < 9; ++col) {
            int x = 8 + col * 18;
            int y = 142;
            this.addSlot(new Slot(playerInventory, col, x, y));
        }
    }

    private void saveToContainer() {
        // Сохраняем содержимое обработчика в компонент предмета
        List<ItemStack> items = new ArrayList<>(BAG_SLOT_COUNT);
        for (int i = 0; i < BAG_SLOT_COUNT; i++) {
            items.add(itemHandler.getStackInSlot(i));
        }
        bagItemStack.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(items));
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        // Сохраняем изменения при закрытии меню
        if (!player.level().isClientSide()) {
            saveToContainer();
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot sourceSlot = slots.get(index);
        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;

        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        if (index < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
            if (!sourceStack.is(ModTags.COMPONENT)) {
                return ItemStack.EMPTY;
            }

            if (sourceStack.getItem() instanceof MediumComponentBag ||
                    sourceStack.getItem() instanceof ComponentBag) {
                return ItemStack.EMPTY;
            }

            if (!moveItemStackTo(sourceStack, BAG_FIRST_SLOT_INDEX, BAG_FIRST_SLOT_INDEX + BAG_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else if (index < BAG_FIRST_SLOT_INDEX + BAG_SLOT_COUNT) {
            if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            return ItemStack.EMPTY;
        }

        if (sourceStack.getCount() == 0) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }

        sourceSlot.onTake(player, sourceStack);
        saveToContainer();
        return copyOfSourceStack;
    }

    @Override
    public boolean stillValid(Player player) {
        return player.getMainHandItem().getItem() instanceof ComponentBag ||
                player.getOffhandItem().getItem() instanceof ComponentBag;
    }

    public ItemStackHandler getItemHandler() {
        return itemHandler;
    }

    public ItemStack getBagItemStack() {
        return bagItemStack;
    }

    public int getBagSlotCount() {
        return BAG_SLOT_COUNT;
    }
}
