package dev.phomc.grimoire.item.gemstone;

import dev.phomc.grimoire.item.GrimoireItem;
import dev.phomc.grimoire.item.ItemHelper;
import dev.phomc.grimoire.item.features.CustomItemFeature;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class GemstoneItem extends GrimoireItem {

    public GemstoneItem(ResourceLocation identifier) {
        super(identifier);
    }

    @Override
    public void onUse() {

    }

    @Override
    public ItemStack getIcon() {
        return null;
    }

    public ItemStack create(Gemstone type) {
        ItemStack itemStack = new ItemStack(type.getBackend(), 1);
        CustomItemFeature customItemFeature = ItemHelper.of(itemStack).getCustomItemFeature();
        customItemFeature.setItemId(getIdentifier());
        customItemFeature.getOrCreateData().putString("type", type.name());
        ItemHelper.of(itemStack).saveChanges();
    }

    public Gemstone classify(ItemStack itemStack) {
        CustomItemFeature customItemFeature = ItemHelper.of(itemStack).getCustomItemFeature();
        if () {

        }
    }
}
