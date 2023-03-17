package dev.phomc.grimoire.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.phomc.grimoire.command.args.GemstoneArgument;
import dev.phomc.grimoire.command.enchant.EnchantAddCommand;
import dev.phomc.grimoire.command.enchant.EnchantRemoveAllCommand;
import dev.phomc.grimoire.command.enchant.EnchantRemoveCommand;
import dev.phomc.grimoire.command.item.GemstoneGiveCommand;
import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraft.resources.ResourceLocation;

import static net.minecraft.commands.Commands.literal;

public class CommandRegistry {
    public static void registerAll(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context, Commands.CommandSelection selection) {
        ArgumentTypeRegistry.registerArgumentType(
                new ResourceLocation("grimoire", "gemstone"),
                GemstoneArgument.class,
                SingletonArgumentInfo.contextFree(GemstoneArgument::create)
        );

        LiteralArgumentBuilder<CommandSourceStack> builder = literal("grimoire");
        builder.then(route("enchant", new EnchantAddCommand()));
        builder.then(route("disenchant", new EnchantRemoveCommand()));
        builder.then(route("disenchantall", new EnchantRemoveAllCommand()));
        builder.then(route("give", builder1 -> {
            builder1.then(route("gemstone", new GemstoneGiveCommand()));
        }));
        dispatcher.register(builder);
    }

    private static LiteralArgumentBuilder<CommandSourceStack> route(String path, SubCommand subCommand) {
        LiteralArgumentBuilder<CommandSourceStack> builder = literal(path);
        subCommand.register(builder);
        return builder;
    }
}
