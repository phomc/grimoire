package dev.phomc.grimoire.item.features;

import net.minecraft.world.item.ItemStack;

public interface Feature {
    void load(ItemStack itemStack);

    void save(ItemStack itemStack);

    void reset(ItemStack itemStack);
}
