package dev.phomc.grimoire.mixin.block;

import dev.phomc.grimoire.enchantment.EnchantmentRegistry;
import dev.phomc.grimoire.enchantment.EnchantmentTarget;
import dev.phomc.grimoire.item.ItemHelper;
import dev.phomc.grimoire.tags.GrimoireBlockTags;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(Block.class)
public abstract class BlockMixin {
    @Inject(
            method = "getDrops(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/entity/BlockEntity;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/item/ItemStack;)Ljava/util/List;",
            at = @At("RETURN"),
            cancellable = true
    )
    private static void getDrops(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos,
                                 @Nullable BlockEntity blockEntity, @Nullable Entity entity, ItemStack itemStack,
                                 CallbackInfoReturnable<List<ItemStack>> cir) {
        if(itemStack == null || itemStack.isEmpty()) return;
        if (EnchantmentTarget.DIGGER.test(itemStack.getItem())) {
            int lv = ItemHelper.of(itemStack).getEnchantmentFeature().getEnchantment(itemStack, EnchantmentRegistry.SMELTING);
            if (lv > 0 && itemStack.isCorrectToolForDrops(blockState) && EnchantmentRegistry.SMELTING.shouldSmelt(lv)) {
                List<Pair<Ingredient, ItemStack>> recipes = serverLevel.getRecipeManager().getAllRecipesFor(RecipeType.SMELTING)
                        .stream()
                        .map(r -> Pair.of(r.getIngredients().get(0), r.getResultItem(serverLevel.registryAccess())))
                        .toList();
                List<ItemStack> returning = new ObjectArrayList<>();
                outer:
                for (ItemStack i : cir.getReturnValue()) {
                    for (Pair<Ingredient, ItemStack> pair : recipes) {
                        if (pair.getKey().test(i)) {
                            returning.add(pair.getValue().copyWithCount(i.getCount()));
                            continue outer;
                        }
                    }
                    returning.add(i);
                }
                cir.setReturnValue(returning);
            }
        }
    }

    @Inject(
            method = "playerDestroy(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/block/entity/BlockEntity;Lnet/minecraft/world/item/ItemStack;)V",
            at = @At("TAIL")
    )
    public void playerDestroy(Level level, Player player, BlockPos blockPos, BlockState blockState, BlockEntity blockEntity, ItemStack itemStack, CallbackInfo ci) {
        if(itemStack == null || itemStack.isEmpty()) return;
        if (EnchantmentTarget.DIGGER.test(itemStack.getItem())) {
            int lv = ItemHelper.of(itemStack).getEnchantmentFeature().getEnchantment(itemStack, EnchantmentRegistry.DIGGER);
            if (lv > 0) {
                if (!blockState.is(GrimoireBlockTags.DIGGER_BLACKLIST) && itemStack.isCorrectToolForDrops(blockState)) {
                    EnchantmentRegistry.DIGGER.trigger((ServerPlayer) player, blockPos, blockState, itemStack, lv);
                }
            } else {
                lv = ItemHelper.of(itemStack).getEnchantmentFeature().getEnchantment(itemStack, EnchantmentRegistry.TUNNEL);
                if (lv > 0) {
                    if (!blockState.is(GrimoireBlockTags.TUNNEL_BLACKLIST) && itemStack.isCorrectToolForDrops(blockState)) {
                        EnchantmentRegistry.TUNNEL.trigger((ServerPlayer) player, blockPos, blockState, itemStack, lv);
                    }
                }
            }
        }
    }
}
