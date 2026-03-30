package net.chixozhmix.dnmmod.items.custom;

import net.chixozhmix.dnmmod.screen.medium_bag.MediumBagMenu;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuConstructor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class MediumComponentBag extends Item {

    private static final int INVENTORY_SIZE = 15;

    public MediumComponentBag(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);

        if (!level.isClientSide()) {
            // Создаём меню и открываем его
            MenuConstructor menuConstructor = (containerId, playerInventory, serverPlayer) ->
                    new MediumBagMenu(containerId, playerInventory, itemStack);

            player.openMenu(new SimpleMenuProvider(menuConstructor,
                            Component.translatable("container.medium_component_bag")),
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
