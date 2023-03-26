package dev.phomc.grimoire.loot;

import net.minecraft.world.level.storage.loot.LootTable;

public interface LootModifier {
    void execute(LootTable.Builder tableBuilder);
}
