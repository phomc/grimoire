package dev.phomc.grimoire;

import dev.phomc.grimoire.enchantment.GrimoireEnchantment;

import java.util.Arrays;
import java.util.stream.Collectors;

public class EnchantmentTest {
    public static void main(String[] args) {
        System.out.println("PROBABILITY PER ENCHANTMENT LEVEL");
        for (int i = 1; i <= 6; i++) {
            System.out.println("If maximum level = " + i);
            for (int j = 0; j <= 3; j++) {
                System.out.printf("- and rarity diff = %d, then chances per level = %s\n", j, Arrays.stream(GrimoireEnchantment.getNormalizedLevelProbability(1, i, j))
                        .mapToObj(value -> String.format("%.2f%%", value * 100))
                        .collect(Collectors.joining(", ")));
            }
        }
    }
}
