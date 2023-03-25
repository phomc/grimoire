package dev.phomc.grimoire.enchantment;

import dev.phomc.grimoire.event.AttackRecord;
import dev.phomc.grimoire.event.NaturalDamageRecord;
import dev.phomc.grimoire.event.ProjectileHitRecord;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.mutable.MutableBoolean;

public interface EnchantmentEventListener {

    // must have player as either attacker or victim
    default void onAttack(AttackRecord attackRecord, int enchantLevel) {

    }

    // must have player as either attacker or victim
    default void onAttacked(AttackRecord attackRecord, ItemStack armor, int enchantLevel) {

    }

    default void onArmorTick(Player player, EquipmentSlot slot, ItemStack itemStack, int enchantLevel, int tick) {

    }

    default void onNaturalDamaged(NaturalDamageRecord naturalDamageRecord, ItemStack armor, int enchantLevel) {

    }

    default void onProjectileHit(ProjectileHitRecord projectileHitRecord, int enchantLevel, MutableBoolean cancelled) {

    }

    default void onShoot(LivingEntity shooter, Projectile projectile, ItemStack weapon, int enchantLevel) {

    }
}
