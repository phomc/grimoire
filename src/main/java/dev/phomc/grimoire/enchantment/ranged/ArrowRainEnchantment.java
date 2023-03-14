package dev.phomc.grimoire.enchantment.ranged;

import dev.phomc.grimoire.enchantment.EnchantmentTarget;
import dev.phomc.grimoire.enchantment.GrimoireEnchantment;
import dev.phomc.grimoire.utils.MathUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class ArrowRainEnchantment extends GrimoireEnchantment {
    private static final float[] DAMAGE_PERCENT = new float[]{0.7f, 0.8f, 0.9f, 1.0f};
    private static final float POWER = 1.0f; // intended

    public ArrowRainEnchantment(@NotNull ResourceLocation identifier) {
        super(identifier, Rarity.RARE, EnchantmentTarget.BOW);
    }

    @Override
    public int getMaxLevel() {
        return DAMAGE_PERCENT.length;
    }

    @Override
    public void onShoot(LivingEntity shooter, Projectile projectile, ItemStack weapon, int enchantLevel) {
        if (projectile instanceof Arrow parent) {
            int amount = Math.min(getMaxLevel(), enchantLevel) * 2;
            Vec3 dir = MathUtils.getDirection(shooter);
            Vec3[] points = MathUtils.getCircularPoints(shooter.getEyePosition(), dir, 1f, amount);
            for (Vec3 point : points) {
                Arrow arrow = new Arrow(shooter.level, shooter);
                arrow.setPos(point);
                arrow.shootFromRotation(shooter, shooter.getXRot(), shooter.getYRot(), 0.0f, POWER, 1.0f);
                if (enchantLevel == getMaxLevel()) {
                    arrow.setCritArrow(parent.isCritArrow());
                }
                arrow.setBaseDamage(parent.getBaseDamage() * DAMAGE_PERCENT[enchantLevel - 1]);
                arrow.setKnockback(parent.getKnockback());
                arrow.setRemainingFireTicks(parent.getRemainingFireTicks());
                arrow.pickup = AbstractArrow.Pickup.DISALLOWED; // no pickup-able - intended
                shooter.level.addFreshEntity(arrow);
            }
            weapon.hurtAndBreak(points.length, shooter, p -> p.broadcastBreakEvent(p.getUsedItemHand()));
        }
    }
}
