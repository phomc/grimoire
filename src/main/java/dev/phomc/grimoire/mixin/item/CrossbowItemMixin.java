package dev.phomc.grimoire.mixin.item;

import dev.phomc.grimoire.accessor.ProjectileAccessor;
import dev.phomc.grimoire.event.EventDispatcher;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(CrossbowItem.class)
public class CrossbowItemMixin {
    @Inject(
            method = "shootProjectile(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;FZFFF)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z"
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private static void shootProjectile(Level level, LivingEntity livingEntity, InteractionHand interactionHand, ItemStack itemStack, ItemStack itemStack2, float f, boolean bl, float g, float h, float i, CallbackInfo ci, boolean bl2, Projectile projectile) {
        ((ProjectileAccessor) projectile).setWeapon(itemStack);
        EventDispatcher.handleShoot(livingEntity, projectile, itemStack);
    }
}
