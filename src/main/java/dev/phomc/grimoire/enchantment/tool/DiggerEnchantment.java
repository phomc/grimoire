package dev.phomc.grimoire.enchantment.tool;

import dev.phomc.grimoire.enchantment.EnchantmentTarget;
import dev.phomc.grimoire.tags.GrimoireBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;
import org.jetbrains.annotations.NotNull;

public class DiggerEnchantment extends AbstractDiggingEnchantment {
    private static final int RADIUS = 1;

    public DiggerEnchantment(@NotNull ResourceLocation identifier) {
        super(identifier, Rarity.VERY_RARE, EnchantmentTarget.PICKAXE);
    }

    @Override
    protected void onTriggered(Excavator excavator) {
        for (int x = -RADIUS; x <= RADIUS; x++) {
            for (int y = -RADIUS; y <= RADIUS; y++) {
                for (int z = -RADIUS; z <= RADIUS; z++) {
                    BlockPos p = excavator.originPos().offset(x, y, z);
                    excavator.tryDig(p, GrimoireBlockTags.DIGGER_BLACKLIST);
                }
            }
        }
    }
}
