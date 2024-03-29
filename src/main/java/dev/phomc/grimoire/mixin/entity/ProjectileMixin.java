package dev.phomc.grimoire.mixin.entity;

import dev.phomc.grimoire.accessor.ProjectileAccessor;
import dev.phomc.grimoire.event.ProjectileHitRecord;
import dev.phomc.grimoire.item.ItemFeature;
import dev.phomc.grimoire.item.ItemHelper;
import dev.phomc.grimoire.item.features.EnchantmentFeature;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.HitResult;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Projectile.class)
public abstract class ProjectileMixin implements ProjectileAccessor {
    @Shadow public abstract @Nullable Entity getOwner();

    private ItemStack weapon;

    @Override
    public @Nullable ItemStack getWeapon() {
        return weapon;
    }

    @Override
    public void setWeapon(@Nullable ItemStack weapon) {
        this.weapon = weapon;
    }

    @Inject(
            method = "onHit(Lnet/minecraft/world/phys/HitResult;)V",
            at = @At("HEAD"),
            cancellable = true
    )
    protected void onHit(HitResult hitResult, CallbackInfo ci) {
        Entity e = getOwner();
        if (e instanceof LivingEntity && weapon != null && hitResult.getType() != HitResult.Type.MISS) {
            EnchantmentFeature enchantmentFeature = ItemHelper.of(weapon).getFeature(ItemFeature.ENCHANTMENT);
            if (enchantmentFeature == null) return;
            ProjectileHitRecord record = new ProjectileHitRecord((LivingEntity) e, (Projectile) (Object) this, hitResult, weapon);
            MutableBoolean cancelled = new MutableBoolean(false);
            enchantmentFeature.iterateEnchantments(weapon, (enc, lv) -> {
                enc.onProjectileHit(record, lv, cancelled);
            });
            if (cancelled.isTrue()) {
                ci.cancel();
            }
        }
    }
}
