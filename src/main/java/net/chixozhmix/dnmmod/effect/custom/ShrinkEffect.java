package net.chixozhmix.dnmmod.effect.custom;

import net.chixozhmix.dnmmod.DnMMod;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.puffish.attributesmod.api.PuffishAttributes;
import virtuoel.pehkui.api.ScaleData;
import virtuoel.pehkui.api.ScaleType;
import virtuoel.pehkui.api.ScaleTypes;

public class ShrinkEffect extends MobEffect {

    private static final ResourceLocation JUMP_MODIFIER_ID =
            ResourceLocation.fromNamespaceAndPath(DnMMod.MOD_ID, "jump_id");
    private static final String ORIGINAL_SCALE_KEY = "ShrinkEffectOriginalScale";
    private static final String HAD_EFFECT_KEY = "ShrinkEffectHadEffect";

    public ShrinkEffect() {
        super(MobEffectCategory.NEUTRAL, 0x88CCFF);
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        if (!entity.level().isClientSide()) {
            // Отмечаем, что эффект активен
            entity.getPersistentData().putBoolean(HAD_EFFECT_KEY, true);

            // Сохраняем оригинальный масштаб при первом применении
            CompoundTag persistentData = entity.getPersistentData();
            if (!persistentData.contains(ORIGINAL_SCALE_KEY)) {
                ScaleType scaleType = ScaleTypes.BASE;
                ScaleData scaleData = scaleType.getScaleData(entity);
                float currentScale = scaleData.getBaseScale();
                persistentData.putFloat(ORIGINAL_SCALE_KEY, currentScale);
            }

            // Применяем эффект уменьшения
            applyShrinkEffects(entity, amplifier);
        }
        return true;
    }

    private void applyShrinkEffects(LivingEntity entity, int amplifier) {
        // Устанавливаем новый масштаб
        ScaleType scaleType = ScaleTypes.BASE;
        ScaleData scaleData = scaleType.getScaleData(entity);
        float targetScale = 1.0f / (amplifier + 2);
        scaleData.setBaseScale(targetScale);
        scaleData.setScale(targetScale);

        // Обновляем модификатор прыжка
        float jumpMultiplier = 1.0f + (amplifier + 1) * 0.5f;
        updateJumpModifier(entity, jumpMultiplier);
    }

    private void updateJumpModifier(LivingEntity entity, float jumpMultiplier) {
        AttributeInstance jumpAttribute = entity.getAttribute(PuffishAttributes.JUMP);
        if (jumpAttribute != null) {
            jumpAttribute.removeModifier(JUMP_MODIFIER_ID);

            AttributeModifier jumpModifier = new AttributeModifier(
                    JUMP_MODIFIER_ID,
                    jumpMultiplier - 1.0f,
                    AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
            );

            jumpAttribute.addTransientModifier(jumpModifier);
        }
    }

    public static void restoreScale(LivingEntity entity) {
        // Удаляем модификатор прыжка
        AttributeInstance jumpAttribute = entity.getAttribute(PuffishAttributes.JUMP);
        if (jumpAttribute != null) {
            jumpAttribute.removeModifier(JUMP_MODIFIER_ID);
        }

        // Восстанавливаем масштаб
        ScaleType scaleType = ScaleTypes.BASE;
        ScaleData scaleData = scaleType.getScaleData(entity);
        CompoundTag persistentData = entity.getPersistentData();

        if (persistentData.contains(ORIGINAL_SCALE_KEY)) {
            float originalScale = persistentData.getFloat(ORIGINAL_SCALE_KEY);
            scaleData.setBaseScale(originalScale);
            scaleData.setScale(originalScale);
            persistentData.remove(ORIGINAL_SCALE_KEY);
        } else {
            scaleData.setBaseScale(1.0f);
            scaleData.setScale(1.0f);
        }

        // Убираем отметку об эффекте
        persistentData.remove(HAD_EFFECT_KEY);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }
}
