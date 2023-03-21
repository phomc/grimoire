package dev.phomc.grimoire.item.features;

import dev.phomc.grimoire.item.Gemstone;
import dev.phomc.grimoire.utils.ItemStackUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.EnumUtils;

public class GemstoneElementFeature implements Feature {
    public Gemstone element;

    @Override
    public void load(ItemStack itemStack) {
        CompoundTag tag = ItemStackUtils.getGrimoireTag(itemStack);
        if (tag == null) return;
        String s = tag.getString("gemstoneElement");
        element = EnumUtils.getEnumIgnoreCase(Gemstone.class, s);
    }

    @Override
    public void save(ItemStack itemStack) {
        if (element == null) {
            reset(itemStack);
        } else {
            ItemStackUtils.getOrCreateGrimoireTag(itemStack).putString("gemstoneElement", element.name());
        }
    }

    @Override
    public void reset(ItemStack itemStack) {
        CompoundTag tag = ItemStackUtils.getGrimoireTag(itemStack);
        if (tag == null) return;
        tag.remove("gemstoneElement");
    }
}
