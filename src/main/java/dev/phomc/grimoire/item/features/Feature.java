package dev.phomc.grimoire.item.features;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;

public abstract class Feature {
    public abstract void load(ItemStack itemStack);

    public abstract void save(ItemStack itemStack);

    public abstract void reset(ItemStack itemStack);

    public static String GRIMOIRE_TAG = "grimoire";

    public CompoundTag getGrimoireTag(ItemStack itemStack) {
        CompoundTag compoundTag = itemStack.getTag();
        if (compoundTag != null && compoundTag.contains(GRIMOIRE_TAG, Tag.TAG_COMPOUND)) {
            return compoundTag.getCompound(GRIMOIRE_TAG);
        }
        return null;
    }

    public CompoundTag getOrCreateGrimoireTag(ItemStack itemStack) {
        CompoundTag compoundTag = itemStack.getOrCreateTag();
        if (compoundTag.contains(GRIMOIRE_TAG, Tag.TAG_COMPOUND)) {
            return compoundTag.getCompound(GRIMOIRE_TAG);
        }
        CompoundTag grimoireTag = new CompoundTag();
        compoundTag.put(GRIMOIRE_TAG, grimoireTag);
        return grimoireTag;
    }

    public CompoundTag getGrimoireElement(ItemStack itemStack, String elem) {
        CompoundTag compoundTag = getGrimoireTag(itemStack);
        return compoundTag == null || !compoundTag.contains(elem, Tag.TAG_COMPOUND) ? null : compoundTag.getCompound(elem);
    }

    public CompoundTag getOrCreateGrimoireElement(ItemStack itemStack, String elem) {
        CompoundTag compoundTag = getOrCreateGrimoireTag(itemStack);
        if (compoundTag.contains(elem, Tag.TAG_COMPOUND)) {
            return compoundTag.getCompound(elem);
        }
        CompoundTag tag = new CompoundTag();
        compoundTag.put(elem, tag);
        return tag;
    }

    public void removeGrimoireElement(ItemStack itemStack, String elem) {
        CompoundTag compoundTag = itemStack.getTag();
        if (compoundTag != null && compoundTag.contains(GRIMOIRE_TAG, Tag.TAG_COMPOUND)) {
            CompoundTag grimoireTag = compoundTag.getCompound(GRIMOIRE_TAG);
            grimoireTag.remove(elem);
            if (grimoireTag.isEmpty()) {
                itemStack.removeTagKey(GRIMOIRE_TAG);
            }
        }
    }
}
