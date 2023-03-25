package dev.phomc.grimoire.enchantment.tool;

import dev.phomc.grimoire.enchantment.EnchantmentTarget;
import dev.phomc.grimoire.enchantment.property.IntegerProperty;
import dev.phomc.grimoire.tags.GrimoireBlockTags;
import dev.phomc.grimoire.utils.MathUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class TunnelEnchantment extends AbstractDiggingEnchantment {
    public TunnelEnchantment(@NotNull ResourceLocation identifier) {
        super(identifier, Rarity.UNCOMMON, EnchantmentTarget.PICKAXE.or(EnchantmentTarget.SHOVEL));

        createProperty("width", (IntegerProperty) level -> level + 1);
    }

    public int getMaxLevel() {
        return 2;
    }

    @Override
    protected void onTriggered(Excavator excavator) {
        int level = clampLevel(excavator.enchantLevel());
        Vec3 v = MathUtils.getDirection(excavator.player());
        for (int i = 1; i <= level; i++) {
            BlockPos p = excavator.originPos().offset(
                    (int) Math.round(v.x * i),
                    (int) Math.round(v.y * i),
                    (int) Math.round(v.z * i)
            );
            excavator.tryDig(p, GrimoireBlockTags.TUNNEL_BLACKLIST);
        }
    }
}
