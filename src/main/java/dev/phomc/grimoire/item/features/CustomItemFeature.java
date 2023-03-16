package dev.phomc.grimoire.item.features;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class CustomItemFeature extends Feature {
    private ResourceLocation itemId;
    private CompoundTag data;

    @Nullable
    public ResourceLocation getItemId() {
        return itemId;
    }

    @Nullable
    public CompoundTag getData() {
        return data;
    }

    @Override
    public void load(ItemStack itemStack) {
        CompoundTag tag = getGrimoireElement(itemStack, "custom");
        if (tag == null) return;
        itemId = new ResourceLocation(tag.getString("id"));
        data = tag.getCompound("data");
    }

    @Override
    public void save(ItemStack itemStack) {

    }

    @Override
    public void reset(ItemStack itemStack) {

    }
}
