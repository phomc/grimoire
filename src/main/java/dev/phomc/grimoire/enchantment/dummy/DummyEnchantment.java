package dev.phomc.grimoire.enchantment.dummy;

import dev.phomc.grimoire.enchantment.EnchantmentRegistry;
import dev.phomc.grimoire.enchantment.GrimoireEnchantment;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class DummyEnchantment extends Enchantment {
    public DummyEnchantment() {
        super(Rarity.COMMON, EnchantmentCategory.BREAKABLE, EquipmentSlot.values());
    }

    public int getMaxLevel() {
        return 1;
    }

    public int getMinCost(int i) {
        return 9999;
    }

    public int getMaxCost(int i) {
        return 9999;
    }

    // ==== START event ====
    public int getDamageProtection(int i, DamageSource damageSource) {
        int value = 0;
        for (GrimoireEnchantment e : EnchantmentRegistry.ALL.values()) {
            value += e.getDamageProtection(i, damageSource);
        }
        return value;
    }

    public float getDamageBonus(int i, MobType mobType) {
        float value = 0;
        for (GrimoireEnchantment e : EnchantmentRegistry.ALL.values()) {
            value += e.getDamageBonus(i, mobType);
        }
        return value;
    }

    public void doPostHurt(LivingEntity livingEntity, Entity entity, int i) {
        for (GrimoireEnchantment e : EnchantmentRegistry.ALL.values()) {
            e.doPostHurt(livingEntity, entity, i);
        }
    }

    public void doPostAttack(LivingEntity livingEntity, Entity entity, int i) {
        // how is this different from doPostHurt ???
    }

    // ==== END event ====

    public boolean canEnchant(ItemStack itemStack) {
        return true;
    }

    public boolean isTreasureOnly() {
        return true;
    }

    public boolean isTradeable() {
        return false;
    }

    public boolean isDiscoverable() {
        return false;
    }
}
