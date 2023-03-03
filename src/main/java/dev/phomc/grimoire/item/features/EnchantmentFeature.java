package dev.phomc.grimoire.item.features;

import dev.phomc.grimoire.enchantment.Enchantment;
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
import java.util.function.Predicate;

public class EnchantmentFeature implements ItemFeature, Displayable {
    private static final String ENC_TAG = "enchant";
    private static final String ID_TAG = "id";
    private static final String LV_TAG = "lv";
    private static final MutableComponent[] PREFIX_IDENTIFIER = new MutableComponent[]{
            // &a&l&c&r
            Component.empty().withStyle(ChatFormatting.GREEN, ChatFormatting.BOLD),
            Component.empty().withStyle(ChatFormatting.RED, ChatFormatting.RESET)
    };
    private static final Predicate<Component> PREFIX_CHECK = component -> {
        List<Component> flatten = component.toFlatList();
        // must have prefix + at least 1 component
        if (flatten.size() <= PREFIX_IDENTIFIER.length) return false;
        for (int i = 0; i < PREFIX_IDENTIFIER.length; i++) {
            if (!flatten.get(i).equals(PREFIX_IDENTIFIER[i])) {
                return false;
            }
        }
        return true;
    };

    public final Map<Enchantment, Byte> enchantments = new Object2ByteLinkedOpenHashMap<>(3); // preserve order

    public byte getEnchantment(Enchantment enchantment) {
        return enchantments.getOrDefault(enchantment, (byte) 0);
    }

    @Override
    public void load(ItemStack itemStack) {
        CompoundTag compoundTag = itemStack.getTag();
        if (compoundTag == null || !compoundTag.contains(ENC_TAG, Tag.TAG_COMPOUND)) return;
        ListTag listTag = compoundTag.getList(ENC_TAG, Tag.TAG_COMPOUND);
        for (Tag elem : listTag) {
            if (elem instanceof CompoundTag) {
                byte lv = ((CompoundTag) elem).getByte(LV_TAG);
                if (lv < 1) return;
                String id = ((CompoundTag) elem).getString(ID_TAG);
                Enchantment enchantment = EnchantmentRegistry.ALL.get(new ResourceLocation(id));
                if (enchantment != null) {
                    enchantments.put(enchantment, lv);
                }
            }
        }
    }

    @Override
    public void save(ItemStack itemStack) {
        CompoundTag compoundTag = itemStack.getOrCreateTag();
        ListTag listTag = new ListTag();
        compoundTag.put(ENC_TAG, listTag);
        for (Map.Entry<Enchantment, Byte> e : enchantments.entrySet()) {
            CompoundTag child = new CompoundTag();
            child.putString(ID_TAG, e.getKey().getIdentifier().toString());
            child.putByte(LV_TAG, e.getValue());
            listTag.add(child);
        }
    }

    @Override
    public void reset(ItemStack itemStack) {
        itemStack.removeTagKey(ENC_TAG);
    }

    @Override
    public void displayLore(List<Component> lines) {
        for (Enchantment e : enchantments.keySet()) {
            String lv = StringUtils.intToRoman(enchantments.get(e));
            MutableComponent line = Component.empty();
            for (MutableComponent mutableComponent : PREFIX_IDENTIFIER) {
                line.append(mutableComponent);
            }
            MutableComponent text = Component.empty().withStyle(e.getRarity().color).append(e.getDisplayName()).append(" ").append(lv);
            line.append(text);
            lines.add(line);
        }
    }

    @Override
    public void hideLore(List<Component> lines) {
        lines.removeIf(PREFIX_CHECK);
    }
}
