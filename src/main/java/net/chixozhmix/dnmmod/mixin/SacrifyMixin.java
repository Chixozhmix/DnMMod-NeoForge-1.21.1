package net.chixozhmix.dnmmod.mixin;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.spells.blood.SacrificeSpell;
import net.chixozhmix.dnmmod.Util.SpellUtils;
import net.chixozhmix.dnmmod.registers.ModItems;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(SacrificeSpell.class)
public class SacrifyMixin {
    @Inject(method = "getUniqueInfo", at = @At("RETURN"), cancellable = true, remap = false)
    private void modifyGetUniqueInfo(int spellLevel, LivingEntity caster, CallbackInfoReturnable<List<MutableComponent>> cir) {
        List<MutableComponent> original = cir.getReturnValue();
        // Создаем новый список с дополнительной информацией
        List<MutableComponent> modified = new java.util.ArrayList<>(original);
        modified.add(Component.translatable("ui.dnmmod.spell_component", SpellUtils.getComponentName(ModItems.RITUAL_DAGGER.get())));
        cir.setReturnValue(modified);
    }

    @Inject(method = "checkPreCastConditions", at = @At("HEAD"), cancellable = true, remap = false)
    private void injectCheckPreCastConditions(Level level, int spellLevel, LivingEntity entity, MagicData playerMagicData, CallbackInfoReturnable<Boolean> cir) {
        if (!SpellUtils.checkSpellComponent(entity, ModItems.RITUAL_DAGGER.get())) {
            cir.setReturnValue(false);
            cir.cancel();
        }
    }
}
