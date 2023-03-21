package dev.phomc.grimoire;

import dev.phomc.grimoire.enchantment.GrimoireEnchantment;

import java.util.Arrays;
import java.util.stream.Collectors;

public class EnchantmentTest {
    public static void main(String[] args) {
        for (int i = 1; i <= 7; i++) {
            for (int j = 0; j <= 3; j++) {
                System.out.printf("Probability lv 1 to %d, rarity diff %d: %s\n", i, j, Arrays.stream(GrimoireEnchantment.getProbabilityPerLevel(1, i, j))
                        .mapToObj(value -> String.format("%.2f", value))
                        .collect(Collectors.joining(", ")));
            }
        }
    }
}
