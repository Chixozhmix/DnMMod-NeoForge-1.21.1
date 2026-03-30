package net.chixozhmix.dnmmod.effect.custom;

import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import net.chixozhmix.dnmmod.DnMMod;
import net.chixozhmix.dnmmod.registers.ModEffects;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

@EventBusSubscriber
public class ThickOfFight extends MobEffect {
    // Храним ID модификаторов как константы
    private static final ResourceLocation ATTACK_DAMAGE_ID =
            ResourceLocation.fromNamespaceAndPath(DnMMod.MOD_ID, "thick_of_fight_attack_damage");
    private static final ResourceLocation ATTACK_SPEED_ID =
            ResourceLocation.fromNamespaceAndPath(DnMMod.MOD_ID, "thick_of_fight_attack_speed");
    private static final ResourceLocation SPELL_POWER_ID =
            ResourceLocation.fromNamespaceAndPath(DnMMod.MOD_ID, "thick_of_fight_spell_power");
    private static final ResourceLocation MOVEMENT_SPEED_ID =
            ResourceLocation.fromNamespaceAndPath(DnMMod.MOD_ID, "thick_of_fight_movement_speed");

    private static final String HAD_EFFECT_KEY = "ThickOfFightHadEffect";

    public ThickOfFight() {
        super(MobEffectCategory.BENEFICIAL, 0xd11e39);
    }

    @Override
    public void onEffectStarted(LivingEntity entity, int amplifier) {
        super.onEffectStarted(entity, amplifier);
        addModifiers(entity, amplifier);
    }

    private void addModifiers(LivingEntity entity, int amplifier) {
        float attackDamageBonus = 0.05F * (amplifier + 1);
        float attackSpeedBonus = 0.15F * (amplifier + 1);
        float spellPowerBonus = 0.10F * (amplifier + 1);
        float movementSpeedBonus = 0.15F * (amplifier + 1);

        addModifier(entity, Attributes.ATTACK_DAMAGE, ATTACK_DAMAGE_ID,
                attackDamageBonus, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);

        addModifier(entity, Attributes.ATTACK_SPEED, ATTACK_SPEED_ID,
                attackSpeedBonus, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);

        addModifier(entity, AttributeRegistry.SPELL_POWER, SPELL_POWER_ID,
                spellPowerBonus, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);

        addModifier(entity, Attributes.MOVEMENT_SPEED, MOVEMENT_SPEED_ID,
                movementSpeedBonus, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
    }

    public static void removeModifiers(LivingEntity entity) {
        removeModifier(entity, Attributes.ATTACK_DAMAGE, ATTACK_DAMAGE_ID);
        removeModifier(entity, Attributes.ATTACK_SPEED, ATTACK_SPEED_ID);
        removeModifier(entity, AttributeRegistry.SPELL_POWER, SPELL_POWER_ID);
        removeModifier(entity, Attributes.MOVEMENT_SPEED, MOVEMENT_SPEED_ID);
    }

    private static void addModifier(LivingEntity entity, net.minecraft.core.Holder<net.minecraft.world.entity.ai.attributes.Attribute> attribute,
                                    ResourceLocation id, double amount, AttributeModifier.Operation operation) {
        AttributeInstance attributeInstance = entity.getAttribute(attribute);
        if (attributeInstance != null) {
            // Удаляем старый модификатор если есть
            attributeInstance.removeModifier(id);
            // Добавляем новый
            AttributeModifier modifier = new AttributeModifier(id, amount, operation);
            attributeInstance.addTransientModifier(modifier);
        }
    }

    private static void removeModifier(LivingEntity entity, net.minecraft.core.Holder<net.minecraft.world.entity.ai.attributes.Attribute> attribute,
                                       ResourceLocation id) {
        AttributeInstance attributeInstance = entity.getAttribute(attribute);
        if (attributeInstance != null) {
            attributeInstance.removeModifier(id);
        }
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true; // Меняем на true для тиковой проверки
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        if (!entity.level().isClientSide()) {
            // Отмечаем, что эффект активен
            entity.getPersistentData().putBoolean(HAD_EFFECT_KEY, true);

            // Проверяем и переприменяем модификаторы если нужно
            ensureModifiersExist(entity, amplifier);
        }
        return true;
    }

    private void ensureModifiersExist(LivingEntity entity, int amplifier) {
        // Проверяем, есть ли модификаторы, и если нет - добавляем
        AttributeInstance damageAttr = entity.getAttribute(Attributes.ATTACK_DAMAGE);
        if (damageAttr != null && !damageAttr.hasModifier(ATTACK_DAMAGE_ID)) {
            addModifiers(entity, amplifier);
        }
    }

    @SubscribeEvent
    public static void reduceDamage(LivingDamageEvent.Pre event) {
        LivingEntity entity = event.getEntity();
        MobEffectInstance effect = entity.getEffect(ModEffects.THICK_OF_FIGHT);
        if (effect != null) {
            float multiplier = 0.8F;
            event.setNewDamage(event.getOriginalDamage() * multiplier);
        }
    }

    // Событие для удаления модификаторов при окончании эффекта
    @SubscribeEvent
    public static void onEffectRemoved(MobEffectEvent.Remove event) {
        if (event.getEntity() instanceof LivingEntity living) {
            MobEffectInstance effectInstance = event.getEffectInstance();
            if (effectInstance != null && effectInstance.getEffect() instanceof ThickOfFight) {
                removeModifiers(living);
                living.getPersistentData().remove(HAD_EFFECT_KEY);
            }
        }
    }

    @SubscribeEvent
    public static void onEffectExpired(MobEffectEvent.Expired event) {
        if (event.getEntity() instanceof LivingEntity living) {
            MobEffectInstance effectInstance = event.getEffectInstance();
            if (effectInstance != null && effectInstance.getEffect() instanceof ThickOfFight) {
                removeModifiers(living);
                living.getPersistentData().remove(HAD_EFFECT_KEY);
            }
        }
    }

    // Резервный метод: проверяем каждый тик, нужны ли модификаторы
    @SubscribeEvent
    public static void onEntityTick(EntityTickEvent.Post event) {
        if (event.getEntity() instanceof LivingEntity living && !living.level().isClientSide()) {
            boolean hasEffect = living.hasEffect(ModEffects.THICK_OF_FIGHT);
            boolean hadEffect = living.getPersistentData().getBoolean(HAD_EFFECT_KEY);

            if (hadEffect && !hasEffect) {
                // Эффект был, но сейчас его нет - удаляем модификаторы
                removeModifiers(living);
                living.getPersistentData().remove(HAD_EFFECT_KEY);
            }
        }
    }
}
