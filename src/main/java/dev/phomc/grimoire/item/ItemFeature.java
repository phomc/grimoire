package dev.phomc.grimoire.item;

import dev.phomc.grimoire.item.features.EnchantmentFeature;
import dev.phomc.grimoire.item.features.Feature;
import dev.phomc.grimoire.item.features.GemstoneElementFeature;

import java.util.function.Supplier;

public enum ItemFeature {
    ENCHANTMENT(EnchantmentFeature::new),
    GEMSTONE_ELEMENT(GemstoneElementFeature::new);

    private final Supplier<? extends Feature> creator;

    ItemFeature(Supplier<? extends Feature> creator) {
        this.creator = creator;
    }

    public <T extends Feature> T create() {
        //noinspection unchecked
        return (T) creator.get();
    }
}
