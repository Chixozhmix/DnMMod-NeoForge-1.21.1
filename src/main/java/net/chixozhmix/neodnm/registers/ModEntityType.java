package net.chixozhmix.neodnm.registers;

import net.chixozhmix.neodnm.DnMMod;
import net.chixozhmix.neodnm.entity.flame_atronach.FlameAtronachEntity;
import net.chixozhmix.neodnm.entity.ghost.GhostEntity;
import net.chixozhmix.neodnm.entity.goblin_shaman.GoblinShamanEntity;
import net.chixozhmix.neodnm.entity.goblin_warior.GoblinWariorEntity;
import net.chixozhmix.neodnm.entity.greemon.GreemonEntity;
import net.chixozhmix.neodnm.entity.green_hag.GreenHagEntity;
import net.chixozhmix.neodnm.entity.leshy.LeshyEntity;
import net.chixozhmix.neodnm.entity.raven.RavenEntity;
import net.chixozhmix.neodnm.entity.spell.acid_projectile.AcidProjectile;
import net.chixozhmix.neodnm.entity.spell.chromatic_orb.ChromaticOrb;
import net.chixozhmix.neodnm.entity.spell.contagion_ray.ContagionRay;
import net.chixozhmix.neodnm.entity.spell.ice_dagger.IceDagger;
import net.chixozhmix.neodnm.entity.spell.ray_of_enfeeblement.RayOfEnfeeblement;
import net.chixozhmix.neodnm.entity.spell.tall_the_dead.tallTheDead;
import net.chixozhmix.neodnm.entity.storm_atronach.StormAtronach;
import net.chixozhmix.neodnm.entity.summoned.SummonedRavenEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModEntityType {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(Registries.ENTITY_TYPE, DnMMod.MOD_ID);

    public static final Supplier<EntityType<LeshyEntity>> LESHY =
            ENTITY_TYPES.register("leshy", () ->
                    EntityType.Builder.of(LeshyEntity::new, MobCategory.MONSTER)
                            .clientTrackingRange(16)
                            .sized(1.2f, 2.7f)
                            .build("leshy"));
    public static final Supplier<EntityType<GreenHagEntity>> GREEN_HAG =
            ENTITY_TYPES.register("green_hag", () ->
                    EntityType.Builder.of(GreenHagEntity::new, MobCategory.MONSTER)
                            .clientTrackingRange(16)
                            .sized(1.0f, 2.3f)
                            .build("green_hag"));

    public static final Supplier<EntityType<GoblinShamanEntity>> GOBLIN_SHAMAN =
            ENTITY_TYPES.register("goblin_shaman", () ->
                    EntityType.Builder.of(GoblinShamanEntity::new, MobCategory.MONSTER)
                            .clientTrackingRange(10)
                            .sized(0.6f, 0.7f)
                            .build("goblin_shaman"));
    public static final Supplier<EntityType<GoblinWariorEntity>> GOBLIN_WARRIOR =
            ENTITY_TYPES.register("goblin_warrior", () ->
                    EntityType.Builder.of(GoblinWariorEntity::new, MobCategory.MONSTER)
                            .clientTrackingRange(16)
                            .sized(0.6f, 0.7f)
                            .build("goblin_warrior"));
    public static final Supplier<EntityType<GhostEntity>> GHOST =
            ENTITY_TYPES.register("ghost", () ->
                    EntityType.Builder.of(GhostEntity::new, MobCategory.MONSTER)
                            .clientTrackingRange(10)
                            .sized(0.8f, 2.2f)
                            .build("ghost"));
    public static final Supplier<EntityType<GreemonEntity>> GREEMON =
            ENTITY_TYPES.register("greemon", () ->
                    EntityType.Builder.of(GreemonEntity::new, MobCategory.MONSTER)
                            .clientTrackingRange(10)
                            .sized(1.3f, 2.2f)
                            .build("greemon"));

    public static final Supplier<EntityType<RavenEntity>> RAVEN =
            ENTITY_TYPES.register("raven", () ->
                    EntityType.Builder.of(RavenEntity::new, MobCategory.CREATURE)
                            .clientTrackingRange(8)
                            .sized(0.375f, 0.5f)
                            .build("raven"));

    public static final Supplier<EntityType<SummonedRavenEntity>> SUMMON_RAVEN =
            ENTITY_TYPES.register("summon_raven", () ->
                    EntityType.Builder.<SummonedRavenEntity>of(SummonedRavenEntity::new, MobCategory.MONSTER)
                            .clientTrackingRange(32)
                            .sized(0.375f, 0.5f)
                            .build("summon_raven"));
    public static final Supplier<EntityType<FlameAtronachEntity>> FLAME_ATRONACH =
            ENTITY_TYPES.register("flame_atronach", () ->
                    EntityType.Builder.<FlameAtronachEntity>of(FlameAtronachEntity::new, MobCategory.MONSTER)
                            .sized(0.9f, 1.8f)
                            .clientTrackingRange(32)
                            .build("flame_atronach"));
    public static final Supplier<EntityType<StormAtronach>> STORM_ATRONACH =
            ENTITY_TYPES.register("storm_atronach", () ->
                    EntityType.Builder.<StormAtronach>of(StormAtronach::new, MobCategory.MONSTER)
                            .sized(0.9f, 1.8f)
                            .clientTrackingRange(32)
                            .build("storm_atronach"));

    public static final Supplier<EntityType<IceDagger>> ICE_DAGGER =
            ENTITY_TYPES.register("ice_dagger",
                    () -> EntityType.Builder.<IceDagger>of(IceDagger::new, MobCategory.MISC)
                            .sized(2.0f, 2.0f)
                            .clientTrackingRange(64)
                            .build("ice_dagger"));

    public static final Supplier<EntityType<AcidProjectile>> ACID_PROJECTILE =
            ENTITY_TYPES.register("acid_projectile",
                    () -> EntityType.Builder.<AcidProjectile>of(AcidProjectile::new, MobCategory.MISC)
                            .sized(1.0f, 1.0f)
                            .clientTrackingRange(32)
                            .build("acid_projectile")
            );

    public static final Supplier<EntityType<ChromaticOrb>> CHROMATIC_ORB =
            ENTITY_TYPES.register("chromatic_orb",
                    () -> EntityType.Builder.<ChromaticOrb>of(ChromaticOrb::new, MobCategory.MISC)
                            .sized(1.0f, 1.0f)
                            .clientTrackingRange(32)
                            .build("chromatic_orb")
            );
    public static final Supplier<EntityType<RayOfEnfeeblement>> RAY_OF_ENFEEBLEMENT =
            ENTITY_TYPES.register("ray_of_enfeeblement",
                    () -> EntityType.Builder.<RayOfEnfeeblement>of(RayOfEnfeeblement::new, MobCategory.MISC)
                            .sized(1.0F, 1.0F)
                            .clientTrackingRange(64)
                            .build("ray_of_enfeeblement"));
    public static final Supplier<EntityType<ContagionRay>> CONTAGION_RAY =
            ENTITY_TYPES.register("contagion_ray",
                    () -> EntityType.Builder.<ContagionRay>of(ContagionRay::new, MobCategory.MISC)
                            .sized(1.0F, 1.0F)
                            .clientTrackingRange(64)
                            .build("contagion_ray"));
    public static final Supplier<EntityType<tallTheDead>> TALL_THE_DEAD =
            ENTITY_TYPES.register("tall_the_dead",
                    () -> EntityType.Builder.<tallTheDead>of(tallTheDead::new, MobCategory.MISC)
                            .sized(1.0F, 1.0F)
                            .clientTrackingRange(64)
                            .build("tall_the_dead"));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
