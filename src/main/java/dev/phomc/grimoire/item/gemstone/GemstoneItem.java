package dev.phomc.grimoire.item.gemstone;

import eu.pb4.polymer.core.api.item.PolymerItem;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GemstoneItem extends Item implements PolymerItem {
    private final Gemstone type;

    public GemstoneItem(Gemstone type, Properties properties) {
        super(properties);
        this.type = type;
    }

    @NotNull
    public Gemstone getType() {
        return type;
    }

    @Override
    public Item getPolymerItem(ItemStack itemStack, @Nullable ServerPlayer player) {
        return type.getBackend();
    }
}
