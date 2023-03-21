package dev.phomc.grimoire.command.gui;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.phomc.grimoire.command.SubCommand;
import dev.phomc.grimoire.gui.ScriptoriumGUI;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class ScriptoriumOpenCommand implements SubCommand {
    @Override
    public void register(LiteralArgumentBuilder<CommandSourceStack> builder) {
        builder.requires(Permissions.require("grimoire.gui.scriptorium", Commands.LEVEL_GAMEMASTERS))
                .executes(context -> new ScriptoriumGUI(context.getSource().getPlayer()).open() ? 1 : 0);
    }
}
