package dev.phomc.grimoire.item.custom;

import dev.phomc.grimoire.item.Gemstone;
import eu.pb4.polymer.core.api.item.PolymerItem;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class InkwellItem extends PotionItem implements PolymerItem {
    public static final Map<Gemstone, Potion> GEMSTONE_POTION_MAP = new EnumMap<>(Gemstone.class);

    static {
        GEMSTONE_POTION_MAP.put(Gemstone.MUSGRAVITE, Potions.WEAKNESS);
        GEMSTONE_POTION_MAP.put(Gemstone.JADE, Potions.NIGHT_VISION);
        GEMSTONE_POTION_MAP.put(Gemstone.SAPPHIRE, Potions.SWIFTNESS);
        GEMSTONE_POTION_MAP.put(Gemstone.TOPAZ, Potions.STRENGTH);

        for (Gemstone value : Gemstone.values()) {
            if (!GEMSTONE_POTION_MAP.containsKey(value)) {
                throw new RuntimeException("missing inkwell support from gemstone " + value.getId());
            }
        }
    }

    private final Gemstone type;

    public InkwellItem(Gemstone type, Item.Properties properties) {
        super(properties);
        this.type = type;
    }

    @NotNull
    public Gemstone getType() {
        return type;
    }

    @Override
    public Item getPolymerItem(ItemStack itemStack, @Nullable ServerPlayer player) {
        return Items.POTION;
    }

    @Override
    public ItemStack getPolymerItemStack(ItemStack itemStack, TooltipFlag context, @Nullable ServerPlayer player) {
        ItemStack out = PolymerItem.super.getPolymerItemStack(itemStack, context, player);
        PotionUtils.setPotion(out, GEMSTONE_POTION_MAP.get(type));
        out.hideTooltipPart(ItemStack.TooltipPart.ADDITIONAL);
        return out;
    }

    @Override
    public @NotNull String getDescriptionId(ItemStack itemStack) {
        return this.getDescriptionId();
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {

    }

    @Override
    public int getUseDuration(ItemStack itemStack) {
        return 0;
    }

    @Override
    public @NotNull UseAnim getUseAnimation(ItemStack itemStack) {
        return UseAnim.NONE;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        return InteractionResultHolder.pass(player.getItemInHand(interactionHand));
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext useOnContext) {
        return InteractionResult.PASS;
    }

    @Override
    public @NotNull ItemStack finishUsingItem(ItemStack itemStack, Level level, LivingEntity livingEntity) {
        return itemStack;
    }
}
