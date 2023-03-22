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
import org.jetbrains.annotations.NotNull;

public class DummyEnchantment extends Enchantment {
    public static final Style CURSE_DISPLAY_NAME_FORMAT = Style.EMPTY
            .withColor(ChatFormatting.RED)
            .withItalic(false);
    public static final Style NORMAL_DISPLAY_NAME_FORMAT = Style.EMPTY
            .withColor(ChatFormatting.GRAY)
            .withItalic(false);

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

    public final @NotNull MutableComponent getDisplayName() {
        MutableComponent mutableComponent = Component.translatable(this.getDescriptionId());
        if (this.isCurse()) {
            mutableComponent.withStyle(CURSE_DISPLAY_NAME_FORMAT);
        } else {
            mutableComponent.withStyle(NORMAL_DISPLAY_NAME_FORMAT);
        }
        return mutableComponent;
    }

    @Override
    public final @NotNull Component getFullname(int lv) {
        MutableComponent mutableComponent = getDisplayName();
        if (lv != 1 || getMaxLevel() != 1) {
            mutableComponent.append(CommonComponents.SPACE).append(StringUtils.intToRoman(lv));
        }
        return mutableComponent;
    }

    public final @NotNull Component getRarityDisplay() {
        MutableComponent mutableComponent = Component.translatable("grimoire.enchantment_rarity." + getRarity().name().toLowerCase());
        switch (getRarity()) {
            case COMMON -> mutableComponent.withStyle(ChatFormatting.GREEN);
            case UNCOMMON -> mutableComponent.withStyle(ChatFormatting.YELLOW);
            case RARE -> mutableComponent.withStyle(ChatFormatting.GOLD);
            case VERY_RARE -> mutableComponent.withStyle(ChatFormatting.RED);
        }
        return mutableComponent;
    }

    @Override
    public final boolean isTreasureOnly() {
        return true;
    }

    @Override
    public final boolean isTradeable() {
        // allows in trading
        return getRarity() == Rarity.COMMON || getRarity() == Rarity.UNCOMMON;
    }

    @Override
    public final boolean isDiscoverable() {
        return false;
    }
}
