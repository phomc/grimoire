package dev.phomc.grimoire.enchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;

import java.util.function.Predicate;

public class EnchantmentTarget {
    public static Predicate<Item> WEAPON = item -> item instanceof SwordItem || item instanceof AxeItem;
    public static Predicate<Item> ARMOR = item -> item instanceof ArmorItem;
    public static Predicate<Item> BOOTS = ARMOR.and(item -> ((ArmorItem) item).getEquipmentSlot() == EquipmentSlot.FEET);
}
