package dev.phomc.grimoire.enchantment;

import com.google.common.base.CaseFormat;
import dev.phomc.grimoire.Grimoire;
import dev.phomc.grimoire.enchantment.armor.AntidoteEnchantment;
import dev.phomc.grimoire.enchantment.armor.RefillEnchantment;
import dev.phomc.grimoire.enchantment.dummy.DummyEnchantment;
import dev.phomc.grimoire.enchantment.effect.active.*;
import dev.phomc.grimoire.enchantment.effect.passive.DecayEnchantment;
import dev.phomc.grimoire.enchantment.effect.passive.PetrifiedEnchantment;
import dev.phomc.grimoire.enchantment.effect.passive.VenomEnchantment;
import dev.phomc.grimoire.enchantment.melee.ColorShuffleEnchantment;
import dev.phomc.grimoire.enchantment.melee.VampireEnchantment;
import dev.phomc.grimoire.enchantment.tool.DiggerEnchantment;
import dev.phomc.grimoire.enchantment.tool.ForgeEnchantment;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.Map;

public class EnchantmentRegistry {
    public static Map<ResourceLocation, GrimoireEnchantment> ALL = new LinkedHashMap<>(); // preserve order

    public static CompatibilityGraph COMPATIBILITY_GRAPH;
    public static Enchantment DUMMY;
    public static ForgeEnchantment FORGE;
    public static DiggerEnchantment DIGGER;

    public static void init() {
        Registry.register(BuiltInRegistries.ENCHANTMENT, new ResourceLocation("grimoire", "dummy"), DUMMY = new DummyEnchantment());

        // melee
        registerEnchant(ColorShuffleEnchantment.class);
        registerEnchant(VampireEnchantment.class);

        // armor
        registerEnchant(AntidoteEnchantment.class);
        registerEnchant(RefillEnchantment.class); // armor

        // tool
        registerEnchant(ForgeEnchantment.class); // pickaxe
        registerEnchant(DiggerEnchantment.class); // pickaxe

        // armor - effect (active)
        registerEnchant(WitherEnchantment.class);
        registerEnchant(PoisonEnchantment.class);
        registerEnchant(NauseaEnchantment.class);
        registerEnchant(FrozenEnchantment.class);
        registerEnchant(BlindnessEnchantment.class);
        registerEnchant(LevitationEnchantment.class);

        // armor - effect (passive)
        registerEnchant(DecayEnchantment.class);
        registerEnchant(PetrifiedEnchantment.class);
        registerEnchant(VenomEnchantment.class);

        COMPATIBILITY_GRAPH = new CompatibilityGraph();
    }

    private static GrimoireEnchantment registerEnchant(Class<? extends GrimoireEnchantment> clazz) {
        try {
            String id = clazz.getSimpleName();
            if (!id.matches("\\w{3,}Enchantment")) throw new RuntimeException("Invalid Enchantment class name");
            id = id.substring(0, id.length() - "Enchantment".length());
            ResourceLocation identifier = new ResourceLocation("grimoire", CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, id));
            GrimoireEnchantment instance = clazz.getDeclaredConstructor(ResourceLocation.class).newInstance(identifier);

            try {
                EnchantmentRegistry.class.getDeclaredField(CaseFormat.UPPER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, id)).set(null, instance);
            } catch (IllegalAccessException | NoSuchFieldException e) {
                Grimoire.LOGGER.warn("No public field specified for enchantment '{}' ", id);
            }

            ALL.put(identifier, instance);
            Grimoire.LOGGER.info("Enchantment '{}' registered", id);
            return instance;
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
