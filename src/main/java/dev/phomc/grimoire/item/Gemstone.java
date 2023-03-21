package dev.phomc.grimoire.item;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import org.jetbrains.annotations.NotNull;

public enum Gemstone {
    MUSGRAVITE(Items.NETHERITE_INGOT, Enchantment.Rarity.VERY_RARE, new Item.Properties().fireResistant()),
    JADE(Items.EMERALD, Enchantment.Rarity.RARE, null),
    SAPPHIRE(Items.DIAMOND, Enchantment.Rarity.RARE, null),
    TOPAZ(Items.GOLD_INGOT, Enchantment.Rarity.UNCOMMON, null);

    private final String id;
    private final Item backend;
    private final Enchantment.Rarity enchantmentRarity;
    private final Item.Properties properties;

    Gemstone(Item backend, Enchantment.Rarity enchantmentRarity, Item.Properties properties) {
        this.id = name().toLowerCase();
        this.backend = backend;
        this.enchantmentRarity = enchantmentRarity;
        this.properties = properties == null ? new Item.Properties() : properties;
    }

    @NotNull
    public String getId() {
        return id;
    }

    @NotNull
    public Item getBackend() {
        return backend;
    }

    @NotNull
    public Enchantment.Rarity getEnchantmentRarity() {
        return enchantmentRarity;
    }

    @NotNull
    public Item.Properties getProperties() {
        return properties;
    }

    @NotNull
    public MutableComponent getDisplayName() {
        return Component.translatable("item.grimoire." + id);
    }
}
