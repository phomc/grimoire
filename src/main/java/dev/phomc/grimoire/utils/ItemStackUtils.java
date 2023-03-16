package dev.phomc.grimoire.utils;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ItemStackUtils {

    @Nullable
    public static List<Component> getLore(@Nullable ItemStack itemStack) {
        if (itemStack == null || itemStack.getTag() == null) {
            return null;
        }
        CompoundTag display = itemStack.getTagElement(ItemStack.TAG_DISPLAY);
        if (display == null || display.getTagType(ItemStack.TAG_LORE) != Tag.TAG_LIST) {
            return null;
        }
        ListTag listTag = display.getList(ItemStack.TAG_LORE, Tag.TAG_STRING);
        List<Component> lines = new ArrayList<>(listTag.size());
        for (Tag tag : listTag) {
            if (tag instanceof StringTag) {
                lines.add(Component.Serializer.fromJson(tag.getAsString()));
            }
        }
        return lines;
    }

    public static void setLore(@NotNull ItemStack itemStack, @Nullable List<Component> lines) {
        if (lines == null || lines.isEmpty()) {
            CompoundTag display = itemStack.getTagElement(ItemStack.TAG_DISPLAY);
            if (display != null) {
                display.remove(ItemStack.TAG_LORE);
                if (display.isEmpty()) {
                    itemStack.removeTagKey(ItemStack.TAG_DISPLAY);
                }
            }
            return;
        }
        CompoundTag display = itemStack.getOrCreateTagElement(ItemStack.TAG_DISPLAY);
        ListTag listTag = new ListTag();
        for (Component component : lines) {
            listTag.add(StringTag.valueOf(Component.Serializer.toJson(component)));
        }
        display.put(ItemStack.TAG_LORE, listTag);
    }

    public static void removeEnchantment(@Nullable ItemStack itemStack, Enchantment enchantment) {
        if (itemStack == null || itemStack.getTag() == null) {
            return;
        }
        if (itemStack.getTag().getTagType(ItemStack.TAG_ENCH) != Tag.TAG_LIST) {
            return;
        }
        ListTag listTag = itemStack.getTag().getList(ItemStack.TAG_ENCH, Tag.TAG_COMPOUND);
        ListTag newList = new ListTag();
        for (Tag elem : listTag) {
            if (elem instanceof CompoundTag) {
                String a = ((CompoundTag) elem).getString("id");
                String b = Objects.requireNonNull(EnchantmentHelper.getEnchantmentId(enchantment)).toString();
                if (!a.equals(b)) {
                    newList.add(elem);
                }
            }
        }
        if (newList.isEmpty()) {
            itemStack.removeTagKey(ItemStack.TAG_ENCH);
        } else {
            itemStack.getTag().put(ItemStack.TAG_ENCH, newList);
        }
    }
}
