package dev.phomc.grimoire.item.custom;

import eu.pb4.polymer.core.api.item.PolymerItem;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;

public class ParchmentItem extends Item implements PolymerItem {
    public ParchmentItem(Properties properties) {
        super(properties);
    }

    @Override
    public Item getPolymerItem(ItemStack itemStack, @Nullable ServerPlayer player) {
        return Items.PAPER;
    }
}
