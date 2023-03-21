package dev.phomc.grimoire.utils;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class InventoryUtils {
    public static void give(ServerPlayer player, @NotNull ItemStack itemStack) {
        give(player, itemStack, 1);
    }

    public static void give(ServerPlayer player, @NotNull ItemStack itemStack, int times) {
        int remain = add(player.getInventory(), itemStack, times);
        player.containerMenu.broadcastChanges();

        while (remain > 0) {
            int delta = itemStack.isStackable() ? Math.min(itemStack.getMaxStackSize(), remain) : 1;
            ItemEntity itemEntity = player.drop(itemStack.copyWithCount(delta), false);
            Objects.requireNonNull(itemEntity).setNoPickUpDelay();
            itemEntity.setTarget(player.getUUID());
            player.level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.2f, ((player.getRandom().nextFloat() - player.getRandom().nextFloat()) * 0.7f + 1.0f) * 2.0f);
            remain -= delta;
        }
    }

    public static int add(Inventory inv, @Nullable ItemStack itemStack, int times) {
        if (itemStack == null || itemStack.isEmpty()) return 0;
        int amount  = times * itemStack.getCount();

        for (int i = 0; i < inv.items.size() && amount > 0; i++) {
            ItemStack it = inv.items.get(i);
            if (it.isEmpty()) {
                int delta = itemStack.isStackable() ? Math.min(itemStack.getMaxStackSize(), amount) : 1;
                inv.items.set(i, itemStack.copyWithCount(delta));
                amount -= delta;
                continue;
            }
            if (ItemStack.isSameItemSameTags(it, itemStack) && it.isStackable() && itemStack.isStackable()) {
                int delta = Math.min(it.getMaxStackSize() - it.getCount(), amount);
                if (delta > 0) {
                    it.setCount(it.getCount() + delta);
                    amount -= delta;
                }
            }
        }

        return amount;
    }
}
