package dev.phomc.grimoire.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

import java.util.function.Predicate;

public interface SubCommand {
    void register(LiteralArgumentBuilder<CommandSourceStack> builder);

    Predicate<CommandSourceStack> forPlayer = CommandSourceStack::isPlayer;

    Predicate<CommandSourceStack> forStaff = (commandSourceStack) -> {
        return commandSourceStack.hasPermission(Commands.LEVEL_GAMEMASTERS);
    };
}
