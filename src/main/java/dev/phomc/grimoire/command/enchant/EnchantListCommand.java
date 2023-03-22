package dev.phomc.grimoire.command.enchant;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.phomc.grimoire.command.SubCommand;
import dev.phomc.grimoire.gui.EnchantmentGUI;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class EnchantListCommand implements SubCommand {
    @Override
    public void register(LiteralArgumentBuilder<CommandSourceStack> builder) {
        builder.requires(Permissions.require("grimoire.enchant.list", Commands.LEVEL_GAMEMASTERS))
                .executes(context -> {
                    return new EnchantmentGUI(context.getSource().getPlayerOrException(), 0, (gui, enc, type, action) -> {

                    }).open() ? 1 : 0;
                });
    }
}
