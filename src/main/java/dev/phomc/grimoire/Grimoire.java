package dev.phomc.grimoire;

import dev.phomc.grimoire.command.CommandRegistry;
import dev.phomc.grimoire.enchantment.EnchantmentRegistry;
import dev.phomc.grimoire.event.listener.LootTableModifyListener;
import dev.phomc.grimoire.event.listener.UseBlockListener;
import dev.phomc.grimoire.item.ItemRegistry;
import net.fabricmc.api.DedicatedServerModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Grimoire implements DedicatedServerModInitializer {
    public static Logger LOGGER = LoggerFactory.getLogger(Grimoire.class);

    @Override
    public void onInitializeServer() {
        EnchantmentRegistry.init();
        ItemRegistry.init();
        CommandRegistry.init();
        new UseBlockListener();
        new LootTableModifyListener();
    }
}
