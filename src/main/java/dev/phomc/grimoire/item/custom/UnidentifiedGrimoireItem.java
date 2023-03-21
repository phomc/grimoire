package dev.phomc.grimoire.item.custom;

import eu.pb4.polymer.core.api.item.PolymerItem;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class UnidentifiedGrimoireItem extends EnchantedBookItem implements PolymerItem {
    public UnidentifiedGrimoireItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public Item getPolymerItem(ItemStack itemStack, @Nullable ServerPlayer player) {
        return Items.ENCHANTED_BOOK;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {

    }
}
