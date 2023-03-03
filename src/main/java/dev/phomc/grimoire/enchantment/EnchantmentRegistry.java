package dev.phomc.grimoire.enchantment;

import com.google.common.base.CaseFormat;
import dev.phomc.grimoire.Grimoire;
import dev.phomc.grimoire.enchantment.weapon.ColorShuffleEnchantment;
import net.minecraft.resources.ResourceLocation;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.Map;

public class EnchantmentRegistry {
    public static Map<ResourceLocation, Enchantment> ALL = new LinkedHashMap<>(); // preserve order

    public static Enchantment COLOR_SHUFFLE;

    public static void init() {
        registerEnchant(ColorShuffleEnchantment.class);
    }

    private static Enchantment registerEnchant(Class<? extends Enchantment> clazz) {
        try {
            String id = clazz.getSimpleName();
            if (!id.matches("\\w{3,}Enchantment")) throw new RuntimeException("Invalid Enchantment class name");
            id = id.substring(0, id.length() - "Enchantment".length()).toLowerCase();
            ResourceLocation identifier = new ResourceLocation("grimoire", id);
            Enchantment instance = clazz.getDeclaredConstructor(ResourceLocation.class).newInstance(identifier);

            try {
                EnchantmentRegistry.class.getDeclaredField(CaseFormat.UPPER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, id)).set(null, instance);
            } catch (IllegalAccessException | NoSuchFieldException e) {
                Grimoire.LOGGER.warn("No public field specified for enchantment '{}' ", id);
            }

            ALL.put(identifier, instance);
            Grimoire.LOGGER.info("Enchantment '{}' registered", id);
            return instance;
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
