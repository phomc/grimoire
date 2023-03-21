package dev.phomc.grimoire.command;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.phomc.grimoire.enchantment.EnchantmentRegistry;
import dev.phomc.grimoire.enchantment.GrimoireEnchantment;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.resources.ResourceLocation;

public class CommandArgs {
    public static GrimoireEnchantment getEnchantment(CommandContext<CommandSourceStack> commandContext, String string) throws CommandSyntaxException {
        ResourceLocation resourceLocation = ResourceLocationArgument.getId(commandContext, string);
        GrimoireEnchantment enchantment = EnchantmentRegistry.ALL.get(resourceLocation);
        if (enchantment == null) {
            throw CommandErrors.ERROR_UNKNOWN_ENCHANTMENT.create(resourceLocation);
        }
        return enchantment;
    }
}
