package dev.phomc.grimoire.item.incanting;

import dev.phomc.grimoire.item.GrimoireItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class InkwellItem extends GrimoireItem {
    public InkwellItem(ResourceLocation identifier) {
        super(identifier);
    }

    @Override
    public void onUse() {

    }

    @Override
    public ItemStack getIcon() {
        return null;
    }
}
