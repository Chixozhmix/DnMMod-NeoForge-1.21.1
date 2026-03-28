package net.chixozhmix.neodnm.items.custom;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.IPresetSpellContainer;
import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import io.redspace.ironsspellbooks.api.spells.ISpellContainerMutable;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ForestHeart extends Item implements IPresetSpellContainer {
    public ForestHeart(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);

        tooltipComponents.add(Component.translatable("tooltip.dnmmod.forest_heart"));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pUsedHand);

        if (!pLevel.isClientSide()) {
            BlockPos belowPos = pPlayer.blockPosition().below();

            if (isValidGroundBlock(pLevel, belowPos)) {
                pPlayer.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 400, 1));
                pPlayer.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 600, 0));

                pPlayer.getCooldowns().addCooldown(this, 1200);

                return InteractionResultHolder.success(itemstack);
            }
        }

        return InteractionResultHolder.pass(itemstack);
    }

    private boolean isValidGroundBlock(Level level, BlockPos pos) {
        Block block = level.getBlockState(pos).getBlock();

        // Проверяем различные блоки земли и травы
        return block == Blocks.GRASS_BLOCK ||
                block == Blocks.DIRT ||
                block == Blocks.COARSE_DIRT ||
                block == Blocks.ROOTED_DIRT ||
                block == Blocks.PODZOL ||
                block == Blocks.MYCELIUM ||
                block == Blocks.MUD ||
                block == Blocks.MUDDY_MANGROVE_ROOTS;
    }

    @Override
    public void initializeSpellContainer(ItemStack itemStack) {
        if (itemStack != null && !ISpellContainer.isSpellContainer(itemStack)) {
            // Создаем изменяемый контейнер
            ISpellContainerMutable mutableContainer = ISpellContainer.create(1, true, false).mutableCopy();
            // Добавляем заклинание
            mutableContainer.addSpellAtIndex(SpellRegistry.ROOT_SPELL.get(), 7, 0, true);
            // Преобразуем в immutable и сохраняем
            ISpellContainer immutableContainer = mutableContainer.toImmutable();
            ISpellContainer.set(itemStack, immutableContainer);
        }
    }
}
