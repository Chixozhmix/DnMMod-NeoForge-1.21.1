package net.chixozhmix.neodnm.items.custom;

import io.redspace.ironsspellbooks.api.item.weapons.MagicSwordItem;
import io.redspace.ironsspellbooks.api.registry.SpellDataRegistryHolder;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tiers;

public class BladesingerSword extends MagicSwordItem {

    public BladesingerSword(SpellDataRegistryHolder[] imbuedSpells) {
        super(Tiers.NETHERITE,
                new Properties().durability(2100).rarity(Rarity.EPIC),
                imbuedSpells);
    }
}
