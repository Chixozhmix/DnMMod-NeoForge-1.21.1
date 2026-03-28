package net.chixozhmix.neodnm.entity.spell.ray_of_enfeeblement;

import net.chixozhmix.neodnm.registers.ModEntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.entity.IEntityWithComplexSpawn;

public class RayOfEnfeeblement extends Entity implements IEntityWithComplexSpawn {
    public static final int lifetime = 15;
    public float distance;

    public RayOfEnfeeblement(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public RayOfEnfeeblement(Level level, Vec3 start, Vec3 end, LivingEntity owner) {
        super((EntityType<?>) ModEntityType.RAY_OF_ENFEEBLEMENT.get(), level);
        this.setPos(start.subtract(0.0, 0.75, 0.0));
        this.distance = (float) start.distanceTo(end);
        this.setRot(owner.getYRot(), owner.getXRot());
    }

    @Override
    public void tick() {
        if (++this.tickCount > lifetime) {
            this.discard();
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {

    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity entity) {
        return super.getAddEntityPacket(entity);
    }

    @Override
    public void writeSpawnData(RegistryFriendlyByteBuf registryFriendlyByteBuf) {
        registryFriendlyByteBuf.writeInt((int) (this.distance * 10.0F));
    }

    @Override
    public void readSpawnData(RegistryFriendlyByteBuf registryFriendlyByteBuf) {
        this.distance = (float) registryFriendlyByteBuf.readInt() / 10.0F;
    }
}
