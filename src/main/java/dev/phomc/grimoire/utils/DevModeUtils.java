package dev.phomc.grimoire.utils;

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

    public static void onEffectEnchantInit(ResourceLocation identifier, String type, MobEffect effect, int[] amplifiers, int[] duration, float[] chances) {
        if (!isDevModeEnabled()) return;

        List<String> matrix = new ArrayList<>();

        for (int i = 0; i < amplifiers.length; i++) {
            matrix.add(String.valueOf(chances[i]));
            matrix.add(String.valueOf(amplifiers[i]));
            matrix.add(String.valueOf(duration[i]));
        }
        System.out.printf(
                "EnchantmentList.set(\"%s|%s\", { effect: \"%s\", params: [%s] })%n",
                type, identifier, effect.getDisplayName().getString(), String.join(",", matrix));
    }
}
