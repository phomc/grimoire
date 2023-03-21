package dev.phomc.grimoire.item;

import dev.phomc.grimoire.item.custom.GemstoneItem;
import dev.phomc.grimoire.item.custom.InkwellItem;
import dev.phomc.grimoire.item.custom.ParchmentItem;
import dev.phomc.grimoire.item.custom.QuillItem;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class ItemRegistry {
    public static QuillItem QUILL;

    public static void init() {
        for (Gemstone value : Gemstone.values()) {
            registerItem(value.getId(), new GemstoneItem(value, value.getProperties()));
            registerItem(value.getId() + "_inkwell", new InkwellItem(value, new Item.Properties().stacksTo(1)));
        }
        registerItem("parchment", new ParchmentItem(new Item.Properties().stacksTo(16)));
        registerItem("quill", QUILL = new QuillItem(new Item.Properties()));
    }

    private static void registerItem(String id, Item item) {
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation("grimoire", id), item);
    }
}
