package dev.phomc.grimoire.enchantment.ranged;

import dev.phomc.grimoire.Grimoire;
import dev.phomc.grimoire.enchantment.EnchantmentTarget;
import dev.phomc.grimoire.enchantment.GrimoireEnchantment;
import dev.phomc.grimoire.enchantment.property.DecimalProperty;
import dev.phomc.grimoire.enchantment.property.InfoProperty;
import dev.phomc.grimoire.event.ProjectileHitRecord;
import dev.phomc.grimoire.utils.DevModeUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.EntityHitResult;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;

public class TelekinesisEnchantment extends GrimoireEnchantment {
    public static final double[] COST_MULTIPLIER = RiftEnchantment.COST_MULTIPLIER; // use Rift as reference

    public TelekinesisEnchantment(@NotNull ResourceLocation identifier) {
        super(identifier, Rarity.VERY_RARE, EnchantmentTarget.RANGED);

        createProperty("cost", new InfoProperty());
        createProperty("blockPerDurability", (DecimalProperty) level -> 1.0 / COST_MULTIPLIER[level - getMinLevel()]);

        DevModeUtils.runInDev(() -> Grimoire.LOGGER.info("Telekinesis travelling cost: {}", Arrays.toString(COST_MULTIPLIER)));
    }

    @Override
    public int getMaxLevel() {
        return COST_MULTIPLIER.length;
    }

    @Override
    public void onProjectileHit(ProjectileHitRecord projectileHitRecord, int enchantLevel, MutableBoolean cancelled) {
        enchantLevel = clampLevel(enchantLevel);
        if (projectileHitRecord.hitResult() instanceof EntityHitResult) {
            cancelled.setTrue();
            Entity target = ((EntityHitResult) projectileHitRecord.hitResult()).getEntity();
            LivingEntity shooter = projectileHitRecord.shooter();
            double cost = Math.sqrt(shooter.distanceToSqr(target)) * COST_MULTIPLIER[enchantLevel - 1];
            Objects.requireNonNull(projectileHitRecord.weapon()).hurtAndBreak((int) cost, shooter, p -> p.broadcastBreakEvent(p.getUsedItemHand()));
            target.teleportTo(shooter.position().x, shooter.position().y, shooter.position().z);
        }
    }
}
