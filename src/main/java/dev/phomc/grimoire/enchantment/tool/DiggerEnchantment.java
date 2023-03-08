package dev.phomc.grimoire.enchantment.tool;

import dev.phomc.grimoire.accessor.ServerPlayerAccessor;
import dev.phomc.grimoire.enchantment.EnchantmentRarity;
import dev.phomc.grimoire.enchantment.EnchantmentTarget;
import dev.phomc.grimoire.enchantment.GrimoireEnchantment;
import dev.phomc.grimoire.tags.GrimoireBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class DiggerEnchantment extends GrimoireEnchantment {
    private static final int RADIUS = 1;

    public DiggerEnchantment(@NotNull ResourceLocation identifier) {
        super(identifier, EnchantmentRarity.RARE, EnchantmentTarget.PICKAXE);
    }

    public void dig(ServerPlayer player, BlockPos origin, BlockState blockState) {
        // ignoredPlayers prevents infinite recursion
        if (((ServerPlayerAccessor) player).shouldIgnoreDigger()) return;
        ((ServerPlayerAccessor) player).ignoreDigger(true);

        for (int x = -RADIUS; x <= RADIUS; x++) {
            for (int y = -RADIUS; y <= RADIUS; y++) {
                for (int z = -RADIUS; z <= RADIUS; z++) {
                    BlockPos p = origin.offset(x, y, z);
                    // must not be origin (it has already broken)
                    // must not in blacklist
                    // must have correct tool used
                    if (p != origin && !blockState.is(GrimoireBlockTags.DIGGER_BLACKLIST) && player.hasCorrectToolForDrops(blockState)) {
                        // this handles the rest (exp, drops, sound, item durability, etc)
                        player.gameMode.destroyBlock(p);
                    }
                }
            }
        }

        ((ServerPlayerAccessor) player).ignoreDigger(false);
    }
}
