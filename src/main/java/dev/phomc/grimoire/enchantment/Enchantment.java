package dev.phomc.grimoire.enchantment;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public abstract class Enchantment {
    public static class Target {
        public static Predicate<Item> ARMOR = item -> item instanceof ArmorItem;
        public static Predicate<Item> BOOTS = ARMOR.and(item -> ((ArmorItem) item).getSlot() == EquipmentSlot.FEET);
    }

    private final ResourceLocation identifier;
    private final Rarity rarity;
    private final Predicate<Item> itemCheck;

    public Enchantment(@NotNull ResourceLocation identifier, @NotNull Rarity rarity, @NotNull Predicate<Item> itemCheck) {
        this.identifier = identifier;
        this.rarity = rarity;
        this.itemCheck = itemCheck;
    }

    @NotNull
    public ResourceLocation getIdentifier() {
        return identifier;
    }

    @NotNull
    public Rarity getRarity() {
        return rarity;
    }

    @NotNull
    public Predicate<Item> getItemCheck() {
        return itemCheck;
    }

    public int getMaxLevel() {
        return 1;
    }
}
