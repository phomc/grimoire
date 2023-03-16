package dev.phomc.grimoire.enchantment;

import dev.phomc.grimoire.Grimoire;
import dev.phomc.grimoire.event.AttackRecord;
import dev.phomc.grimoire.event.NaturalDamageRecord;
import dev.phomc.grimoire.event.ProjectileHitRecord;
import dev.phomc.grimoire.item.ItemHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.trading.MerchantOffer;
import org.apache.commons.lang3.mutable.MutableBoolean;
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

    protected final int clampLevel(int lv) {
        if (lv < getMinLevel()) {
            Grimoire.LOGGER.warn("Invalid {} level detected: {} < min = {}", identifier, lv, getMinLevel());
            return getMinLevel();
        }
        if (lv > getMaxLevel()) {
            Grimoire.LOGGER.warn("Invalid {} level detected: {} > max = {}", identifier, lv, getMaxLevel());
            return getMaxLevel();
        }
        return lv;
    }

    // must have player as either attacker or victim
    public void onAttack(AttackRecord attackRecord, int enchantLevel) {

    }

    // must have player as either attacker or victim
    public void onAttacked(AttackRecord attackRecord, ItemStack armor, int enchantLevel) {

    }

    public void onArmorTick(Player player, EquipmentSlot slot, ItemStack itemStack, int enchantLevel, int tick) {

    }

    public void onNaturalDamaged(NaturalDamageRecord naturalDamageRecord, ItemStack armor, int enchantLevel) {

    }

    public void onProjectileHit(ProjectileHitRecord projectileHitRecord, int enchantLevel, MutableBoolean cancelled) {

    }

    public void onShoot(LivingEntity shooter, Projectile projectile, ItemStack weapon, int enchantLevel) {

    }

    public MerchantOffer handleEnchantedBookOffer(Entity entity, RandomSource randomSource, MerchantOffer offer, Integer enchantLevel) {
        // Custom offer generation to fix two issues:
        // - The "vanilla" cost is doubled because of being "treasure enchantment"
        // - The "vanilla" cost is not scaled by rarity
        ItemStack result = EnchantedBookItem.createForEnchantment(new EnchantmentInstance(this, enchantLevel));
        ItemHelper.of(result).updateDisplay(); // set lore
        int k = 0;
        switch (getRarity()) {
            case COMMON -> k = 1;
            case UNCOMMON -> k = 2;
            case RARE -> k = 5;
            case VERY_RARE -> k = 10;
            default -> {
                throw new UnsupportedOperationException("not implemented");
            }
        }
        int j = k * 2 + enchantLevel * 3 + randomSource.nextInt(5 + enchantLevel * 5 * k);
        return new MerchantOffer(
                new ItemStack(Items.EMERALD, Math.min(j, 64)),
                new ItemStack(Items.BOOK),
                result,
                offer.getMaxUses(), offer.getXp(), offer.getPriceMultiplier()
        );
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
