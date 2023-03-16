package dev.phomc.grimoire.gemstone;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Material;
import org.jetbrains.annotations.NotNull;

public enum Gemstone {
    MUSGRAVITE(Items.NETHERITE_INGOT),
    JADE(Items.EMERALD),
    SAPPHIRE(Items.DIAMOND),
    TOPAZ(Items.GOLD_INGOT);

    private final Item backend;

    Gemstone(Item backend) {
        this.backend = backend;
    }

    @NotNull
    public Item getBackend() {
        return backend;
    }
}
