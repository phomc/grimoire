package dev.phomc.grimoire.item.incanting;

import dev.phomc.grimoire.item.GrimoireItem;
import dev.phomc.grimoire.item.ItemFeature;
import dev.phomc.grimoire.item.ItemHelper;
import dev.phomc.grimoire.item.features.CustomItemFeature;
import dev.phomc.grimoire.item.gemstone.Gemstone;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Consumer;

public class InkwellItem extends GrimoireItem {
    private static final Map<Gemstone, Potion> gemstonePotionMap = new EnumMap<>(Gemstone.class);

    static {
        gemstonePotionMap.put(Gemstone.MUSGRAVITE, Potions.WEAKNESS);
        gemstonePotionMap.put(Gemstone.JADE, Potions.NIGHT_VISION);
        gemstonePotionMap.put(Gemstone.SAPPHIRE, Potions.SWIFTNESS);
        gemstonePotionMap.put(Gemstone.TOPAZ, Potions.STRENGTH);
    }

    public InkwellItem(ResourceLocation identifier) {
        super(identifier);
    }

    @Override
    public void onUse() {

    }

    @Override
    public ItemStack getIcon() {
        return create(Gemstone.MUSGRAVITE);
    }

    @NotNull
    public ItemStack create(@NotNull Gemstone type) {
        Potion p = gemstonePotionMap.get(type);
        if (p == null) {
            throw new UnsupportedOperationException(type + " is unsupported");
        }
        ItemStack itemStack = new ItemStack(Items.LINGERING_POTION, 1);
        ItemHelper.of(itemStack).requestFeatureAndSave(ItemFeature.CUSTOM_ITEM, new Consumer<CustomItemFeature>() {
            @Override
            public void accept(CustomItemFeature feature) {
                feature.setItemId(getIdentifier());
                feature.getOrCreateData().putString("type", type.name());
            }
        }).setItemName(Component.translatable("grimoire.inkwell." + type.getId()));
        return itemStack;
    }
}
