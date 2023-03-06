package dev.phomc.grimoire.enchantment;

import dev.phomc.grimoire.event.AttackRecord;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public abstract class GrimoireEnchantment {
    private final ResourceLocation identifier;
    private final MutableComponent displayName;
    private final EnchantmentRarity rarity;
    private final Predicate<Item> itemCheck;

    public GrimoireEnchantment(@NotNull ResourceLocation identifier, @NotNull EnchantmentRarity rarity, @NotNull Predicate<Item> itemCheck) {
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
    public EnchantmentRarity getRarity() {
        return rarity;
    }

    @NotNull
    public Predicate<Item> getItemCheck() {
        return itemCheck;
    }

    public byte getMaxLevel() {
        return 1;
    }

    public int getDamageProtection(int level, DamageSource damageSource) {
        return 0;
    }

    public float getDamageBonus(int level, MobType mobType) {
        return 0;
    }

    public void doPostHurt(LivingEntity livingEntity, Entity entity, int level) {

    }

    // must have player as either attacker or victim
    public void onAttack(AttackRecord attackRecord, byte level) {

    }

    // must have player as either attacker or victim
    public void onAttacked(AttackRecord attackRecord, ItemStack armor, byte level) {

    }

    public void onArmorTick(Player player, EquipmentSlot slot, ItemStack itemStack, byte level, int tick) {

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
