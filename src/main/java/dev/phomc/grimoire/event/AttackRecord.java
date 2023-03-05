package dev.phomc.grimoire.event;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;

public record AttackRecord(LivingEntity attacker, LivingEntity victim, Projectile projectile, float damage, ItemStack weapon) {
    public boolean isRanged() {
        return projectile != null;
    }
}
