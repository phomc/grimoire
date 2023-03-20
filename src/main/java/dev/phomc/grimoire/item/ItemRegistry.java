package dev.phomc.grimoire.item;

import dev.phomc.grimoire.item.gemstone.Gemstone;
import dev.phomc.grimoire.item.gemstone.GemstoneItem;
import dev.phomc.grimoire.item.incanting.InkwellItem;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class ItemRegistry {
    public static GemstoneItem GEMSTONE;
    public static InkwellItem INKWELL;

    public static void init() {
        for (Gemstone value : Gemstone.values()) {
            registerItem(value.getId(), new GemstoneItem(value, value.getProperties()));
            registerItem(value.getId() + "_inkwell", new InkwellItem(value, new Item.Properties().stacksTo(1)));
        }
    }

    private static void registerItem(String id, Item item) {
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation("grimoire", id), item);
    }
}
