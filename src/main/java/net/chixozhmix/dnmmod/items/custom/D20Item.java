package net.chixozhmix.dnmmod.items.custom;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Random;

public class D20Item extends Item {

    public D20Item(Properties pProperties) {
        super(pProperties.stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if(!pLevel.isClientSide())
        {
            Random random = new Random();

            int diceRoll = random.nextInt(1, 21);

            pPlayer.sendSystemMessage(Component.literal(String.valueOf(diceRoll)));

            if(diceRoll == 1)
                pPlayer.hurt(pLevel.damageSources().magic(), 10);

            else if (diceRoll == 20)
                pPlayer.addEffect(new MobEffectInstance(
                        MobEffects.REGENERATION,
                        80,
                        2
                ));
        }

        return InteractionResultHolder.sidedSuccess(pPlayer.getItemInHand(pUsedHand), pLevel.isClientSide());
    }
}
