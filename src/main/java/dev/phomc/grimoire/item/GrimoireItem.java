package dev.phomc.grimoire.item;

import net.minecraft.world.item.ItemStack;

public interface GrimoireItem {
    void onUse();

    ItemStack getIcon();
}
