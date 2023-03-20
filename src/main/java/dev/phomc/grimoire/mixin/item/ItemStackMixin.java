package dev.phomc.grimoire.mixin.item;

import dev.phomc.grimoire.item.ItemFeature;
import dev.phomc.grimoire.item.ItemHelper;
import dev.phomc.grimoire.item.features.EnchantmentFeature;
import dev.phomc.grimoire.item.features.Feature;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Consumer;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements ItemHelper {
    @Shadow
    public abstract boolean isEmpty();

    @Shadow public abstract ItemStack setHoverName(@Nullable Component component);

    private final Map<ItemFeature, Feature> featureMap = new EnumMap<>(ItemFeature.class);

    private ItemStack self() {
        return (ItemStack) (Object) this;
    }

    // preserved
    @NotNull
    public EnchantmentFeature getEnchantmentFeature() {
        return getOrCreateFeature(ItemFeature.ENCHANTMENT);
    }

    @Override
    public <T extends Feature> @Nullable T getFeature(ItemFeature feature) {
        //noinspection unchecked
        return (T) featureMap.get(feature);
    }

    @Override
    public <T extends Feature> @NotNull T getOrCreateFeature(ItemFeature feature) {
        Feature f = featureMap.get(feature);
        if (f == null) {
            f = feature.create();
            featureMap.put(feature, f);
        }
        //noinspection unchecked
        return (T) f;
    }

    @Override
    public <T extends Feature> ItemHelper requestFeature(ItemFeature feature, Consumer<T> consumer) {
        consumer.accept(getOrCreateFeature(feature));
        return this;
    }

    @Override
    public <T extends Feature> ItemHelper requestFeatureAndSave(ItemFeature feature, Consumer<T> consumer) {
        consumer.accept(getOrCreateFeature(feature));
        saveChanges();
        return this;
    }

    public void setItemName(MutableComponent component) {
        setHoverName(component.withStyle(Style.EMPTY.withItalic(false)));
    }

    public void saveChanges() {
        if (isEmpty()) return;
        for (Feature f : featureMap.values()) {
            f.save(self());
        }
    }
}
