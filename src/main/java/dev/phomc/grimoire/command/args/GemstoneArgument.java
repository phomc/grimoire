package dev.phomc.grimoire.command.args;

import com.mojang.brigadier.context.CommandContext;
import dev.phomc.grimoire.item.gemstone.Gemstone;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.StringRepresentableArgument;

public class GemstoneArgument extends StringRepresentableArgument<Gemstone> {
    private GemstoneArgument() {
        super(Gemstone.CODEC, Gemstone::values);
    }

    public static StringRepresentableArgument<Gemstone> create() {
        return new GemstoneArgument();
    }

    public static Gemstone getGemstone(CommandContext<CommandSourceStack> commandContext, String string) {
        return commandContext.getArgument(string, Gemstone.class);
    }
}
