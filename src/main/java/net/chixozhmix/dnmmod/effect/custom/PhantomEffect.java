package net.chixozhmix.dnmmod.effect.custom;

import net.chixozhmix.dnmmod.registers.ModEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

public class PhantomEffect extends MobEffect {
    public PhantomEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public boolean applyEffectTick(LivingEntity livingEntity, int amplifier) {
        if (livingEntity instanceof Player player) {
            // Применяем эффект только если он активен
            if (player.hasEffect(ModEffects.PHANTOM_EFFECT)) {
                player.getAbilities().mayfly = true;
                player.getAbilities().flying = true;
                player.onUpdateAbilities();

                player.noPhysics = true;
                player.setNoGravity(true);
                player.fallDistance = 0.0f;

                preventBedrockPassing(player);
            }
        }
        return super.applyEffectTick(livingEntity, amplifier);
    }

    private void preventBedrockPassing(Player player) {
        Level level = player.level();
        AABB playerBounds = player.getBoundingBox();

        BlockPos.betweenClosed(
                BlockPos.containing(playerBounds.minX, playerBounds.minY, playerBounds.minZ),
                BlockPos.containing(playerBounds.maxX, playerBounds.maxY, playerBounds.maxZ)
        ).forEach(pos -> {
            BlockState blockState = level.getBlockState(pos);

            if (isForbiddenBlock(blockState)) {
                pushPlayerOutOfBlock(player, pos, blockState);
            }
        });
    }

    private void pushPlayerOutOfBlock(Player player, BlockPos blockPos, BlockState blockState) {
        AABB blockBounds = blockState.getShape(player.level(), blockPos).bounds().move(blockPos);
        AABB playerBounds = player.getBoundingBox();

        double pushX = 0;
        double pushY = 0;
        double pushZ = 0;

        if (playerBounds.intersects(blockBounds)) {
            double blockCenterX = blockPos.getX() + 0.5;
            double blockCenterY = blockPos.getY() + 0.5;
            double blockCenterZ = blockPos.getZ() + 0.5;

            double playerCenterX = player.getX();
            double playerCenterY = player.getY() + player.getEyeHeight();
            double playerCenterZ = player.getZ();

            pushX = playerCenterX - blockCenterX;
            pushY = playerCenterY - blockCenterY;
            pushZ = playerCenterZ - blockCenterZ;

            double length = Math.sqrt(pushX * pushX + pushY * pushY + pushZ * pushZ);
            if (length > 0) {
                double speed = 0.3;
                pushX = pushX / length * speed;
                pushY = pushY / length * speed;
                pushZ = pushZ / length * speed;

                player.setDeltaMovement(pushX, pushY, pushZ);
            }
        }
    }

    private boolean isForbiddenBlock(BlockState blockState) {
        return blockState.getBlock() == Blocks.BEDROCK ||
                blockState.getBlock() == Blocks.BARRIER ||
                blockState.getBlock() == Blocks.COMMAND_BLOCK ||
                blockState.getBlock() == Blocks.STRUCTURE_BLOCK ||
                blockState.getBlock() == Blocks.JIGSAW ||
                blockState.getBlock() == Blocks.OBSIDIAN ||
                blockState.getBlock() == Blocks.CRYING_OBSIDIAN;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }

    @EventBusSubscriber
    public static class PhantomEffectHandler {

        @SubscribeEvent
        public static void onEffectExpired(MobEffectEvent.Expired event) {
            if (event.getEffectInstance() != null && event.getEffectInstance().getEffect() instanceof PhantomEffect) {
                if (event.getEntity() instanceof Player player) {
                    resetPlayerPhysics(player);
                }
            }
        }

        @SubscribeEvent
        public static void onEffectRemoved(MobEffectEvent.Remove event) {
            if (event.getEffectInstance() != null && event.getEffectInstance().getEffect() instanceof PhantomEffect) {
                if (event.getEntity() instanceof Player player) {
                    resetPlayerPhysics(player);
                }
            }
        }

        // Добавляем дополнительную проверку в каждом тике для подстраховки
        @SubscribeEvent
        public static void onPlayerTick(PlayerTickEvent.Post event) {
            Player player = event.getEntity();
            MobEffectInstance effect = player.getEffect(ModEffects.PHANTOM_EFFECT);

            // Если эффекта нет, но игрок всё ещё имеет способности от эффекта - сбрасываем
            if (effect == null || effect.getEffect() == null) {
                if (player.getAbilities().mayfly && !player.isCreative() && !player.isSpectator()) {
                    resetPlayerPhysics(player);
                }
                if (player.noPhysics || player.isNoGravity()) {
                    resetPlayerPhysics(player);
                }
            }
        }

        private static void resetPlayerPhysics(Player player) {
            // Сохраняем информацию о том, был ли игрок в творческом режиме до этого
            boolean wasFlying = player.getAbilities().flying;

            // Сбрасываем способности только если игрок не в творческом и не в режиме наблюдателя
            if (!player.isCreative() && !player.isSpectator()) {
                player.getAbilities().mayfly = false;
                player.getAbilities().flying = false;
                player.onUpdateAbilities();
            }

            // Всегда сбрасываем физику
            player.setNoGravity(false);
            player.noPhysics = false;
            player.fallDistance = 0.0f;

            // Если игрок был в полёте и эффект пропал, применяем небольшую гравитацию
            if (wasFlying && !player.isCreative() && !player.isSpectator()) {
                if (!player.onGround()) {
                    player.setDeltaMovement(player.getDeltaMovement().x, -0.5, player.getDeltaMovement().z);
                }
            }
        }
    }
}
