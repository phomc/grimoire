package dev.phomc.grimoire.item.gemstone;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

public enum Gemstone implements StringRepresentable {
    MUSGRAVITE(Items.NETHERITE_INGOT),
    JADE(Items.EMERALD),
    SAPPHIRE(Items.DIAMOND),
    TOPAZ(Items.GOLD_INGOT);

    public static final Codec<Gemstone> CODEC;

    static {
        CODEC = StringRepresentable.fromEnum(Gemstone::values);
    }

    private final Item backend;

    Gemstone(Item backend) {
        this.backend = backend;
    }

    @NotNull
    public Item getBackend() {
        return backend;
    }

    @Override
    public String getSerializedName() {
        return name().toLowerCase();
    }
}
