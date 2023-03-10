package dev.phomc.grimoire.enchantment.tool;

import dev.phomc.grimoire.accessor.ServerPlayerAccessor;
import dev.phomc.grimoire.enchantment.GrimoireEnchantment;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public abstract class AbstractDiggingEnchantment extends GrimoireEnchantment {
    public AbstractDiggingEnchantment(@NotNull ResourceLocation identifier, @NotNull Rarity rarity, @NotNull Predicate<Item> itemCheck) {
        super(identifier, rarity, itemCheck);
    }

    protected abstract void onTriggered(Excavator excavator);

    public void trigger(ServerPlayer player, BlockPos origin, BlockState originState, ItemStack itemStack) {
        // ignoredPlayers prevents infinite recursion
        if (((ServerPlayerAccessor) player).shouldIgnoreDiggingEnchantment()) {
            return;
        }
        ((ServerPlayerAccessor) player).ignoreDiggingEnchantment(true);
        onTriggered(new Excavator(player, origin, originState, itemStack));
        ((ServerPlayerAccessor) player).ignoreDiggingEnchantment(false);
    }

    public record Excavator(ServerPlayer player, BlockPos originPos, BlockState originState, ItemStack itemStack) {
        public void tryDig(BlockPos pos, TagKey<Block> blacklist) {
            BlockState blockState = player.level.getBlockState(pos);
            if (!pos.equals(originPos) && (blacklist == null || !blockState.is(blacklist)) &&
                    blockState.getBlock().defaultDestroyTime() <= originState.getBlock().defaultDestroyTime() &&
                    player.level.mayInteract(player, pos) &&
                    itemStack.isCorrectToolForDrops(blockState)) {
                // this handles the rest: do extra validation
                // and calculate exp, drops, sound, item durability, etc
                player.gameMode.destroyBlock(pos);
            }
        }
    }
}
