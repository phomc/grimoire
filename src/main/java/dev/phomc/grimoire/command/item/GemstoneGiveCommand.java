package dev.phomc.grimoire.command.item;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.phomc.grimoire.command.CommandErrors;
import dev.phomc.grimoire.command.SubCommand;
import dev.phomc.grimoire.command.Suggestions;
import dev.phomc.grimoire.item.ItemRegistry;
import dev.phomc.grimoire.item.gemstone.Gemstone;
import dev.phomc.grimoire.utils.InventoryUtils;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.EnumUtils;

import java.util.Collection;
import java.util.Collections;

public class GemstoneGiveCommand implements SubCommand {
    @Override
    public void register(LiteralArgumentBuilder<CommandSourceStack> builder) {
        builder.requires(Permissions.require("grimoire.item.give.gemstone", Commands.LEVEL_GAMEMASTERS))
                .then(
                        Commands.argument("type", StringArgumentType.word())
                                .suggests(Suggestions.ALL_GEMSTONE_SUGGESTION)
                                .executes(context -> give(context, 1, null))
                                .then(
                                        Commands.argument("amount", IntegerArgumentType.integer(1, Short.MAX_VALUE))
                                                .executes(context -> give(
                                                        context,
                                                        IntegerArgumentType.getInteger(context, "amount"),
                                                        null)
                                                ).then(
                                                        Commands.argument("targets", EntityArgument.players())
                                                                .executes(context -> give(
                                                                        context,
                                                                        IntegerArgumentType.getInteger(context, "amount"),
                                                                        EntityArgument.getPlayers(context, "targets")
                                                                ))
                                                )
                                )
                );
    }

    public int give(CommandContext<CommandSourceStack> context, int amount, Collection<ServerPlayer> targets) throws CommandSyntaxException {
        String type = context.getArgument("type", String.class);
        Gemstone gemstone = EnumUtils.getEnumIgnoreCase(Gemstone.class, type);
        if (gemstone == null) {
            throw CommandErrors.ERROR_INVALID_GEMSTONE.create(type);
        }
        ServerPlayer executor = context.getSource().getPlayer();
        if (executor == null) throw new RuntimeException();
        if (targets == null || targets.isEmpty()) targets = Collections.singletonList(executor);
        ItemStack itemStack = ItemRegistry.GEMSTONE.create(gemstone);
        for (ServerPlayer target : targets) {
            InventoryUtils.give(target, itemStack, amount);
        }
        executor.displayClientMessage(Component.translatable("grimoire.command.give.success", itemStack.getDisplayName(), targets.size()), false);
        return targets.size();
    }
}
