package net.chixozhmix.neodnm;

import net.chixozhmix.neodnm.entity.flame_atronach.FlameAtronachrenderer;
import net.chixozhmix.neodnm.entity.ghost.GhostRenderer;
import net.chixozhmix.neodnm.entity.goblin_shaman.GoblinShamanRenderer;
import net.chixozhmix.neodnm.entity.goblin_warior.GoblinWariorRenderer;
import net.chixozhmix.neodnm.entity.greemon.GreemonRenderer;
import net.chixozhmix.neodnm.entity.green_hag.GreenHagRenderer;
import net.chixozhmix.neodnm.entity.leshy.LeshyRenderer;
import net.chixozhmix.neodnm.entity.raven.RavenModel;
import net.chixozhmix.neodnm.entity.raven.RavenRenderer;
import net.chixozhmix.neodnm.entity.spell.acid_projectile.AcidProjectileRenderer;
import net.chixozhmix.neodnm.entity.spell.chromatic_orb.ChromaticOrbRenderer;
import net.chixozhmix.neodnm.entity.spell.contagion_ray.ContagionRayRenderer;
import net.chixozhmix.neodnm.entity.spell.ice_dagger.IceDaggerRenderer;
import net.chixozhmix.neodnm.entity.spell.ray_of_enfeeblement.RayOfEnfeeblementRenderer;
import net.chixozhmix.neodnm.entity.spell.tall_the_dead.TallTheDeadRenderer;
import net.chixozhmix.neodnm.entity.storm_atronach.StormAtronachRenderer;
import net.chixozhmix.neodnm.entity.summoned.client.SummonedRavenModel;
import net.chixozhmix.neodnm.entity.summoned.client.SummonedRavenRenderer;
import net.chixozhmix.neodnm.registers.ModEntityType;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@Mod(value = DnMMod.MOD_ID, dist = Dist.CLIENT)
// You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
@EventBusSubscriber(modid = DnMMod.MOD_ID, value = Dist.CLIENT)
public class DnMModClient {
    public DnMModClient(ModContainer container) {
        // Allows NeoForge to create a config screen for this mod's configs.
        // The config screen is accessed by going to the Mods screen > clicking on your mod > clicking on config.
        // Do not forget to add translations for your config options to the en_us.json file.

        //container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        EntityRenderers.register(ModEntityType.ICE_DAGGER.get(), IceDaggerRenderer::new);
        EntityRenderers.register(ModEntityType.ACID_PROJECTILE.get(), AcidProjectileRenderer::new);
        EntityRenderers.register(ModEntityType.CHROMATIC_ORB.get(), ChromaticOrbRenderer::new);
        EntityRenderers.register(ModEntityType.RAY_OF_ENFEEBLEMENT.get(), RayOfEnfeeblementRenderer::new);
        EntityRenderers.register(ModEntityType.CONTAGION_RAY.get(), ContagionRayRenderer::new);
        EntityRenderers.register(ModEntityType.TALL_THE_DEAD.get(), TallTheDeadRenderer::new);

//        EntityRenderers.register(ModEntityType.UNDEAD_SPIRIT.get(), UndeadSpiritRenderer::new);
//        EntityRenderers.register(ModEntityType.SUMMONED_UNDEAD_SPIRIT.get(), SummonedUndeadSpiritRenderer::new);
        EntityRenderers.register(ModEntityType.GOBLIN_SHAMAN.get(), GoblinShamanRenderer::new);
        EntityRenderers.register(ModEntityType.GREEN_HAG.get(), GreenHagRenderer::new);
        EntityRenderers.register(ModEntityType.RAVEN.get(), RavenRenderer::new);
        EntityRenderers.register(ModEntityType.SUMMON_RAVEN.get(), SummonedRavenRenderer::new);
        EntityRenderers.register(ModEntityType.LESHY.get(), LeshyRenderer::new);
        EntityRenderers.register(ModEntityType.GHOST.get(), GhostRenderer::new);
        EntityRenderers.register(ModEntityType.GREEMON.get(), GreemonRenderer::new);
        EntityRenderers.register(ModEntityType.GOBLIN_WARRIOR.get(), GoblinWariorRenderer::new);
        EntityRenderers.register(ModEntityType.FLAME_ATRONACH.get(), FlameAtronachrenderer::new);
        EntityRenderers.register(ModEntityType.STORM_ATRONACH.get(), StormAtronachRenderer::new);

//        MenuScreens.register(ModMenuTypes.COMPONENT_BAG_MENU.get(), ComponentBagScreen::new);
//        MenuScreens.register(ModMenuTypes.MEDIUM_COMPONENT_BAG_MENU.get(), MediumBagScreen::new);
    }

    @SubscribeEvent
    public static void onRegisterLayer(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(IceDaggerRenderer.MODEL_LAYER_LOCATION, IceDaggerRenderer::createBodyLayer);
        event.registerLayerDefinition(AcidProjectileRenderer.MODEL_LAYER_LOCATION, AcidProjectileRenderer::createBodyLayer);
        event.registerLayerDefinition(ChromaticOrbRenderer.MODEL_LAYER_LOCATION, ChromaticOrbRenderer::createBodyLayer);
        event.registerLayerDefinition(RavenRenderer.MODEL_LAYER_LOCATION, RavenModel::createBodyLayer);
        event.registerLayerDefinition(SummonedRavenRenderer.MODEL_LAYER_LOCATION, SummonedRavenModel::createBodyLayer);
        event.registerLayerDefinition(RayOfEnfeeblementRenderer.MODEL_LAYER_LOCATION, RayOfEnfeeblementRenderer::createBodyLayer);
    }
}
