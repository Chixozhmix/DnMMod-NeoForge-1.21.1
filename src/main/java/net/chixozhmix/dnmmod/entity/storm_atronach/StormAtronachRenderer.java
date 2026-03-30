package net.chixozhmix.dnmmod.entity.storm_atronach;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMobRenderer;
import net.chixozhmix.dnmmod.DnMMod;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class StormAtronachRenderer extends AbstractSpellCastingMobRenderer {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(DnMMod.MOD_ID, "textures/entity/storm_atronach/storm_atronach.png");
    private static final ResourceLocation EMISSIVE_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(DnMMod.MOD_ID, "textures/entity/storm_atronach/lightning.png");

    public StormAtronachRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new StormAtronachModel());
        this.shadowRadius = 0.5f;

        addRenderLayer(new GeoRenderLayer<>(this) {
            @Override
            public void render(PoseStack poseStack, AbstractSpellCastingMob animatable, BakedGeoModel model,
                               RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer,
                               float partialTick, int packedLight, int packedOverlay) {
                // Получаем эмиссивный вершинный буфер
                RenderType emissiveRenderType = RenderType.entityTranslucentEmissive(EMISSIVE_TEXTURE);
                VertexConsumer emissiveBuffer = bufferSource.getBuffer(emissiveRenderType);

                // Вызываем reRender с правильным количеством аргументов
                getRenderer().reRender(model, poseStack, bufferSource, animatable,
                        emissiveRenderType, emissiveBuffer, partialTick, packedLight, packedOverlay, 1);
            }
        });
    }

    @Override
    public RenderType getRenderType(AbstractSpellCastingMob animatable, ResourceLocation texture,
                                    @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityTranslucent(TEXTURE);
    }
}
