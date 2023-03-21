package dev.phomc.grimoire.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.phomc.grimoire.command.enchant.EnchantAddCommand;
import dev.phomc.grimoire.command.enchant.EnchantRemoveAllCommand;
import dev.phomc.grimoire.command.enchant.EnchantRemoveCommand;
import dev.phomc.grimoire.command.gui.ScriptoriumOpenCommand;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

import static net.minecraft.commands.Commands.literal;

public class CommandRegistry {
    public static void init() {
        CommandRegistrationCallback.EVENT.register(CommandRegistry::registerAll);
    }

    public static void registerAll(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context, Commands.CommandSelection selection) {
        LiteralArgumentBuilder<CommandSourceStack> builder = literal("grimoire");
        builder.then(route("enchant", new EnchantAddCommand()));
        builder.then(route("disenchant", new EnchantRemoveCommand()));
        builder.then(route("disenchantall", new EnchantRemoveAllCommand()));
        dispatcher.register(builder);
        dispatcher.register(route("scriptorium", new ScriptoriumOpenCommand()));
    }

    private static LiteralArgumentBuilder<CommandSourceStack> route(String path, SubCommand subCommand) {
        LiteralArgumentBuilder<CommandSourceStack> builder = literal(path);
        subCommand.register(builder);
        return builder;
    }
}
