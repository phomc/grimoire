package dev.phomc.grimoire.tags;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class GrimoireBlockTags {
    public static final TagKey<Block> DIGGER_BLACKLIST = create("digger_blacklist");
    public static final TagKey<Block> TUNNEL_BLACKLIST = create("tunnel_blacklist");

    private static TagKey<Block> create(String id) {
        return TagKey.create(Registries.BLOCK, new ResourceLocation("grimoire", id));
    }
}
