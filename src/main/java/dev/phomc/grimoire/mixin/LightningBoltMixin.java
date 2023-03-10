package dev.phomc.grimoire.mixin;

import dev.phomc.grimoire.accessor.LightningBoltAccessor;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LightningBolt;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(LightningBolt.class)
public class LightningBoltMixin implements LightningBoltAccessor {
    @Unique
    private ServerPlayer owner;

    @Override
    public ServerPlayer getOwner() {
        return owner;
    }

    @Override
    public void setOwner(ServerPlayer owner) {
        this.owner = owner;
    }
}
