package dev.phomc.grimoire.gui;

import dev.phomc.grimoire.item.ItemRegistry;
import eu.pb4.sgui.api.elements.GuiElementBuilder;
import eu.pb4.sgui.api.gui.SimpleGui;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Items;

public class ScriptoriumGUI extends SimpleGui {
    public ScriptoriumGUI(ServerPlayer player) {
        super(MenuType.GENERIC_3x3, player, false);

        for (int i : new int[]{3, 4, 5, 6, 8}) {
            setSlot(i, new GuiElementBuilder(Items.BARRIER, 1).setCallback((index, type, action) -> {

            }));
        }

        setSlot(7, new GuiElementBuilder(ItemRegistry.QUILL, 1)
                .setName(Component.translatable("grimoire.gui.scriptorium.write"))
                .glow()
                .setCallback((index, type, action) -> {

                }));

        setTitle(Component.translatable("grimoire.gui.scriptorium.title"));
    }
}
