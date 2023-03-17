package dev.phomc.grimoire.item;

import dev.phomc.grimoire.item.features.Feature;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public interface ItemHelper {
    static ItemHelper of(ItemStack itemStack) {
        if (itemStack == null) throw new IllegalArgumentException("item is null");
        return (ItemHelper) (Object) itemStack;
    }

    @Nullable
    <T extends Feature> T getFeature(ItemFeature feature);

    @NotNull
    <T extends Feature> T getOrCreateFeature(ItemFeature feature);

    // feature getter chaining, then finally saveChanges
    <T extends Feature> ItemHelper requestFeature(ItemFeature feature, Consumer<T> consumer);

    // single feature getter + saveChanges
    <T extends Feature> ItemHelper requestFeatureAndSave(ItemFeature feature, Consumer<T> consumer);

    void setItemName(MutableComponent component);

    void updateDisplay();

    void saveChanges();
}
