package dev.phomc.grimoire.gui;

import dev.phomc.grimoire.item.ItemFeature;
import dev.phomc.grimoire.item.ItemHelper;
import dev.phomc.grimoire.item.ItemRegistry;
import dev.phomc.grimoire.item.custom.InkwellItem;
import dev.phomc.grimoire.item.custom.ParchmentItem;
import dev.phomc.grimoire.item.custom.QuillItem;
import dev.phomc.grimoire.item.features.GemstoneElementFeature;
import dev.phomc.grimoire.utils.InventoryUtils;
import eu.pb4.sgui.api.elements.GuiElementBuilder;
import eu.pb4.sgui.api.elements.GuiElementInterface;
import eu.pb4.sgui.api.gui.SimpleGui;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class ScriptoriumGUI extends SimpleGui {
    private static final int[] INGREDIENT_SLOTS = new int[]{0, 1, 2};
    private static final int[] BACKGROUND_SLOTS = new int[]{3, 4, 5, 6, 8};
    private static final int TRIGGER_SLOT = 7;

    public ScriptoriumGUI(ServerPlayer player) {
        super(MenuType.GENERIC_3x3, player, false);

        for (int i : INGREDIENT_SLOTS) {
            addMutableSlot(i);
        }

        for (int i : BACKGROUND_SLOTS) {
            setSlot(i, new GuiElementBuilder(Items.BARRIER, 1).setCallback((index, type, action) -> {

            }));
        }

        setSlot(TRIGGER_SLOT, new GuiElementBuilder(ItemRegistry.QUILL, 1)
                .setName(Component.translatable("grimoire.gui.scriptorium.write"))
                .glow()
                .setCallback((index, type, action) -> {
                    handleWriting();
                }));

        setTitle(Component.translatable("grimoire.gui.scriptorium.title"));
    }

    private void addMutableSlot(int i) {
        setSlot(i, new GuiElementBuilder(Items.AIR).setCallback((index, type, action) -> {
            if (type.isLeft) {
                GuiElementInterface slot = getSlot(i);
                ItemStack current = slot.getItemStack();
                ItemStack cursor = player.containerMenu.getCarried();
                setSlot(i, cursor, slot.getGuiCallback());
                player.containerMenu.setCarried(current);
            }
        }));
    }

    @Override
    public void onClose() {
        for (int i : INGREDIENT_SLOTS) {
            InventoryUtils.give(player, getSlot(i).getItemStack());
        }
    }

    public GuiElementInterface scanIngredient(Predicate<Item> predicate) {
        for (int i : INGREDIENT_SLOTS) {
            GuiElementInterface slot = getSlot(i);
            if (predicate.test(slot.getItemStack().getItem())) {
                return slot;
            }
        }
        return null;
    }

    private void handleWriting() {
        GuiElementInterface parchmentSlot = scanIngredient(item -> item instanceof ParchmentItem);
        GuiElementInterface inkwellSlot = scanIngredient(item -> item instanceof InkwellItem);
        GuiElementInterface quillSlot = scanIngredient(item -> item instanceof QuillItem);
        if (parchmentSlot == null || inkwellSlot == null || quillSlot == null) {
            return;
        }

        ItemStack out = new ItemStack(ItemRegistry.UNIDENTIFIED_GRIMOIRE);
        ItemHelper.of(out).requestFeatureAndSave(ItemFeature.GEMSTONE_ELEMENT, (Consumer<GemstoneElementFeature>) feature -> {
            feature.element = ((InkwellItem) inkwellSlot.getItemStack().getItem()).getType();
        });
        InventoryUtils.give(player, out);

        for (int i : INGREDIENT_SLOTS) {
            GuiElementInterface slot = getSlot(i);
            ItemStack item = slot.getItemStack();
            if (item.getCount() == 1) {
                setSlot(i, new ItemStack(Items.AIR), slot.getGuiCallback());
            } else {
                setSlot(i, item.copyWithCount(item.getCount() - 1), slot.getGuiCallback());
            }
        }

        player.level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.BOOK_PAGE_TURN, SoundSource.PLAYERS, 3.0f, 1.0f);
    }
}
