package dev.phomc.grimoire.item.features;

import dev.phomc.grimoire.enchantment.GrimoireEnchantment;
import dev.phomc.grimoire.enchantment.EnchantmentRegistry;
import dev.phomc.grimoire.utils.StringUtils;
import it.unimi.dsi.fastutil.objects.Object2ByteLinkedOpenHashMap;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Map;

public class EnchantmentFeature extends ItemFeature implements Displayable {
    public static final String ENC_TAG = "enchant";
    public static final String ID_TAG = "id";
    public static final String LV_TAG = "lv";

    public final Map<GrimoireEnchantment, Byte> enchantments = new Object2ByteLinkedOpenHashMap<>(3); // preserve order

    public byte getEnchantment(GrimoireEnchantment enchantment) {
        return enchantments.getOrDefault(enchantment, (byte) 0);
    }

    @Override
    public void load(ItemStack itemStack) {
        CompoundTag compoundTag = getGrimoireTag(itemStack);
        if (compoundTag == null || !compoundTag.contains(ENC_TAG, Tag.TAG_LIST)) return;
        ListTag listTag = compoundTag.getList(ENC_TAG, Tag.TAG_COMPOUND);
        for (Tag elem : listTag) {
            if (elem instanceof CompoundTag) {
                byte lv = ((CompoundTag) elem).getByte(LV_TAG);
                if (lv < 1) return;
                String id = ((CompoundTag) elem).getString(ID_TAG);
                GrimoireEnchantment enchantment = EnchantmentRegistry.ALL.get(new ResourceLocation(id));
                if (enchantment != null) {
                    enchantments.put(enchantment, lv);
                }
            }
        }
    }

    @Override
    public void save(ItemStack itemStack) {
        CompoundTag compoundTag = getOrCreateGrimoireTag(itemStack);
        ListTag listTag = new ListTag();
        compoundTag.put(ENC_TAG, listTag);
        for (Map.Entry<GrimoireEnchantment, Byte> e : enchantments.entrySet()) {
            CompoundTag child = new CompoundTag();
            child.putString(ID_TAG, e.getKey().getIdentifier().toString());
            child.putByte(LV_TAG, e.getValue());
            listTag.add(child);
        }
    }

    @Override
    public void reset(ItemStack itemStack) {
        removeGrimoireElement(itemStack, ENC_TAG);
    }

    @Override
    public void displayLore(List<Component> lines) {
        for (GrimoireEnchantment e : enchantments.keySet()) {
            MutableComponent mutableComponent = e.getDisplayName();
            mutableComponent.withStyle(ChatFormatting.GRAY);
            int lv = enchantments.get(e);
            if (lv != 1 || e.getMaxLevel() != 1) {
                mutableComponent.append(" " + StringUtils.intToRoman(lv));
            }
            lines.add(mutableComponent);
        }
    }
}
