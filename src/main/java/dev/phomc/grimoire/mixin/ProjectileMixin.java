package dev.phomc.grimoire.mixin;

import dev.phomc.grimoire.accessor.ProjectileAccessor;
import dev.phomc.grimoire.event.ProjectileHitRecord;
import dev.phomc.grimoire.item.GrimoireItem;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.HitResult;
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
            at = @At("TAIL")
    )
    protected void onHit(HitResult hitResult, CallbackInfo ci) {
        Entity e = getOwner();
        if (e instanceof LivingEntity && weapon != null && hitResult.getType() != HitResult.Type.MISS) {
            ProjectileHitRecord record = new ProjectileHitRecord((LivingEntity) e, (Projectile) (Object) this, hitResult, weapon);
            GrimoireItem.of(weapon).getEnchantmentFeature().iterateEnchantments(weapon, (enc, lv) -> {
                enc.onProjectileHit(record, lv);
            });
        }
    }
}
