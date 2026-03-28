package net.chixozhmix.neodnm.items.weapons;

import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.item.weapons.AttributeContainer;
import io.redspace.ironsspellbooks.item.weapons.IronsWeaponTier;
import io.redspace.ironsspellbooks.item.weapons.StaffTier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public class WandTier implements IronsWeaponTier {
    public static StaffTier WOODEN_WAND;
    public static StaffTier DRUID_WAND;
    public static StaffTier ELECTROMANCER_WAND;
    public static StaffTier CRYOMANCER_WAND;
    public static StaffTier PYROMANCER_WAND;
    public static StaffTier BLOOD_WAND;
    public static StaffTier ENDER_WAND;
    public static StaffTier SACRED_SYMBOL;
    public static StaffTier EVOKER_WAND;

    float damage;
    float speed;
    AttributeContainer[] attributes;

    public WandTier(float damage, float speed, AttributeContainer... attributes) {
        this.damage = damage;
        this.speed = speed;
        this.attributes = attributes;
    }

    public float getSpeed() {
        return this.speed;
    }

    public float getAttackDamageBonus() {
        return this.damage;
    }

    public AttributeContainer[] getAdditionalAttributes() {
        return this.attributes;
    }

    static {
        WOODEN_WAND = new StaffTier(1.0F, -2.0F,
                new AttributeContainer[]
                        {new AttributeContainer(AttributeRegistry.MANA_REGEN, (double)0.25F, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                new AttributeContainer(AttributeRegistry.SPELL_POWER, 0.1,
                        AttributeModifier.Operation.ADD_MULTIPLIED_BASE)});

        DRUID_WAND = new StaffTier(1.0F, -2.0F,
                new AttributeContainer[]
                        {new AttributeContainer(AttributeRegistry.MANA_REGEN, (double)0.25F, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                                new AttributeContainer(AttributeRegistry.SPELL_POWER, 0.05F, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                        new AttributeContainer(AttributeRegistry.NATURE_SPELL_POWER, 0.15F, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)});
        ELECTROMANCER_WAND = new StaffTier(1.0F, -2.0F,
                new AttributeContainer[]
                        {new AttributeContainer(AttributeRegistry.MANA_REGEN, (double)0.25F, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                                new AttributeContainer(AttributeRegistry.SPELL_POWER, 0.05F, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                                new AttributeContainer(AttributeRegistry.LIGHTNING_SPELL_POWER, 0.15F, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)});
        CRYOMANCER_WAND = new StaffTier(1.0F, -2.0F,
                new AttributeContainer[]
                        {new AttributeContainer(AttributeRegistry.MANA_REGEN, (double)0.25F, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                                new AttributeContainer(AttributeRegistry.SPELL_POWER, 0.05F, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                                new AttributeContainer(AttributeRegistry.ICE_SPELL_POWER, 0.15F, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)});
        PYROMANCER_WAND = new StaffTier(1.0F, -2.0F,
                new AttributeContainer[]
                        {new AttributeContainer(AttributeRegistry.MANA_REGEN, (double)0.25F, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                                new AttributeContainer(AttributeRegistry.SPELL_POWER, 0.05F, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                                new AttributeContainer(AttributeRegistry.FIRE_SPELL_POWER, 0.15F, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)});
        BLOOD_WAND = new StaffTier(1.0F, -2.0F,
                new AttributeContainer[]
                        {new AttributeContainer(AttributeRegistry.MANA_REGEN, (double)0.25F, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                                new AttributeContainer(AttributeRegistry.SPELL_POWER, 0.05F, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                                new AttributeContainer(AttributeRegistry.BLOOD_SPELL_POWER, 0.15F, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)});
        ENDER_WAND = new StaffTier(1.0F, -2.0F,
                new AttributeContainer[]
                        {new AttributeContainer(AttributeRegistry.MANA_REGEN, (double)0.25F, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                                new AttributeContainer(AttributeRegistry.SPELL_POWER, 0.05F, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                                new AttributeContainer(AttributeRegistry.ENDER_SPELL_POWER, 0.15F, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)});
        SACRED_SYMBOL = new StaffTier(1.0F, -2.0F,
                new AttributeContainer[]
                        {new AttributeContainer(AttributeRegistry.MANA_REGEN, (double)0.25F, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                                new AttributeContainer(AttributeRegistry.SPELL_POWER, 0.05F, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                                new AttributeContainer(AttributeRegistry.HOLY_SPELL_POWER, 0.15F, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)});
        EVOKER_WAND = new StaffTier(1.0F, -2.0F,
                new AttributeContainer[]
                        {new AttributeContainer(AttributeRegistry.MANA_REGEN, (double)0.25F, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                                new AttributeContainer(AttributeRegistry.SPELL_POWER, 0.05F, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                                new AttributeContainer(AttributeRegistry.EVOCATION_SPELL_POWER, 0.15F, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                        new AttributeContainer(AttributeRegistry.SUMMON_DAMAGE, 0.10F, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)});


        }
}
