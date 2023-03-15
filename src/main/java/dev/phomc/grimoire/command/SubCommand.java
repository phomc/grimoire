package dev.phomc.grimoire.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;

public interface SubCommand {
    void register(LiteralArgumentBuilder<CommandSourceStack> builder);
}
