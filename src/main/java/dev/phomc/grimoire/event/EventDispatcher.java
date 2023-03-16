package dev.phomc.grimoire.event;

import dev.phomc.grimoire.accessor.ProjectileAccessor;
import dev.phomc.grimoire.item.ItemHelper;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;

public class EventDispatcher {
    public static void handleHurt(DamageSource damageSource, float f, LivingEntity victim) {
        if (damageSource.typeHolder().is(DamageTypes.PLAYER_ATTACK) ||
                damageSource.typeHolder().is(DamageTypes.MOB_ATTACK) ||
                damageSource.is(DamageTypeTags.IS_PROJECTILE)) {
            if (!(damageSource.getEntity() instanceof LivingEntity attacker)) return;
            if (!(attacker instanceof Player || victim instanceof Player)) return;

            Projectile projectile = null;
            ItemStack weapon = attacker.getMainHandItem();
            if (damageSource.getDirectEntity() instanceof Projectile) {
                projectile = (Projectile) damageSource.getDirectEntity();
                weapon = ((ProjectileAccessor) projectile).getWeapon();
            }

            AttackRecord attackRecord = new AttackRecord(attacker, victim, projectile, f, weapon);
            if (weapon != null) {
                ItemHelper.of(weapon).getEnchantmentFeature().iterateEnchantments(weapon, (key, value) -> {
                    if (value < 1) return;
                    key.onAttack(attackRecord, value);
                });
            }

            victim.getArmorSlots().forEach(armor -> {
                if (armor == null) return;
                ItemHelper.of(armor).getEnchantmentFeature().iterateEnchantments(armor, (key, value) -> {
                    if (value < 1) return;
                    key.onAttacked(attackRecord, armor, value);
                });
            });
        }
        else if (damageSource.is(DamageTypeTags.IS_FALL) ||
                damageSource.is(DamageTypeTags.IS_FIRE) ||
                damageSource.is(DamageTypeTags.IS_EXPLOSION)) {
            if (!(victim instanceof Player)) return;

            NaturalDamageRecord damageRecord = new NaturalDamageRecord(victim, f, damageSource);
            victim.getArmorSlots().forEach(armor -> {
                if (armor == null) return;
                ItemHelper.of(armor).getEnchantmentFeature().iterateEnchantments(armor, (key, value) -> {
                    if (value < 1) return;
                    key.onNaturalDamaged(damageRecord, armor, value);
                });
            });
        }
    }

    public static void handleShoot(LivingEntity shooter, Projectile projectile, ItemStack weapon) {
        ItemHelper.of(weapon).getEnchantmentFeature().iterateEnchantments(weapon, (enc, integer) -> {
            enc.onShoot(shooter, projectile, weapon, integer);
        });
    }
}
