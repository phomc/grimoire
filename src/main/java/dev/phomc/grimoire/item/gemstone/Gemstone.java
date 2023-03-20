package dev.phomc.grimoire.item.gemstone;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

public enum Gemstone {
    MUSGRAVITE(Items.NETHERITE_INGOT, null),
    JADE(Items.EMERALD, null),
    SAPPHIRE(Items.DIAMOND, null),
    TOPAZ(Items.GOLD_INGOT, null);

    private final String id;
    private final Item backend;
    private final Item.Properties properties;

    Gemstone(Item backend, Item.Properties properties) {
        this.id = name().toLowerCase();
        this.backend = backend;
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
    public Item.Properties getProperties() {
        return properties;
    }

    @NotNull
    public MutableComponent getDisplayName() {
        return Component.translatable("item.grimoire." + id);
    }
}
