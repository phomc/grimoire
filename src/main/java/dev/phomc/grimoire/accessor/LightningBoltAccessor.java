package dev.phomc.grimoire.accessor;

import net.minecraft.server.level.ServerPlayer;

public interface LightningBoltAccessor {
    ServerPlayer getOwner();

    void setOwner(ServerPlayer owner);
}
