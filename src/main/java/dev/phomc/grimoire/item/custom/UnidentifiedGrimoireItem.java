package dev.phomc.grimoire.item.custom;

import dev.phomc.grimoire.enchantment.EnchantmentRegistry;
import dev.phomc.grimoire.enchantment.GrimoireEnchantment;
import dev.phomc.grimoire.item.Gemstone;
import eu.pb4.polymer.core.api.item.PolymerItem;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class UnidentifiedGrimoireItem extends EnchantedBookItem implements PolymerItem {
    public UnidentifiedGrimoireItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public Item getPolymerItem(ItemStack itemStack, @Nullable ServerPlayer player) {
        return Items.ENCHANTED_BOOK;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {

    }

    public static ItemStack identify(Gemstone gemstone) {
        // TODO implement more identifiable stuff
        ItemStack out = new ItemStack(Items.ENCHANTED_BOOK);
        GrimoireEnchantment selected = selectEnchantment(gemstone);
        int lv = selectEnchantmentLevel(gemstone, selected);
        EnchantedBookItem.addEnchantment(out, new EnchantmentInstance(selected, lv));
        return out;
    }

    public static GrimoireEnchantment selectEnchantment(Gemstone gemstone) {
        List<GrimoireEnchantment> list = EnchantmentRegistry.ALL.values().stream()
                .filter(enchantment -> enchantment.isIdentifiableBy(gemstone))
                .toList();
        int totalWeight = 0;
        for (GrimoireEnchantment grimoireEnchantment : list) {
            totalWeight += grimoireEnchantment.getRarity().getWeight();
        }
        double val = ThreadLocalRandom.current().nextDouble(totalWeight);
        for (GrimoireEnchantment grimoireEnchantment : list) {
            if (val > 0) {
                val -= grimoireEnchantment.getRarity().getWeight();
            } else {
                return grimoireEnchantment;
            }
        }
        return list.get(ThreadLocalRandom.current().nextInt(list.size()));
    }

    public static int selectEnchantmentLevel(Gemstone gemstone, GrimoireEnchantment enchantment) {
        double[] chances = enchantment.getProbabilityPerLevel(gemstone);
        double total = 0;
        for (double chance : chances) {
            total += chance;
        }
        double val = ThreadLocalRandom.current().nextDouble(total);
        for (int i = 0; i < chances.length; i++) {
            if (val > 0) {
                val -= chances[i];
            } else {
                return enchantment.getMinLevel() + i;
            }
        }
        return enchantment.getMinLevel();
    }
}
