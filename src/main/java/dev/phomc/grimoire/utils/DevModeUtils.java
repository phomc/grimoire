package dev.phomc.grimoire.utils;

import dev.phomc.grimoire.enchantment.effect.EffectStage;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DevModeUtils {
    public static boolean isDevModeEnabled() {
        return Objects.equals(System.getenv("GRIMOIRE_DEV"), "yes");
    }

    public static void runInDev(Runnable runnable) {
        if (isDevModeEnabled()) runnable.run();
    }

    public static void onEffectEnchantInit(ResourceLocation identifier, String type, MobEffect effect, EffectStage[] effectStages) {
        if (!isDevModeEnabled()) return;

        List<String> matrix = new ArrayList<>();

        for (EffectStage effectStage : effectStages) {
            matrix.add(String.valueOf(effectStage.chance()));
            matrix.add(String.valueOf(effectStage.amplifier()));
            matrix.add(String.valueOf(effectStage.duration()));
        }
        System.out.printf(
                "EnchantmentList.set(\"%s|%s\", { effect: \"%s\", params: [%s] })%n",
                type, identifier, effect.getDisplayName().getString(), String.join(",", matrix));
    }
}
