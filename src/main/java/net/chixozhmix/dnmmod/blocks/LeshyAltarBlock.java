package net.chixozhmix.dnmmod.blocks;

import net.chixozhmix.dnmmod.entity.leshy.LeshyEntity;
import net.chixozhmix.dnmmod.registers.ModEntityType;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class LeshyAltarBlock extends Block {
    public static final VoxelShape SHAPE = Block.box(0,0,0,16,16,16);

    public LeshyAltarBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected boolean useShapeForLightOcclusion(BlockState state) {
        return true;
    }
    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }
    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }
    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if(!level.isClientSide && !state.is(newState.getBlock())) {
            destroyBlocksInRadius((ServerLevel) level, pos, 3);
            spawnCreature((ServerLevel) level, pos);
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    private void spawnCreature(ServerLevel level, BlockPos pos) {
        LeshyEntity leshy = ModEntityType.LESHY.get().create(level);

        if(leshy != null) {
            leshy.setPos(pos.getX() + 0.5f, pos.getY() + 1, pos.getZ() + 0.5f);

            level.addFreshEntity(leshy);
        }
    }

    private void destroyBlocksInRadius(ServerLevel level, BlockPos centerPos, int radius) {
        // Перебираем все блоки в кубе с радиусом
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    BlockPos targetPos = centerPos.offset(x, y, z);
                    BlockState blockState = level.getBlockState(targetPos);

                    // Проверяем, что блок не является воздухом и не является самим алтарем
                    if (!blockState.isAir() && !targetPos.equals(centerPos) && y >= 0) {
                        // Удаляем блок, вызывая его деструкцию с дропом предметов
                        level.destroyBlock(targetPos, false);
                    }
                }
            }
        }
    }
}
