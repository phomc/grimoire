package dev.phomc.grimoire.mixin;

import dev.phomc.grimoire.enchantment.EnchantmentRegistry;
import dev.phomc.grimoire.item.GrimoireItem;
import dev.phomc.grimoire.item.features.EnchantmentFeature;
import net.fabricmc.fabric.api.item.v1.FabricItemStack;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Shadow
    public abstract ItemStack getItemBySlot(EquipmentSlot var1);

    @Inject(method = "getJumpBoostPower()D", at = @At("HEAD"), cancellable = true)
    public void getJumpBoostPower(CallbackInfoReturnable<Double> info) {
        ItemStack item = getItemBySlot(EquipmentSlot.FEET);
        int lv = GrimoireItem.of(item).getEnchantmentFeature().getEnchantment(EnchantmentRegistry.SPRINGS);
        if (lv > 0) {
            info.setReturnValue(0.1d * lv);
        }
    }
}
