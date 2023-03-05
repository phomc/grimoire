package dev.phomc.grimoire.mixin;

import dev.phomc.grimoire.accessor.ProjectileMixinAccessor;
import dev.phomc.grimoire.item.GrimoireItem;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Inject(method = "actuallyHurt(Lnet/minecraft/world/damagesource/DamageSource;F)V", at = @At("TAIL"))
    protected void actuallyHurt(DamageSource damageSource, float f, CallbackInfo ci) {
        if (damageSource.is(DamageTypeTags.IS_PROJECTILE)) {
            Entity direct = damageSource.getDirectEntity();
            Entity attacker = damageSource.getEntity();
            if (direct instanceof Projectile && attacker instanceof LivingEntity) {
                ItemStack weapon = ((ProjectileMixinAccessor) direct).getWeapon();
                if (weapon == null) return;
                GrimoireItem.of(weapon).getEnchantmentFeature().enchantments.forEach((key, value) -> {
                    if (value < 1) return;
                    key.onProjectileAttack((LivingEntity) attacker, (LivingEntity) (Object) this, (Projectile) direct, f, value);
                });
            }
        }
    }
}
