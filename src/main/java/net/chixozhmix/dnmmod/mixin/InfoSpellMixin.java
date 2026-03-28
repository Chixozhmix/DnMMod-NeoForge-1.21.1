package net.chixozhmix.dnmmod.mixin;

import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import io.redspace.ironsspellbooks.spells.blood.RaiseDeadSpell;
import io.redspace.ironsspellbooks.spells.ender.BlackHoleSpell;
import io.redspace.ironsspellbooks.spells.evocation.GustSpell;
import io.redspace.ironsspellbooks.spells.evocation.InvisibilitySpell;
import io.redspace.ironsspellbooks.spells.fire.BurningDashSpell;
import io.redspace.ironsspellbooks.spells.fire.WallOfFireSpell;
import io.redspace.ironsspellbooks.spells.holy.GreaterHealSpell;
import io.redspace.ironsspellbooks.spells.ice.SummonPolarBearSpell;
import io.redspace.ironsspellbooks.spells.lightning.*;
import net.chixozhmix.dnmmod.Util.SpellUtils;
import net.chixozhmix.dnmmod.registers.ModItems;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@Mixin(value = {LightningBoltSpell.class, LightningLanceSpell.class, BallLightningSpell.class, BlackHoleSpell.class, BurningDashSpell.class,
        ChargeSpell.class, GreaterHealSpell.class, GustSpell.class, InvisibilitySpell.class, RaiseDeadSpell.class, SummonPolarBearSpell.class,
        ThunderstormSpell.class, WallOfFireSpell.class})
public abstract class InfoSpellMixin {
    @Unique
    private static final Map<Class<?>, Supplier<Item>> SPELL_COMPONENTS = new HashMap<>();

    static {
        SPELL_COMPONENTS.put(LightningBoltSpell.class, () -> Items.LIGHTNING_ROD);
        SPELL_COMPONENTS.put(LightningLanceSpell.class, () -> ModItems.IRON_TRIDENT.get());
        SPELL_COMPONENTS.put(BallLightningSpell.class, () -> ItemRegistry.LIGHTNING_BOTTLE.get());
        SPELL_COMPONENTS.put(BlackHoleSpell.class, () -> Items.LODESTONE);
        SPELL_COMPONENTS.put(BurningDashSpell.class, () -> ModItems.BURNT_SUGAR.get());
        SPELL_COMPONENTS.put(ChargeSpell.class, () -> ItemRegistry.ENERGIZED_CORE.get());
        SPELL_COMPONENTS.put(GreaterHealSpell.class, () -> Items.GLISTERING_MELON_SLICE);
        SPELL_COMPONENTS.put(GustSpell.class, () -> Items.FEATHER);
        SPELL_COMPONENTS.put(InvisibilitySpell.class, () -> Items.GLASS_BOTTLE);
        SPELL_COMPONENTS.put(RaiseDeadSpell.class, () -> Items.ROTTEN_FLESH);
        SPELL_COMPONENTS.put(SummonPolarBearSpell.class, () -> Items.COD);
        SPELL_COMPONENTS.put(ThunderstormSpell.class, () -> ModItems.THUNDERSTORM_BOTTLE.get());
        SPELL_COMPONENTS.put(WallOfFireSpell.class, () -> Items.LAVA_BUCKET);
    }

    private AbstractSpell getThisSpell() {
        return (AbstractSpell)(Object)this;
    }

    private Class<? extends AbstractSpell> getSpellClass() {
        return getThisSpell().getClass();
    }

    @Inject(method = "getUniqueInfo", at = @At("RETURN"), cancellable = true, remap = false)
    private void modifyGetUniqueInfo(int spellLevel, LivingEntity caster, CallbackInfoReturnable<List<MutableComponent>> cir) {
        Supplier<Item> componentSupplier = SPELL_COMPONENTS.get(this.getSpellClass());
        if (componentSupplier != null) {
            Item component = componentSupplier.get();
            if (component != null) {
                List<MutableComponent> original = cir.getReturnValue();
                List<MutableComponent> modified = new java.util.ArrayList<>(original);
                modified.add(Component.translatable("ui.dnmmod.spell_component",
                        SpellUtils.getComponentName(component)));
                cir.setReturnValue(modified);
            }
        }
    }
}
