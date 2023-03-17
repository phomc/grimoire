package dev.phomc.grimoire.item;

import com.google.common.base.CaseFormat;
import dev.phomc.grimoire.item.gemstone.GemstoneItem;
import dev.phomc.grimoire.item.incanting.InkwellItem;
import net.minecraft.resources.ResourceLocation;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.Map;

public class ItemRegistry {
    public static Map<ResourceLocation, GrimoireItem> ALL = new LinkedHashMap<>(); // preserve order

    public static void init() {
        registerItem(GemstoneItem.class);
        registerItem(InkwellItem.class);
    }

    private static void registerItem(Class<? extends GrimoireItem> clazz) {
        try {
            String id = clazz.getSimpleName();
            if (!id.matches("\\w{3,}Item")) throw new RuntimeException("Invalid Item class name");
            id = id.substring(0, id.length() - "Item".length());
            ResourceLocation identifier = new ResourceLocation("grimoire", CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, id));
            GrimoireItem instance = clazz.getDeclaredConstructor(ResourceLocation.class).newInstance(identifier);

            try {
                ItemRegistry.class.getDeclaredField(CaseFormat.UPPER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, id)).set(null, instance);
            } catch (IllegalAccessException | NoSuchFieldException ignored) {}

            ALL.put(identifier, instance);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
