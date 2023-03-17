package dev.phomc.grimoire.item.gemstone;

import dev.phomc.grimoire.item.GrimoireItem;
import dev.phomc.grimoire.item.ItemFeature;
import dev.phomc.grimoire.item.ItemHelper;
import dev.phomc.grimoire.item.features.CustomItemFeature;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.EnumUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

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

    @NotNull
    public ItemStack create(@NotNull Gemstone type) {
        ItemStack itemStack = new ItemStack(type.getBackend(), 1);
        ItemHelper.of(itemStack).requestFeatureAndSave(ItemFeature.CUSTOM_ITEM, new Consumer<CustomItemFeature>() {
            @Override
            public void accept(CustomItemFeature feature) {
                feature.setItemId(getIdentifier());
                feature.getOrCreateData().putString("type", type.name());
            }
        });
        return itemStack;
    }

    @Nullable
    public Gemstone classify(ItemStack itemStack) {
        CustomItemFeature customItemFeature = ItemHelper.of(itemStack).getFeature(ItemFeature.CUSTOM_ITEM);
        if (customItemFeature == null || customItemFeature.getData() == null || !(customItemFeature.identify() instanceof GemstoneItem)) {
            return null;
        }
        return EnumUtils.getEnumIgnoreCase(Gemstone.class, customItemFeature.getData().getString("type"));
    }
}
