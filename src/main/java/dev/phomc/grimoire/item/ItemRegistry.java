package dev.phomc.grimoire.item;

import dev.phomc.grimoire.Grimoire;
import dev.phomc.grimoire.item.custom.*;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class ItemRegistry {
    public static GemstoneItem MUSGRAVITE;
    public static GemstoneItem JADE;
    public static GemstoneItem SAPPHIRE;
    public static GemstoneItem TOPAZ;
    public static QuillItem QUILL;
    public static UnidentifiedGrimoireItem UNIDENTIFIED_GRIMOIRE;

    public static void init() {
        for (Gemstone value : Gemstone.values()) {
            registerItem(value.getId(), new GemstoneItem(value, value.getProperties()));
            registerItem(value.getId() + "_inkwell", new InkwellItem(value, new Item.Properties().stacksTo(1)));
        }
        registerItem("parchment", new ParchmentItem(new Item.Properties().stacksTo(16)));
        registerItem("quill", new QuillItem(new Item.Properties()));
        registerItem("unidentified_grimoire", new UnidentifiedGrimoireItem(new Item.Properties().stacksTo(1)));
    }

    private static void registerItem(String id, Item item) {
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation("grimoire", id), item);

        try {
            ItemRegistry.class.getDeclaredField(id.toUpperCase()).set(null, item);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            Grimoire.LOGGER.warn("No public field specified for item '{}' ", id);
        }
    }
}
