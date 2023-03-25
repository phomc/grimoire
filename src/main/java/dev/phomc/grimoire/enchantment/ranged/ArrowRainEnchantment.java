package dev.phomc.grimoire.enchantment.ranged;

import dev.phomc.grimoire.enchantment.EnchantmentTarget;
import dev.phomc.grimoire.enchantment.GrimoireEnchantment;
import dev.phomc.grimoire.enchantment.property.ConditionalProperty;
import dev.phomc.grimoire.enchantment.property.DecimalProperty;
import dev.phomc.grimoire.enchantment.property.InfoProperty;
import dev.phomc.grimoire.enchantment.property.IntegerProperty;
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
    private static final double[] DAMAGE_PERCENT = new double[]{0.7, 0.8, 0.9, 1.0};
    private static final float POWER = 1.0f; // intended

    public ArrowRainEnchantment(@NotNull ResourceLocation identifier) {
        super(identifier, Rarity.RARE, EnchantmentTarget.BOW);

        createProperty("damageRatio", (DecimalProperty) level -> DAMAGE_PERCENT[level - getMinLevel()]);
        createProperty("amount", (IntegerProperty) level -> 2 * level);
        createProperty("criticalBonus", new ConditionalProperty() {
            @Override
            public boolean hasExtraDescription() {
                return true;
            }

            @Override
            public Boolean evaluate(int level) {
                return level == getMaxLevel();
            }
        });
        createProperty("cost", new InfoProperty());
    }

    @Override
    public int getMaxLevel() {
        return DAMAGE_PERCENT.length;
    }

    @Override
    public void onShoot(LivingEntity shooter, Projectile projectile, ItemStack weapon, int enchantLevel) {
        enchantLevel = clampLevel(enchantLevel);
        if (projectile instanceof Arrow parent) {
            int amount = requireIntegerProperty("amount").evaluate(enchantLevel);
            Vec3 dir = MathUtils.getDirection(shooter);
            Vec3[] points = MathUtils.getCircularPoints(shooter.getEyePosition(), dir, 1f, amount);
            for (Vec3 point : points) {
                Arrow arrow = new Arrow(shooter.level, shooter);
                arrow.setPos(point);
                arrow.shootFromRotation(shooter, shooter.getXRot(), shooter.getYRot(), 0.0f, POWER, 1.0f);
                if (requireConditionalProperty("criticalBonus").evaluate(enchantLevel)) {
                    arrow.setCritArrow(parent.isCritArrow());
                }
                arrow.setBaseDamage(parent.getBaseDamage() * requireDecimalProperty("damageRatio").evaluate(enchantLevel));
                arrow.setKnockback(parent.getKnockback());
                arrow.setRemainingFireTicks(parent.getRemainingFireTicks());
                arrow.pickup = AbstractArrow.Pickup.DISALLOWED; // no pickup-able - intended
                shooter.level.addFreshEntity(arrow);
            }
            weapon.hurtAndBreak(points.length, shooter, p -> p.broadcastBreakEvent(p.getUsedItemHand()));
        }
    }
}
