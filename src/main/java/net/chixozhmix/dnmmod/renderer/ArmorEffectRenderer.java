package net.chixozhmix.dnmmod.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.chixozhmix.dnmmod.DnMMod;
import net.chixozhmix.dnmmod.registers.ModEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.ShieldModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderLivingEvent;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@EventBusSubscriber(modid = DnMMod.MOD_ID, value = Dist.CLIENT)
public class ArmorEffectRenderer {
    private static final Map<UUID, PlayerEffectData> effectDataMap = new ConcurrentHashMap<>();
    private static boolean initialized = false;
    private static long lastTickTime = 0;
    private static float deltaTime = 0.05f;

    private static final float[] SIN_CACHE = new float[360];
    private static final float[] COS_CACHE = new float[360];

    static {
        for (int i = 0; i < 360; i++) {
            double rad = Math.toRadians(i);
            SIN_CACHE[i] = (float) Math.sin(rad);
            COS_CACHE[i] = (float) Math.cos(rad);
        }
    }

    private static class PlayerEffectData {
        float armorState = 0f;
        float rotation = 0f;
        long lastUpdateTick = 0;
        boolean hasEffect = false;
        float lastRotation = 0f;

        void update(boolean hasEffectNow, float deltaTime) {
            lastRotation = rotation;
            hasEffect = hasEffectNow;

            // Обновляем вращение (константная скорость)
            rotation += 2.0f * deltaTime * 20f;
            if (rotation >= 360f) rotation -= 360f;

            // Обновляем состояние анимации
            if (hasEffectNow) {
                armorState = Math.min(armorState + 0.05f * deltaTime * 20f, 1.0f);
            } else {
                armorState = Math.max(armorState - 0.05f * deltaTime * 20f, 0f);
            }
        }

        boolean shouldRemove() {
            return !hasEffect && armorState <= 0f;
        }

        float getInterpolatedRotation(float partialTick) {
            float rotDiff = rotation - lastRotation;
            if (rotDiff > 180) rotDiff -= 360;
            if (rotDiff < -180) rotDiff += 360;
            return lastRotation + rotDiff * partialTick;
        }
    }

    public static void init() {
        if (!initialized) {
            initialized = true;
            Minecraft.getInstance().execute(() -> {
                // Пустая инициализация для гарантии загрузки класса
            });
        }
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null || minecraft.player == null) return;

        // Вычисляем delta time вручную
        long currentTime = System.currentTimeMillis();
        if (lastTickTime == 0) {
            lastTickTime = currentTime;
            deltaTime = 0.05f;
        } else {
            deltaTime = (currentTime - lastTickTime) / 1000.0f;
            lastTickTime = currentTime;
        }

        // Ограничиваем delta time для предотвращения резких скачков
        deltaTime = Math.min(deltaTime, 0.1f);

        List<Player> playersToUpdate = new ArrayList<>();
        playersToUpdate.add(minecraft.player);

        for (Player player : minecraft.level.players()) {
            if (player != minecraft.player &&
                    player.distanceToSqr(minecraft.player) < 64 * 64) {
                playersToUpdate.add(player);
            }
        }

        for (Player player : playersToUpdate) {
            UUID playerId = player.getUUID();
            boolean hasEffectNow = player.hasEffect(ModEffects.AGATHYS_ARMOR);

            PlayerEffectData data = effectDataMap.get(playerId);
            if (data == null) {
                if (hasEffectNow) {
                    data = new PlayerEffectData();
                    effectDataMap.put(playerId, data);
                    data.update(hasEffectNow, deltaTime);
                }
            } else {
                data.update(hasEffectNow, deltaTime);

                if (data.shouldRemove()) {
                    effectDataMap.remove(playerId);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onRenderPlayer(RenderLivingEvent.Post<Player, HumanoidModel<Player>> event) {
        if (!(event.getEntity() instanceof Player player)) return;

        PlayerEffectData data = effectDataMap.get(player.getUUID());
        if (data == null || data.armorState <= 0.001f) return;

        renderRotatingShields(player, event.getPoseStack(), event.getMultiBufferSource(),
                event.getPartialTick(), data);
    }

    private static void renderRotatingShields(Player player, PoseStack poseStack,
                                              MultiBufferSource bufferSource, float partialTicks,
                                              PlayerEffectData data) {
        poseStack.pushPose();

        float interpolatedRotation = data.getInterpolatedRotation(partialTicks);

        renderShieldPair(player, poseStack, bufferSource, partialTicks,
                data.armorState, interpolatedRotation);

        poseStack.popPose();
    }

    private static void renderShieldPair(Player player, PoseStack poseStack,
                                         MultiBufferSource bufferSource, float partialTicks,
                                         float alpha, float rotation) {
        final float radius = 1.2f;
        final float yOffset = 1.2f;

        int angle1 = ((int) rotation) % 360;
        if (angle1 < 0) angle1 += 360;

        int angle2 = (angle1 + 180) % 360;

        renderSingleShield(poseStack, bufferSource, partialTicks, alpha,
                angle1, radius, yOffset, player.tickCount);

        renderSingleShield(poseStack, bufferSource, partialTicks, alpha,
                angle2, radius, yOffset, player.tickCount);
    }

    private static void renderSingleShield(PoseStack poseStack,
                                           MultiBufferSource bufferSource, float partialTicks,
                                           float alpha, int angle, float radius,
                                           float yOffset, int tickCount) {
        poseStack.pushPose();

        float x = COS_CACHE[angle] * radius;
        float z = SIN_CACHE[angle] * radius;

        poseStack.translate(x, yOffset, z);

        poseStack.mulPose(Axis.YP.rotationDegrees(-angle + 45));
        poseStack.mulPose(Axis.XP.rotationDegrees(15));

        final float scale = 0.8f;
        poseStack.scale(scale, scale, scale);

        int sinIndex = (tickCount * 2) % 360;
        if (sinIndex < 0) sinIndex += 360;
        float pulse = SIN_CACHE[sinIndex] * 0.1f + 1.0f;
        poseStack.scale(pulse, pulse, pulse);

        renderShieldModel(poseStack, bufferSource, alpha);

        poseStack.popPose();
    }

    private static ShieldModel cachedShieldModel = null;

    private static void renderShieldModel(PoseStack poseStack, MultiBufferSource bufferSource,
                                          float alpha) {
        Minecraft minecraft = Minecraft.getInstance();

        if (cachedShieldModel == null) {
            cachedShieldModel = new ShieldModel(
                    minecraft.getEntityModels().bakeLayer(ModelLayers.SHIELD)
            );
        }

        RenderType renderType = RenderType.entityTranslucent(ResourceLocation.withDefaultNamespace("textures/block/water_flow.png"));
        var vertexConsumer = bufferSource.getBuffer(renderType);

        int alphaInt = (int)(alpha * 255);
        int color = (alphaInt << 24) | (150 << 16) | (220 << 8) | 255;

        cachedShieldModel.plate().render(poseStack, vertexConsumer, 15728880, OverlayTexture.NO_OVERLAY, color);
    }
}