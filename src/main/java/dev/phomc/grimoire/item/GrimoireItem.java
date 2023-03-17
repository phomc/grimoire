package dev.phomc.grimoire.item;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public abstract class GrimoireItem {
    public abstract void onUse();

    public abstract ItemStack getIcon();

    private final ResourceLocation identifier;

    public GrimoireItem(ResourceLocation identifier) {
        this.identifier = identifier;
    }

    public ResourceLocation getIdentifier() {
        return identifier;
    }
}
