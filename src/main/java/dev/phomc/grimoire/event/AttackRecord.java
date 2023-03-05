package dev.phomc.grimoire.event;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public record AttackRecord(LivingEntity attacker, LivingEntity victim, @Nullable Projectile projectile, float damage, @Nullable ItemStack weapon) {
    public boolean isRanged() {
        return projectile != null;
    }
}
