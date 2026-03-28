package net.chixozhmix.dnmmod.mixin;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import io.redspace.ironsspellbooks.spells.blood.RaiseDeadSpell;
import io.redspace.ironsspellbooks.spells.ender.BlackHoleSpell;
import io.redspace.ironsspellbooks.spells.ender.RecallSpell;
import io.redspace.ironsspellbooks.spells.ender.SummonEnderChestSpell;
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
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@Mixin(AbstractSpell.class)
public class AbstractSpellMixin {
    private static final Map<Class<? extends AbstractSpell>, Supplier<Item>> SPELL_COMPONENTS;

    private static final Map<Item, Component> COMPONENT_NAME_CACHE = new HashMap<>();

    static {
        // Инициализация карты компонентов - используем HashMap для скорости
        Map<Class<? extends AbstractSpell>, Supplier<Item>> map = new HashMap<>();

        // Blood spells
        map.put(RaiseDeadSpell.class, () -> Items.ROTTEN_FLESH);

        // Holy spells
        map.put(GreaterHealSpell.class, () -> Items.GLISTERING_MELON_SLICE);

        // Ender spells
        map.put(SummonEnderChestSpell.class, () -> Items.CHEST);
        map.put(BlackHoleSpell.class, () -> Items.LODESTONE);
        map.put(RecallSpell.class, () -> ModItems.MIRROR.get());

        // Fire spells
        map.put(WallOfFireSpell.class, () -> Items.LAVA_BUCKET);
        map.put(BurningDashSpell.class, () -> ModItems.BURNT_SUGAR.get());

        // Evocation spells
        map.put(GustSpell.class, () -> Items.FEATHER);
        map.put(InvisibilitySpell.class, () -> Items.GLASS_BOTTLE);

        // Ice spells
        map.put(SummonPolarBearSpell.class, () -> Items.COD);

        // Lightning spells
        map.put(BallLightningSpell.class, ItemRegistry.LIGHTNING_BOTTLE);
        map.put(ChargeSpell.class, ItemRegistry.ENERGIZED_CORE);
        map.put(LightningBoltSpell.class, () -> Items.LIGHTNING_ROD);
        map.put(LightningLanceSpell.class, ModItems.IRON_TRIDENT);
        map.put(ThunderstormSpell.class, ModItems.THUNDERSTORM_BOTTLE);

        SPELL_COMPONENTS = Collections.unmodifiableMap(map);
    }

    private AbstractSpell getThisSpell() {
        return (AbstractSpell)(Object)this;
    }

    private Class<? extends AbstractSpell> getSpellClass() {
        return getThisSpell().getClass();
    }

    @Inject(method = "checkPreCastConditions", at = @At("HEAD"), cancellable = true, remap = false)
    private void injectCheckPreCastConditions(Level level, int spellLevel, LivingEntity entity, MagicData playerMagicData,
                                              CallbackInfoReturnable<Boolean> cir) {
        Supplier<Item> componentSupplier = SPELL_COMPONENTS.get(getSpellClass());

        if (componentSupplier != null) {
            Item requiredComponent = componentSupplier.get();
            if (!SpellUtils.checkSpellComponent(entity, requiredComponent)) {
                cir.setReturnValue(false);
            }
        }
    }
    @Inject(method = "getUniqueInfo", at = @At("RETURN"), cancellable = true, remap = false)
    public void ingectedgetUniqueInfo(int spellLevel, LivingEntity caster, CallbackInfoReturnable<List<MutableComponent>> cir) {
        Supplier<Item> componentSupplier = SPELL_COMPONENTS.get(this.getSpellClass());

        if (componentSupplier != null) {
            Item requiredComponent = componentSupplier.get();
            List<MutableComponent> original = cir.getReturnValue();

            List<MutableComponent> modified = new java.util.ArrayList<>(original);
            modified.add(Component.translatable("ui.dnmmod.spell_component",
                    SpellUtils.getComponentName(requiredComponent)));

            cir.setReturnValue(modified);
        }
    }
}
