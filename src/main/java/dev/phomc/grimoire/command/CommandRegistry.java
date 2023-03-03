package dev.phomc.grimoire.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.phomc.grimoire.command.enchant.EnchantAddCommand;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

import java.util.function.Predicate;

import static net.minecraft.commands.Commands.*;

public class CommandRegistry {
    public static void registerAll(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context, Commands.CommandSelection selection) {
        LiteralArgumentBuilder<CommandSourceStack> builder = literal("grimoire");
        builder.then(
                literal("enchant").then(
                        route("add", new EnchantAddCommand())
                )
        );
        dispatcher.register(builder);
    }

    private static LiteralArgumentBuilder<CommandSourceStack> route(String path, SubCommand subCommand) {
        LiteralArgumentBuilder<CommandSourceStack> builder = literal(path);
        subCommand.register(builder);
        return builder;
    }
}
