package dev.phomc.grimoire.enchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;

import java.util.function.Predicate;

public class EnchantmentTarget {
    public static Predicate<Item> MELEE = item -> item instanceof SwordItem || item instanceof AxeItem;
    public static Predicate<Item> RANGED = item -> item instanceof ProjectileWeaponItem || item instanceof TridentItem;
    public static Predicate<Item> BOW = item -> item instanceof BowItem;
    public static Predicate<Item> ARMOR = item -> item instanceof ArmorItem;
    public static Predicate<Item> PICKAXE = item -> item instanceof PickaxeItem;
    public static Predicate<Item> SHOVEL = item -> item instanceof ShovelItem;
    public static Predicate<Item> DIGGER = item -> item instanceof DiggerItem;
    public static Predicate<Item> HELMET = ARMOR.and(item -> ((ArmorItem) item).getEquipmentSlot() == EquipmentSlot.HEAD);
    public static Predicate<Item> BOOTS = ARMOR.and(item -> ((ArmorItem) item).getEquipmentSlot() == EquipmentSlot.FEET);
}
