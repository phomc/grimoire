package dev.phomc.grimoire.item.features;

import net.minecraft.network.chat.Component;

import java.util.List;

public interface Displayable {
    void displayLore(List<Component> lines);

    void hideLore(List<Component> lines);
}
