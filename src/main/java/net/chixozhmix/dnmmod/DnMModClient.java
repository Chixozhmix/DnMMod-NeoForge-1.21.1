package net.chixozhmix.dnmmod;

import io.redspace.ironsspellbooks.render.SpellBookCurioRenderer;
import net.chixozhmix.dnmmod.entity.custom.client.UndeadSpiritRenderer;
import net.chixozhmix.dnmmod.entity.flame_atronach.FlameAtronachrenderer;
import net.chixozhmix.dnmmod.entity.ghost.GhostRenderer;
import net.chixozhmix.dnmmod.entity.goblin_shaman.GoblinShamanRenderer;
import net.chixozhmix.dnmmod.entity.goblin_warior.GoblinWariorRenderer;
import net.chixozhmix.dnmmod.entity.greemon.GreemonRenderer;
import net.chixozhmix.dnmmod.entity.green_hag.GreenHagRenderer;
import net.chixozhmix.dnmmod.entity.leshy.LeshyRenderer;
import net.chixozhmix.dnmmod.entity.raven.RavenModel;
import net.chixozhmix.dnmmod.entity.raven.RavenRenderer;
import net.chixozhmix.dnmmod.entity.spell.acid_projectile.AcidProjectileRenderer;
import net.chixozhmix.dnmmod.entity.spell.chromatic_orb.ChromaticOrbRenderer;
import net.chixozhmix.dnmmod.entity.spell.contagion_ray.ContagionRayRenderer;
import net.chixozhmix.dnmmod.entity.spell.ice_dagger.IceDaggerRenderer;
import net.chixozhmix.dnmmod.entity.spell.ray_of_enfeeblement.RayOfEnfeeblementRenderer;
import net.chixozhmix.dnmmod.entity.spell.tall_the_dead.TallTheDeadRenderer;
import net.chixozhmix.dnmmod.entity.storm_atronach.StormAtronachRenderer;
import net.chixozhmix.dnmmod.entity.summoned.client.SummonedRavenModel;
import net.chixozhmix.dnmmod.entity.summoned.client.SummonedRavenRenderer;
import net.chixozhmix.dnmmod.registers.ModEntityType;
import net.chixozhmix.dnmmod.registers.ModItems;
import net.chixozhmix.dnmmod.registers.ModMenuTypes;
import net.chixozhmix.dnmmod.renderer.ArmorEffectRenderer;
import net.chixozhmix.dnmmod.screen.component_bag.ComponentBagScreen;
import net.chixozhmix.dnmmod.screen.medium_bag.MediumBagScreen;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

@Mod(value = DnMMod.MOD_ID, dist = Dist.CLIENT)
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
        event.enqueueWork(() -> {
            CuriosRendererRegistry.register(ModItems.MAGICAL_GRIMOIRE.get(), SpellBookCurioRenderer::new);

            ArmorEffectRenderer.init();
        });

        EntityRenderers.register(ModEntityType.ICE_DAGGER.get(), IceDaggerRenderer::new);
        EntityRenderers.register(ModEntityType.ACID_PROJECTILE.get(), AcidProjectileRenderer::new);
        EntityRenderers.register(ModEntityType.CHROMATIC_ORB.get(), ChromaticOrbRenderer::new);
        EntityRenderers.register(ModEntityType.RAY_OF_ENFEEBLEMENT.get(), RayOfEnfeeblementRenderer::new);
        EntityRenderers.register(ModEntityType.CONTAGION_RAY.get(), ContagionRayRenderer::new);
        EntityRenderers.register(ModEntityType.TALL_THE_DEAD.get(), TallTheDeadRenderer::new);

        EntityRenderers.register(ModEntityType.UNDEAD_SPIRIT.get(), UndeadSpiritRenderer::new);
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

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenuTypes.COMPONENT_BAG_MENU.get(), ComponentBagScreen::new);
        event.register(ModMenuTypes.MEDIUM_COMPONENT_BAG_MENU.get(), MediumBagScreen::new);
    }
}
