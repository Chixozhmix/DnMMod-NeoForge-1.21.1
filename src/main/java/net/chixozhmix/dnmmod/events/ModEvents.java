package net.chixozhmix.dnmmod.events;


import net.chixozhmix.dnmmod.DnMMod;
import net.chixozhmix.dnmmod.entity.custom.UndeadSpiritEntity;
import net.chixozhmix.dnmmod.entity.flame_atronach.FlameAtronachEntity;
import net.chixozhmix.dnmmod.entity.ghost.GhostEntity;
import net.chixozhmix.dnmmod.entity.goblin_shaman.GoblinShamanEntity;
import net.chixozhmix.dnmmod.entity.goblin_warior.GoblinWariorEntity;
import net.chixozhmix.dnmmod.entity.greemon.GreemonEntity;
import net.chixozhmix.dnmmod.entity.green_hag.GreenHagEntity;
import net.chixozhmix.dnmmod.entity.leshy.LeshyEntity;
import net.chixozhmix.dnmmod.entity.raven.RavenEntity;
import net.chixozhmix.dnmmod.entity.storm_atronach.StormAtronach;
import net.chixozhmix.dnmmod.entity.summoned.SummonedRavenEntity;
import net.chixozhmix.dnmmod.registers.ModEntityType;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;

@Mod(value = DnMMod.MOD_ID)
@EventBusSubscriber(modid = DnMMod.MOD_ID)
public class ModEvents {

    @SubscribeEvent
    public static void entityAttributeEvent(EntityAttributeCreationEvent event) {
        event.put(ModEntityType.UNDEAD_SPIRIT.get(), UndeadSpiritEntity.createAttributes());
//        event.put(ModEntityType.SUMMONED_UNDEAD_SPIRIT.get(), SummonedUndeadSpirit.createAttributes());
        event.put(ModEntityType.GOBLIN_SHAMAN.get(), GoblinShamanEntity.prepareAttributes().build());
        event.put(ModEntityType.GREEN_HAG.get(), GreenHagEntity.prepareAttributes().build());
        event.put(ModEntityType.RAVEN.get(), RavenEntity.createAttributes());
        event.put(ModEntityType.SUMMON_RAVEN.get(), SummonedRavenEntity.createAttributes());
        event.put(ModEntityType.LESHY.get(), LeshyEntity.prepareAttributes().build());
        event.put(ModEntityType.GHOST.get(), GhostEntity.createAttributes());
        event.put(ModEntityType.GREEMON.get(), GreemonEntity.createAttributes());
        event.put(ModEntityType.GOBLIN_WARRIOR.get(), GoblinWariorEntity.createAttributes());
        event.put(ModEntityType.FLAME_ATRONACH.get(), FlameAtronachEntity.prepareAttributes().build());
        event.put(ModEntityType.STORM_ATRONACH.get(), StormAtronach.prepareAttributes().build());
    }

    @SubscribeEvent
    public static void registerSpawnPlacements(RegisterSpawnPlacementsEvent event) {
        event.register(ModEntityType.GREEMON.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Monster::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(ModEntityType.GOBLIN_WARRIOR.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Monster::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(ModEntityType.UNDEAD_SPIRIT.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Monster::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(ModEntityType.GHOST.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Monster::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
    }
}
