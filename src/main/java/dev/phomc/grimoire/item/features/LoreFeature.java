package dev.phomc.grimoire.item.features;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class LoreFeature extends ItemFeature implements Displayable {
    public static final String LORE_TAG = "lore";
    public List<Component> lore = new ArrayList<>();

    @Override
    public void displayLore(List<Component> lines) {
        lines.addAll(lore);
    }

    @Override
    public void load(ItemStack itemStack) {
        CompoundTag compoundTag = getGrimoireTag(itemStack);
        if (compoundTag == null || !compoundTag.contains(LORE_TAG, Tag.TAG_LIST)) return;
        ListTag listTag = compoundTag.getList(LORE_TAG, Tag.TAG_STRING);
        for (Tag elem : listTag) {
            if (elem instanceof StringTag) {
                lore.add(Component.Serializer.fromJson(elem.getAsString()));
            }
        }
    }

    @Override
    public void save(ItemStack itemStack) {
        if (lore.isEmpty()) {
            reset(itemStack);
            return;
        }
        CompoundTag compoundTag = getOrCreateGrimoireTag(itemStack);
        ListTag listTag = new ListTag();
        for (Component component : lore) {
            listTag.add(StringTag.valueOf(Component.Serializer.toJson(component)));
        }
        compoundTag.put(LORE_TAG, listTag);
    }

    @Override
    public void reset(ItemStack itemStack) {
        removeGrimoireElement(itemStack, LORE_TAG);
    }
}
