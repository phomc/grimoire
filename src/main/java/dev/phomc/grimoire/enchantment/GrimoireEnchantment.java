package dev.phomc.grimoire.enchantment;

import dev.phomc.grimoire.event.AttackRecord;
import dev.phomc.grimoire.event.NaturalDamageRecord;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public abstract class GrimoireEnchantment extends DummyEnchantment {
    private final ResourceLocation identifier;
    private final Predicate<Item> itemCheck;

    public GrimoireEnchantment(@NotNull ResourceLocation identifier,
                               @NotNull Enchantment.Rarity rarity,
                               @NotNull Predicate<Item> itemCheck) {
        super(rarity, EnchantmentCategory.VANISHABLE, EquipmentSlot.values());
        this.identifier = identifier;
        this.itemCheck = itemCheck;
    }

    @NotNull
    public final ResourceLocation getIdentifier() {
        return identifier;
    }

    @NotNull
    public final Predicate<Item> getItemCheck() {
        return itemCheck;
    }

    @Override
    public final boolean canEnchant(ItemStack itemStack) {
        return itemStack != null && itemCheck.test(itemStack.getItem());
    }

    @Override
    protected final boolean checkCompatibility(Enchantment enchantment) {
        return this != enchantment && EnchantmentRegistry.COMPATIBILITY_GRAPH.isCompatible(this, enchantment);
    }

    // must have player as either attacker or victim
    public void onAttack(AttackRecord attackRecord, int level) {

    }

    // must have player as either attacker or victim
    public void onAttacked(AttackRecord attackRecord, ItemStack armor, int level) {

    }

    public void onArmorTick(Player player, EquipmentSlot slot, ItemStack itemStack, int level, int tick) {

    }

    public void onNaturalDamaged(NaturalDamageRecord naturalDamageRecord, ItemStack armor, int level) {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GrimoireEnchantment that = (GrimoireEnchantment) o;
        return identifier.equals(that.identifier);
    }

    @Override
    public int hashCode() {
        return identifier.hashCode();
    }
}
