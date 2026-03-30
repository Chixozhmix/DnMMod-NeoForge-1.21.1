package net.chixozhmix.dnmmod.effect.custom;

import net.chixozhmix.dnmmod.DnMMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class MageArmorEffect extends MobEffect {
    public MageArmorEffect() {
        super(MobEffectCategory.BENEFICIAL, 0x0B87EC);
        this.addAttributeModifier(Attributes.ARMOR,
                ResourceLocation.fromNamespaceAndPath(DnMMod.MOD_ID, "effect.mage_armor"),
                4,
                AttributeModifier.Operation.ADD_VALUE);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }
}
