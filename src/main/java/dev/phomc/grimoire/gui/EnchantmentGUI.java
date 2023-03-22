package dev.phomc.grimoire.gui;

import dev.phomc.grimoire.enchantment.EnchantmentRegistry;
import dev.phomc.grimoire.enchantment.GrimoireEnchantment;
import eu.pb4.sgui.api.ClickType;
import eu.pb4.sgui.api.elements.GuiElementBuilder;
import eu.pb4.sgui.api.gui.SimpleGui;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.Arrays;

public class EnchantmentGUI extends SimpleGui {
    public interface Callback {
        void onSelectEnchantment(EnchantmentGUI gui, GrimoireEnchantment enc, ClickType type, net.minecraft.world.inventory.ClickType action);
    }

    private static final int LIST_SIZE = 27;
    private static final Item CONTROL_BACKGROUND = Items.LIME_STAINED_GLASS_PANE;

    private static GrimoireEnchantment[] getEnchantments(int page) {
        GrimoireEnchantment[] arr = EnchantmentRegistry.ALL.values().toArray(GrimoireEnchantment[]::new);
        int start = page * LIST_SIZE; // inclusive
        if (start >= arr.length) {
            return new GrimoireEnchantment[0];
        }
        int bound = Math.min(arr.length, start + LIST_SIZE); // exclusive bound
        return Arrays.copyOfRange(arr, start, bound);
    }

    private static int getMaxPage() {
        return (int) Math.floor(EnchantmentRegistry.ALL.values().size() / (double) LIST_SIZE);
    }

    private int page;
    private final Callback callback;

    public EnchantmentGUI(ServerPlayer player, int page, Callback callback) {
        super(MenuType.GENERIC_9x4, player, false);
        this.page = page;
        this.callback = callback;
        render();
    }

    private void render() {
        renderList();

        for (int i = LIST_SIZE; i < LIST_SIZE + 9; i++) {
            if (i == LIST_SIZE + 2) {
                if (getPage() == 0) {
                    setSlot(i, new GuiElementBuilder(CONTROL_BACKGROUND)
                            .setName(Component.empty())
                            .setCallback((index, type, action) -> {

                            }));
                } else {
                    setSlot(i, GuiElements.createPreviousPageButton(player.server)
                            .setCallback((index, type, action) -> {
                                setPage(getPage() - 1);
                                renderList();
                            }));
                }
            } else if (i == LIST_SIZE + 4) {
                setSlot(i, GuiElements.createCloseButton()
                        .setCallback((index, type, action) -> {
                            close();
                        }));
            } else if (i == LIST_SIZE + 6) {
                if (getPage() == getMaxPage()) {
                    setSlot(i, new GuiElementBuilder(CONTROL_BACKGROUND)
                            .setName(Component.empty())
                            .setCallback((index, type, action) -> {

                            }));
                } else {
                    setSlot(i, GuiElements.createNextPageButton(player.server)
                            .setCallback((index, type, action) -> {
                                setPage(getPage() + 1);
                                renderList();
                            }));
                }
            } else {
                setSlot(i, new GuiElementBuilder(CONTROL_BACKGROUND)
                        .setName(Component.empty())
                        .setCallback((index, type, action) -> {

                        }));
            }
        }

        setTitle(Component.translatable("grimoire.gui.enchantment.title"));
    }

    private void renderList() {
        GrimoireEnchantment[] arr = getEnchantments(page);

        for (int i = 0; i < LIST_SIZE; i++) {
            if (i < arr.length) {
                int finalI = i;
                setSlot(i, new GuiElementBuilder(Items.ENCHANTED_BOOK)
                        .setName(arr[i].getDisplayName())
                        .addLoreLine(Component.translatable("grimoire.gui.enchantment.info.max_level", arr[i].getMaxLevel()))
                        .addLoreLine(Component.translatable("grimoire.gui.enchantment.info.tradeable", GuiElements.getStateSymbol(arr[i].isTradeable())))
                        .addLoreLine(Component.translatable("grimoire.gui.enchantment.info.rarity", arr[i].getRarityDisplay()))
                        .setCallback((index, type, action) -> {
                            callback.onSelectEnchantment(this, arr[finalI], type, action);
                        }));
            } else {
                setSlot(i, new GuiElementBuilder(Items.GRAY_STAINED_GLASS_PANE)
                        .setName(Component.empty())
                        .setCallback((index, type, action) -> {

                        }));
            }
        }
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public Callback getCallback() {
        return callback;
    }
}
