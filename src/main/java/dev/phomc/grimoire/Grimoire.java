package dev.phomc.grimoire;

import com.mojang.brigadier.CommandDispatcher;
import dev.phomc.grimoire.command.CommandRegistry;
import dev.phomc.grimoire.enchantment.EnchantmentRegistry;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Grimoire implements DedicatedServerModInitializer {
    public static Logger LOGGER = LoggerFactory.getLogger(Grimoire.class);

    @Override
    public void onInitializeServer() {
        EnchantmentRegistry.init();
        CommandRegistrationCallback.EVENT.register(CommandRegistry::registerAll);
    }
}
