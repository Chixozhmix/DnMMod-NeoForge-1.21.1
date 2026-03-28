package net.chixozhmix.dnmmod.items.custom;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class MirrorItem extends Item {
    public MirrorItem(Properties pProperties) {
        super(pProperties.stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        float healf = pPlayer.getHealth();

        if(healf == pPlayer.getMaxHealth())
            pPlayer.displayClientMessage(Component.translatable("ui.dnmmod.max_mirror_massage"), true);

        else if (healf < pPlayer.getMaxHealth() && healf > pPlayer.getMaxHealth() / 2)
            pPlayer.displayClientMessage(Component.translatable("ui.dnmmod.half_mirror_massage"), true);

        else if(healf < pPlayer.getMaxHealth() / 2)
            pPlayer.displayClientMessage(Component.translatable("ui.dnmmod.min_mirror_massage"), true);

        return super.use(pLevel, pPlayer, pUsedHand);
    }
}
