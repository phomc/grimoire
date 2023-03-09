package dev.phomc.grimoire.enchantment;

import dev.phomc.grimoire.utils.StringUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class DummyEnchantment extends Enchantment {
    public DummyEnchantment(Enchantment.Rarity rarity, EnchantmentCategory category, EquipmentSlot[] equipmentSlots) {
        super(rarity, category, equipmentSlots);
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public final int getMinLevel() {
        return 1;
    }

    @Override
    public final int getMinCost(int i) {
        return Integer.MAX_VALUE;
    }

    @Override
    public final int getMaxCost(int i) {
        return Integer.MAX_VALUE;
    }

    @Override
    public final Component getFullname(int lv) {
        MutableComponent mutableComponent = Component.translatable(this.getDescriptionId());
        if (this.isCurse()) {
            mutableComponent.withStyle(Style.EMPTY.withColor(ChatFormatting.RED).withItalic(false));
        } else {
            mutableComponent.withStyle(Style.EMPTY.withColor(ChatFormatting.GRAY).withItalic(false));
        }
        if (lv != 1 || getMaxLevel() != 1) {
            mutableComponent.append(CommonComponents.SPACE).append(StringUtils.intToRoman(lv));
        }
        return mutableComponent;
    }

    @Override
    public final boolean isTreasureOnly() {
        return true;
    }

    @Override
    public final boolean isTradeable() {
        return false;
    }

    @Override
    public final boolean isDiscoverable() {
        return false;
    }
}
