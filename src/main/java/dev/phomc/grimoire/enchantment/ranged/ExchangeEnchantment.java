package dev.phomc.grimoire.enchantment.ranged;

import dev.phomc.grimoire.Grimoire;
import dev.phomc.grimoire.enchantment.EnchantmentTarget;
import dev.phomc.grimoire.enchantment.GrimoireEnchantment;
import dev.phomc.grimoire.event.ProjectileHitRecord;
import dev.phomc.grimoire.utils.DevModeUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;

public class ExchangeEnchantment extends GrimoireEnchantment {
    // use Rift as reference; because Exchange swaps two entities, the cost should be doubled to make sense,
    // but we are so kind so there is 50% cost reduction applied for the second entity :D
    private static final double[] COST_MULTIPLIER = Arrays.stream(RiftEnchantment.COST_MULTIPLIER).map(n -> n * 1.5).toArray();

    public ExchangeEnchantment(@NotNull ResourceLocation identifier) {
        super(identifier, Rarity.VERY_RARE, EnchantmentTarget.RANGED);

        DevModeUtils.runInDev(() -> Grimoire.LOGGER.info("Exchange travelling cost: {}", Arrays.toString(COST_MULTIPLIER)));
    }

    @Override
    public int getMaxLevel() {
        return COST_MULTIPLIER.length;
    }

    @Override
    public void onProjectileHit(ProjectileHitRecord projectileHitRecord, int enchantLevel, MutableBoolean cancelled) {
        enchantLevel = clampLevel(enchantLevel);
        if (projectileHitRecord.hitResult() instanceof EntityHitResult) {
            Entity target = ((EntityHitResult) projectileHitRecord.hitResult()).getEntity();
            LivingEntity shooter = projectileHitRecord.shooter();
            if (target instanceof LivingEntity && !target.is(shooter) && target.isAlive()) {
                cancelled.setTrue();
                double cost = Math.sqrt(target.distanceToSqr(shooter)) * COST_MULTIPLIER[enchantLevel - 1];
                Objects.requireNonNull(projectileHitRecord.weapon()).hurtAndBreak((int) cost, projectileHitRecord.shooter(), p -> p.broadcastBreakEvent(p.getUsedItemHand()));
                Vec3 m = target.position();
                Vec3 n = shooter.position();
                target.teleportTo(n.x, n.y, n.z);
                shooter.teleportTo(m.x, m.y, m.z);
            }
        }
    }
}
