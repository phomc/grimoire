package dev.phomc.grimoire.gui;

import com.mojang.authlib.GameProfile;
import eu.pb4.sgui.api.elements.GuiElementBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.Items;

import java.util.UUID;

public class GuiElements {
    public static Component getStateSymbol(boolean state) {
        return state ? Component.translatable("grimoire.gui.element.tick").withStyle(ChatFormatting.GREEN) :
                Component.translatable("grimoire.gui.element.cross").withStyle(ChatFormatting.RED);
    }

    public static GuiElementBuilder createPreviousPageButton(MinecraftServer server) {
        return new GuiElementBuilder(Items.PLAYER_HEAD)
                .setSkullOwner(new GameProfile(UUID.fromString("a68f0b64-8d14-4000-a95f-4b9ba14f8df9"), "MHF_ArrowLeft"), server)
                .setName(Component.translatable("grimoire.gui.element.prev_page"));
    }

    public static GuiElementBuilder createNextPageButton(MinecraftServer server) {
        return new GuiElementBuilder(Items.PLAYER_HEAD)
                .setSkullOwner(new GameProfile(UUID.fromString("50c8510b-5ea0-4d60-be9a-7d542d6cd156"), "MHF_ArrowRight"), server)
                .setName(Component.translatable("grimoire.gui.element.next_page"));
    }

    public static GuiElementBuilder createCloseButton() {
        return new GuiElementBuilder(Items.BARRIER).setName(Component.translatable("grimoire.gui.element.close"));
    }
}
