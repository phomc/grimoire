package dev.phomc.grimoire.utils;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ItemStackUtils {

    @Nullable
    public static List<Component> getLore(@Nullable ItemStack itemStack) {
        if (itemStack == null || itemStack.getTag() == null) {
            return null;
        }
        if (itemStack.getTag().getTagType(ItemStack.TAG_LORE) != Tag.TAG_LIST) {
            return null;
        }
        ListTag listTag = itemStack.getTag().getList(ItemStack.TAG_LORE, Tag.TAG_STRING);
        List<Component> lines = new ArrayList<>(listTag.size());
        for (Tag tag : listTag) {
            if (tag instanceof StringTag) {
                lines.add(Component.Serializer.fromJson(tag.getAsString()));
            }
        }
        return lines;
    }

    public static void setLore(@NotNull ItemStack itemStack, List<Component> lines) {
        CompoundTag compoundTag = itemStack.getOrCreateTag();
        ListTag listTag = new ListTag();
        for (Component component : lines) {
            listTag.add(StringTag.valueOf(Component.Serializer.toJson(component)));
        }
        compoundTag.put(ItemStack.TAG_LORE, listTag);
    }
}
