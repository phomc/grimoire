package dev.phomc.grimoire.enchantment;

import dev.phomc.grimoire.Grimoire;
import dev.phomc.grimoire.enchantment.property.ConditionalProperty;
import dev.phomc.grimoire.enchantment.property.NumericProperty;
import dev.phomc.grimoire.enchantment.property.Property;
import dev.phomc.grimoire.item.Gemstone;
import eu.pb4.polymer.core.api.other.PolymerEnchantment;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.trading.MerchantOffer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

public abstract class GrimoireEnchantment extends DummyEnchantment implements PolymerEnchantment, EnchantmentEventListener {
    public static double[] getLevelWeights(int minLv, int maxLv, int rarityDiff) {
        // TODO Cache this
        double[] weights = new double[maxLv - minLv + 1];
        for (int i = 0; i < weights.length; i++) {
            double k = (i + minLv - 1) / (double) maxLv;
            weights[i] = Math.max(0.0, 1.0 - k + rarityDiff * 0.1) * 100;
        }
        return weights;
    }

    public static double[] getNormalizedLevelProbability(int minLv, int maxLv, int rarityDiff) {
        double[] weights = getLevelWeights(minLv, maxLv, rarityDiff);
        double total = 0;
        for (double w : weights) {
            total += w;
        }
        for (int i = 0; i < weights.length; i++) {
            weights[i] /= total;
        }
        return weights;
    }

    private final ResourceLocation identifier;
    private final Predicate<Item> itemCheck;
    private final Map<String, Property> enchantmentProperties = new LinkedHashMap<>(5);

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

    @NotNull
    public Map<String, Property> getEnchantmentProperties() {
        return enchantmentProperties;
    }

    public void createProperty(String id, Property<?> property) {
        enchantmentProperties.put(id, property);
    }

    @NotNull
    public Property<?> requireProperty(String property) {
        return Objects.requireNonNull(enchantmentProperties.get(property));
    }

    @NotNull
    public NumericProperty requireNumericProperty(String property) {
        return (NumericProperty) requireProperty(property);
    }

    @NotNull
    public ConditionalProperty requireConditionalProperty(String property) {
        return (ConditionalProperty) requireProperty(property);
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

    public boolean isIdentifiableBy(Gemstone gemstone) {
        // Gemstone with rarity level N can identify enchantments rated level 1 to N
        return gemstone.getEnchantmentRarity().compareTo(getRarity()) >= 0;
    }

    public double[] getLevelWeights(Gemstone gemstone) {
        int diff = gemstone.getEnchantmentRarity().compareTo(getRarity());
        return getLevelWeights(getMinLevel(), getMaxLevel(), diff);
    }

    public MerchantOffer handleEnchantedBookOffer(Entity entity, RandomSource randomSource, MerchantOffer offer, Integer enchantLevel) {
        // Custom offer generation to fix two issues:
        // - The "vanilla" cost is doubled because of being "treasure enchantment"
        // - The "vanilla" cost is not scaled by rarity
        ItemStack result = EnchantedBookItem.createForEnchantment(new EnchantmentInstance(this, enchantLevel));
        int k;
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

    @Nullable
    @Override
    public Enchantment getPolymerReplacement(ServerPlayer player) {
        return null;
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
