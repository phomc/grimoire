package dev.phomc.grimoire.event;

import dev.phomc.grimoire.accessor.ProjectileMixinAccessor;
import dev.phomc.grimoire.item.GrimoireItem;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;

public class AttackEvent {
    public static void handleHurt(DamageSource damageSource, float f, LivingEntity victim) {
        if (!(damageSource.getEntity() instanceof LivingEntity attacker)) return;

        // Current implementation
        // 1. Attack: Living Entity -> Living Entity if and only if the main hand has enchantment
        // 2. Defense: Living Entity -> Living Entity if and only if any armor has enchantment
        // Either party must be a player

        if (!(attacker instanceof Player) && !(victim instanceof Player)) return;

        if (damageSource.typeHolder().is(DamageTypes.PLAYER_ATTACK) || damageSource.typeHolder().is(DamageTypes.MOB_ATTACK)) {
            ItemStack weapon = attacker.getMainHandItem();
            AttackRecord attackRecord = new AttackRecord(attacker, victim, null, f, weapon);
            GrimoireItem.of(weapon).getEnchantmentFeature().enchantments.forEach((key, value) -> {
                if (value < 1) return;
                key.onAttack(attackRecord, value);
            });

            victim.getArmorSlots().forEach(armor -> {
                if (armor == null) return;
                GrimoireItem.of(armor).getEnchantmentFeature().enchantments.forEach((key, value) -> {
                    if (value < 1) return;
                    key.onAttacked(attackRecord, armor, value);
                });
            });
        }
        else if (damageSource.is(DamageTypeTags.IS_PROJECTILE) && damageSource.getDirectEntity() instanceof Projectile projectile) {
            ItemStack weapon = ((ProjectileMixinAccessor) projectile).getWeapon();

            AttackRecord attackRecord = new AttackRecord(attacker, victim, projectile, f, weapon);
            if (weapon != null) {
                GrimoireItem.of(weapon).getEnchantmentFeature().enchantments.forEach((key, value) -> {
                    if (value < 1) return;
                    key.onAttack(attackRecord, value);
                });
            }

            victim.getArmorSlots().forEach(armor -> {
                if (armor == null) return;
                GrimoireItem.of(armor).getEnchantmentFeature().enchantments.forEach((key, value) -> {
                    if (value < 1) return;
                    key.onAttacked(attackRecord, armor, value);
                });
            });
        }
    }
}
