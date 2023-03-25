package dev.phomc.grimoire.utils;

import dev.phomc.grimoire.enchantment.GrimoireEnchantment;
import dev.phomc.grimoire.enchantment.property.ConditionalProperty;
import dev.phomc.grimoire.enchantment.property.DecimalProperty;
import dev.phomc.grimoire.enchantment.property.IntegerProperty;
import dev.phomc.grimoire.enchantment.property.Property;
import net.minecraft.network.chat.Component;
import org.apache.commons.lang3.text.WordUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
    private static final Pattern PLACEHOLDER_PATTERN =Pattern.compile("\\{[A-Za-z0-9_\\-%:]+}");
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.##");

    // big brain: https://leetcode.com/problems/integer-to-roman/solutions/6274/simple-solution/
    public static String intToRoman(int num) {
        String[] M = {"", "M", "MM", "MMM"};
        String[] C = {"", "C", "CC", "CCC", "CD", "D", "DC", "DCC", "DCCC", "CM"};
        String[] X = {"", "X", "XX", "XXX", "XL", "L", "LX", "LXX", "LXXX", "XC"};
        String[] I = {"", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX"};
        return M[num/1000] + C[(num%1000)/100] + X[(num%100)/10] + I[num%10];
    }

    public static List<Component> formatEnchantmentDesc(Component component, GrimoireEnchantment enc, int level) {
        List<Component> lines = new ArrayList<>();
        for (String s : WordUtils.wrap(formatEnchantmentDesc(component.getString(), enc, level), 40, "\n", false).split("\\n")) {
            lines.add(Component.literal(s));
        }
        return lines;
    }

    public static String formatEnchantmentDesc(String str, GrimoireEnchantment enc, int level) {
        Matcher matcher = PLACEHOLDER_PATTERN.matcher(str);
        StringBuilder stringBuilder = new StringBuilder(str.length());

        while (matcher.find()) {
            String matched = matcher.group();
            String rawPropertyKey = matched.substring(1, matched.length() - 1).trim();
            String[] propertyKeyParams = rawPropertyKey.split(":");
            String format = propertyKeyParams.length == 1 ? "" : propertyKeyParams[0];
            String key = propertyKeyParams.length == 1 ? propertyKeyParams[0] : propertyKeyParams[1];
            String replaced = matched;
            Property<?> property = enc.getProperties().get(key);

            if (property != null) {
                if (property instanceof ConditionalProperty p) {
                    boolean val = p.evaluate(level);
                    switch (format) {
                        case "i" -> replaced = Component.translatable(val ? "grimoire.enchantment.desc.tick" : "grimoire.enchantment.desc.cross").getString();
                        case "yn" -> replaced = Component.translatable(val ? "grimoire.enchantment.desc.yes" : "grimoire.enchantment.desc.no").getString();
                        default -> replaced = String.valueOf(val);
                    }
                } else if (property instanceof IntegerProperty p) {
                    int val = p.evaluate(level);
                    switch (format) {
                        case "I" -> replaced = val == 0 ? "0" : intToRoman(val);
                        case "ts" -> replaced = String.valueOf(val / 20.0);
                        default -> replaced = String.valueOf(val);
                    }
                } else if (property instanceof DecimalProperty p) {
                    double val = p.evaluate(level);
                    switch (format) {
                        case "ts" -> replaced = DECIMAL_FORMAT.format(val / 20.0);
                        case "%" -> replaced = DECIMAL_FORMAT.format(val * 100.0);
                        default -> replaced = DECIMAL_FORMAT.format(val);
                    }
                }
            }

            matcher.appendReplacement(stringBuilder, replaced);
        }

        matcher.appendTail(stringBuilder);
        return stringBuilder.toString();
    }
}
