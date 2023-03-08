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

    public void dig(ServerPlayer player, BlockPos origin, BlockState originState) {
        // ignoredPlayers prevents infinite recursion
        if (((ServerPlayerAccessor) player).shouldIgnoreDigger()) return;
        ((ServerPlayerAccessor) player).ignoreDigger(true);

        for (int x = -RADIUS; x <= RADIUS; x++) {
            for (int y = -RADIUS; y <= RADIUS; y++) {
                for (int z = -RADIUS; z <= RADIUS; z++) {
                    BlockPos p = origin.offset(x, y, z);
                    BlockState blockState = player.level.getBlockState(p);
                    // must not be origin (it has already broken)
                    // must not in blacklist
                    // the destruction time must not exceed the origin's
                    // the block can be interacted (with spawn check later)
                    // must have correct tool used
                    if (p != origin && !blockState.is(GrimoireBlockTags.DIGGER_BLACKLIST) &&
                            blockState.getBlock().defaultDestroyTime() <= originState.getBlock().defaultDestroyTime() &&
                            player.level.mayInteract(player, p) &&
                            player.hasCorrectToolForDrops(blockState)) {
                        // this handles the rest: do extra validation
                        // and calculate exp, drops, sound, item durability, etc
                        player.gameMode.destroyBlock(p);
                    }
                }
            }
        }

        ((ServerPlayerAccessor) player).ignoreDigger(false);
    }
}
