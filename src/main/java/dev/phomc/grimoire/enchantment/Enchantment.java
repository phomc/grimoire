package dev.phomc.grimoire.enchantment;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public abstract class Enchantment {
    public static class Target {
        public static Predicate<Item> WEAPON = item -> item instanceof SwordItem || item instanceof AxeItem;
        public static Predicate<Item> ARMOR = item -> item instanceof ArmorItem;
        public static Predicate<Item> BOOTS = ARMOR.and(item -> ((ArmorItem) item).getSlot() == EquipmentSlot.FEET);
    }

    private final ResourceLocation identifier;
    private final MutableComponent displayName;
    private final Rarity rarity;
    private final Predicate<Item> itemCheck;

    public Enchantment(@NotNull ResourceLocation identifier, @NotNull Rarity rarity, @NotNull Predicate<Item> itemCheck) {
        this.identifier = identifier;
        this.displayName = Component.translatable("enchantment." + identifier.toString().replace(":", "."));
        this.rarity = rarity;
        this.itemCheck = itemCheck;
    }

    @NotNull
    public ResourceLocation getIdentifier() {
        return identifier;
    }

    @NotNull
    public MutableComponent getDisplayName() {
        return displayName.copy();
    }

    @NotNull
    public Rarity getRarity() {
        return rarity;
    }

    @NotNull
    public Predicate<Item> getItemCheck() {
        return itemCheck;
    }

    public byte getMaxLevel() {
        return 1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Enchantment that = (Enchantment) o;
        return identifier.equals(that.identifier);
    }

    @Override
    public int hashCode() {
        return identifier.hashCode();
    }
}
