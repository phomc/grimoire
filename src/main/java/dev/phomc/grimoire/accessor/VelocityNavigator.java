package dev.phomc.grimoire.accessor;

import net.minecraft.world.phys.Vec3;

public abstract class VelocityNavigator {
    private final int ticks;
    private int tickCounter;
    private final Vec3 deltaMovement;

    private boolean cancelled;

    public VelocityNavigator(Vec3 movement, double speed) {
        speed = Math.max(0, Math.min(1, speed));
        this.ticks = (int) Math.ceil(1d / speed);
        this.deltaMovement = movement.multiply(speed, speed, speed);
    }

    public abstract void onTick(boolean lastTick);

    public Vec3 getDeltaMovement() {
        return deltaMovement;
    }

    public float getTicks() {
        return ticks;
    }

    public boolean nextTick() {
        return ++tickCounter < ticks;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
