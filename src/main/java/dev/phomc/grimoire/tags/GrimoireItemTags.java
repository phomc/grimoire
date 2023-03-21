package dev.phomc.grimoire.tags;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class GrimoireItemTags {
    public static final TagKey<Item> GEMSTONE = create("gemstone");
    public static final TagKey<Item> INKWELL = create("inkwell");

    private static TagKey<Item> create(String id) {
        return TagKey.create(Registries.ITEM, new ResourceLocation("grimoire", id));
    }
}
