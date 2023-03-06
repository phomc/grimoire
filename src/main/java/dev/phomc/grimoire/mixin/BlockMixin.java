package dev.phomc.grimoire.mixin;

import dev.phomc.grimoire.enchantment.EnchantmentRegistry;
import dev.phomc.grimoire.enchantment.EnchantmentTarget;
import dev.phomc.grimoire.item.GrimoireItem;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(Block.class)
public class BlockMixin {
    @Inject(
            method = "getDrops(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/entity/BlockEntity;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/item/ItemStack;)Ljava/util/List;",
            at = @At("RETURN"),
            cancellable = true
    )
    private static void getDrops(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos,
                                 @Nullable BlockEntity blockEntity, @Nullable Entity entity, ItemStack itemStack,
                                 CallbackInfoReturnable<List<ItemStack>> cir) {
        if(itemStack == null || itemStack.isEmpty()) return;
        if (EnchantmentTarget.PICKAXE.test(itemStack.getItem())) {
            byte lv = GrimoireItem.of(itemStack).getEnchantmentFeature().getEnchantment(EnchantmentRegistry.FORGE);
            if (lv > 0 && EnchantmentRegistry.FORGE.shouldSmelt(lv)) {
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
}
