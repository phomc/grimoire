package dev.phomc.grimoire.item.gemstone;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

public enum Gemstone {
    MUSGRAVITE(Items.NETHERITE_INGOT),
    JADE(Items.EMERALD),
    SAPPHIRE(Items.DIAMOND),
    TOPAZ(Items.GOLD_INGOT);

    private final String id;
    private final Item backend;

    Gemstone(Item backend) {
        this.id = name().toLowerCase();
        this.backend = backend;
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
    public MutableComponent getDisplayName() {
        return Component.translatable("grimoire.gemstone." + id);
    }
}
