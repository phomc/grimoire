package dev.phomc.grimoire.command.give;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.phomc.grimoire.command.SubCommand;
import dev.phomc.grimoire.command.Suggestions;
import dev.phomc.grimoire.item.Gemstone;
import dev.phomc.grimoire.item.ItemFeature;
import dev.phomc.grimoire.item.ItemHelper;
import dev.phomc.grimoire.item.ItemRegistry;
import dev.phomc.grimoire.item.features.GemstoneElementFeature;
import dev.phomc.grimoire.utils.InventoryUtils;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.EnumUtils;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class UnidentifiedGrimoireGiveCommand implements SubCommand {
    @Override
    public void register(LiteralArgumentBuilder<CommandSourceStack> builder) {
        builder.requires(Permissions.require("grimoire.give.unidentified_grimoire", Commands.LEVEL_GAMEMASTERS))
                .then(
                        Commands.argument("gemstone", StringArgumentType.word())
                                .suggests(Suggestions.ALL_GEMSTONE_SUGGESTION)
                                .executes(context -> {
                                    return give(context, 1, null);
                                })
                                .then(
                                        Commands.argument("amount", IntegerArgumentType.integer(1, 1000))
                                                .executes(context -> give(
                                                        context,
                                                        IntegerArgumentType.getInteger(context, "amount"),
                                                        null)
                                                ).then(
                                                        Commands.argument("target", EntityArgument.player())
                                                                .executes(context -> give(
                                                                        context,
                                                                        IntegerArgumentType.getInteger(context, "amount"),
                                                                        EntityArgument.getPlayer(context, "target")
                                                                ))
                                                )
                                )
                );
    }

    public int give(CommandContext<CommandSourceStack> context, int amount, @Nullable Player target) throws CommandSyntaxException {
        if (target == null) {
            target = context.getSource().getPlayerOrException();
        }
        ItemStack itemStack = new ItemStack(ItemRegistry.UNIDENTIFIED_GRIMOIRE);
        ItemHelper.of(itemStack).requestFeatureAndSave(ItemFeature.GEMSTONE_ELEMENT, (Consumer<GemstoneElementFeature>) feature -> {
            feature.element = EnumUtils.getEnumIgnoreCase(Gemstone.class, context.getArgument("gemstone", String.class));
        });
        InventoryUtils.give((ServerPlayer) target, itemStack, amount);
        return amount;
    }
}
