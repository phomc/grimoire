package dev.phomc.grimoire.event;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;

public record ProjectileHitRecord(LivingEntity shooter, Projectile projectile, HitResult hitResult, @Nullable ItemStack weapon) {
}
