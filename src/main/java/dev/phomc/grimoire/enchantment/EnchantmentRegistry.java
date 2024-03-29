package dev.phomc.grimoire.enchantment;

import com.google.common.base.CaseFormat;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import dev.phomc.grimoire.Grimoire;
import dev.phomc.grimoire.enchantment.armor.AftershockEnchantment;
import dev.phomc.grimoire.enchantment.armor.AntidoteEnchantment;
import dev.phomc.grimoire.enchantment.armor.RefillEnchantment;
import dev.phomc.grimoire.enchantment.melee.ThunderEnchantment;
import dev.phomc.grimoire.enchantment.effect.passive.DecayEnchantment;
import dev.phomc.grimoire.enchantment.effect.passive.PetrifiedEnchantment;
import dev.phomc.grimoire.enchantment.effect.passive.VenomEnchantment;
import dev.phomc.grimoire.enchantment.effect.proactive.*;
import dev.phomc.grimoire.enchantment.melee.ColorShuffleEnchantment;
import dev.phomc.grimoire.enchantment.melee.DashEnchantment;
import dev.phomc.grimoire.enchantment.melee.VampireEnchantment;
import dev.phomc.grimoire.enchantment.ranged.*;
import dev.phomc.grimoire.enchantment.tool.DiggerEnchantment;
import dev.phomc.grimoire.enchantment.tool.SmeltingEnchantment;
import dev.phomc.grimoire.enchantment.tool.TunnelEnchantment;
import dev.phomc.grimoire.utils.DevModeUtils;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EnchantmentRegistry {
    public static Map<ResourceLocation, GrimoireEnchantment> ALL = new LinkedHashMap<>(); // preserve order

    public static CompatibilityGraph COMPATIBILITY_GRAPH;
    public static SmeltingEnchantment SMELTING;
    public static DiggerEnchantment DIGGER;
    public static TunnelEnchantment TUNNEL;
    public static RiftEnchantment RIFT;
    public static ExplosiveEnchantment EXPLOSIVE;
    public static SkybreakerEnchantment SKYBREAKER;
    public static ExchangeEnchantment EXCHANGE;
    public static TelekinesisEnchantment TELEKINESIS;
    public static ArrowRainEnchantment ARROW_RAIN;

    public static void init() {
        // melee
        registerEnchant(ColorShuffleEnchantment.class);
        registerEnchant(VampireEnchantment.class);
        registerEnchant(DashEnchantment.class);
        registerEnchant(ThunderEnchantment.class);

        // ranged
        registerEnchant(RiftEnchantment.class);
        registerEnchant(ExplosiveEnchantment.class);
        registerEnchant(ExchangeEnchantment.class);
        registerEnchant(TelekinesisEnchantment.class);
        registerEnchant(ArrowRainEnchantment.class);
        registerEnchant(SkybreakerEnchantment.class);

        // armor
        registerEnchant(AntidoteEnchantment.class);
        registerEnchant(RefillEnchantment.class); // helmet
        registerEnchant(AftershockEnchantment.class); // boots

        // tool
        registerEnchant(SmeltingEnchantment.class); // digger
        registerEnchant(DiggerEnchantment.class); // pickaxe
        registerEnchant(TunnelEnchantment.class); // pickaxe, shovel

        // active effect group (melee/ranged)
        registerEnchant(WitherEnchantment.class);
        registerEnchant(PoisonEnchantment.class);
        registerEnchant(NauseaEnchantment.class);
        registerEnchant(FrozenEnchantment.class);
        registerEnchant(BlindnessEnchantment.class);
        registerEnchant(LevitationEnchantment.class);

        // passive effect group (armor)
        registerEnchant(DecayEnchantment.class);
        registerEnchant(PetrifiedEnchantment.class);
        registerEnchant(VenomEnchantment.class);

        COMPATIBILITY_GRAPH = new CompatibilityGraph();
        reportRegistration();
    }

    private static void registerEnchant(Class<? extends GrimoireEnchantment> clazz) {
        try {
            String id = clazz.getSimpleName();
            if (!id.matches("\\w{3,}Enchantment")) throw new RuntimeException("Invalid Enchantment class name");
            id = id.substring(0, id.length() - "Enchantment".length());
            ResourceLocation identifier = new ResourceLocation("grimoire", CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, id));
            GrimoireEnchantment instance = clazz.getDeclaredConstructor(ResourceLocation.class).newInstance(identifier);

            if (instance.getMaxLevel() > Byte.MAX_VALUE) {
                Grimoire.LOGGER.warn("Enchantment '{}' has excessive maximum level", id);
            }

            try {
                EnchantmentRegistry.class.getDeclaredField(CaseFormat.UPPER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, id)).set(null, instance);
            } catch (IllegalAccessException | NoSuchFieldException e) {
                //Grimoire.LOGGER.warn("No public field specified for enchantment '{}' ", id);
            }

            ALL.put(identifier, instance);
            Registry.register(BuiltInRegistries.ENCHANTMENT, identifier, instance);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private static void reportRegistration() {
        DevModeUtils.runInDev(() -> {
            Multimap<Enchantment.Rarity, ResourceLocation> map = ArrayListMultimap.create();
            for (Map.Entry<ResourceKey<Enchantment>, Enchantment> entry : BuiltInRegistries.ENCHANTMENT.entrySet()) {
                map.put(entry.getValue().getRarity(), entry.getKey().location());
            }
            for (Enchantment.Rarity entry : map.keySet()) {
                List<String> collection = map.get(entry).stream().map(ResourceLocation::toString).sorted().toList();
                Grimoire.LOGGER.info("{} {} enchantments: {}", collection.size(), entry, String.join(", ", collection));
            }

            for (int i = 1; i <= 7; i++) {
                for (int j = 0; j <= 3; j++) {
                    Grimoire.LOGGER.info("Lv 1 to {}, rarity diff {} then weights = {}", i, j, Arrays.stream(GrimoireEnchantment.getLevelWeights(1, i, j))
                            .mapToObj(value -> String.format("%.2f", value))
                            .collect(Collectors.joining(", ")));
                }
            }

            for (GrimoireEnchantment e : ALL.values()) {
                if (e.getProperties().isEmpty()) continue;
                Grimoire.LOGGER.info(
                        "{} has {} properties: {}",
                        e.getIdentifier(),
                        e.getProperties().size(),
                        e.getProperties().entrySet().stream()
                                .map(x -> String.format("%s (%s)", x.getKey(), x.getValue().type().getSimpleName()))
                                .collect(Collectors.joining(", "))
                );
            }
        });
    }
}
