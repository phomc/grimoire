package dev.phomc.grimoire;

import dev.phomc.grimoire.command.CommandRegistry;
import dev.phomc.grimoire.enchantment.EnchantmentRegistry;
import dev.phomc.grimoire.listener.AttackEntityListener;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Grimoire implements DedicatedServerModInitializer {
    public static Logger LOGGER = LoggerFactory.getLogger(Grimoire.class);

    @Override
    public void onInitializeServer() {
        EnchantmentRegistry.init();
        CommandRegistrationCallback.EVENT.register(CommandRegistry::registerAll);
        AttackEntityCallback.EVENT.register(new AttackEntityListener());
    }
}
