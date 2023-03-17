package dev.phomc.grimoire.item.features;

import dev.phomc.grimoire.item.GrimoireItem;
import dev.phomc.grimoire.item.ItemRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CustomItemFeature extends Feature {
    private ResourceLocation itemId;
    private CompoundTag data;

    public void setItemId(ResourceLocation itemId) {
        this.itemId = itemId;
    }

    public void setData(CompoundTag data) {
        this.data = data;
    }

    @Nullable
    public ResourceLocation getItemId() {
        return itemId;
    }

    @Nullable
    public CompoundTag getData() {
        return data;
    }

    @NotNull
    public CompoundTag getOrCreateData() {
        return data == null ? (data = new CompoundTag()) : data;
    }

    public boolean isCustomItem() {
        return itemId != null;
    }

    @Nullable
    public GrimoireItem identify() {
        return ItemRegistry.ALL.get(itemId);
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
