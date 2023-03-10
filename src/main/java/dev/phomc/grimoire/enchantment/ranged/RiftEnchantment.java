package dev.phomc.grimoire.enchantment.ranged;

import dev.phomc.grimoire.enchantment.EnchantmentTarget;
import dev.phomc.grimoire.enchantment.GrimoireEnchantment;
import dev.phomc.grimoire.event.ProjectileHitRecord;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class RiftEnchantment extends GrimoireEnchantment {
    private static final double[] COST_MULTIPLIER = new double[]{1.0, 0.8, 0.5, 0.1};

    public RiftEnchantment(@NotNull ResourceLocation identifier) {
        super(identifier, Rarity.VERY_RARE, EnchantmentTarget.RANGED);
    }

    @Override
    public int getMaxLevel() {
        return COST_MULTIPLIER.length;
    }

    @Override
    public void onProjectileHit(ProjectileHitRecord projectileHitRecord, int enchantLevel) {
        Vec3 v = projectileHitRecord.hitResult().getLocation();
        double cost = Math.sqrt(projectileHitRecord.shooter().distanceToSqr(v)) * COST_MULTIPLIER[enchantLevel - 1];
        Objects.requireNonNull(projectileHitRecord.weapon()).hurtAndBreak((int) cost, projectileHitRecord.shooter(), livingEntity -> {});
        // TODO safe teleportation
        projectileHitRecord.shooter().teleportTo(v.x, v.y, v.z);
    }
}