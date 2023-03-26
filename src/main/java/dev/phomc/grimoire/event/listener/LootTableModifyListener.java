package dev.phomc.grimoire.event.listener;

import dev.phomc.grimoire.loot.LootModifier;
import dev.phomc.grimoire.loot.LootRegistry;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.fabricmc.fabric.api.loot.v2.LootTableSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTables;

public class LootTableModifyListener implements LootTableEvents.Modify {
    public LootTableModifyListener() {
        LootTableEvents.MODIFY.register(this);
    }

    @Override
    public void modifyLootTable(ResourceManager resourceManager, LootTables lootManager, ResourceLocation id, LootTable.Builder tableBuilder, LootTableSource source) {
        if (source.isBuiltin()) {
            for (LootModifier lootModifier : LootRegistry.MODIFIERS.get(id)) {
                lootModifier.execute(tableBuilder);
            }
        }
    }
}
