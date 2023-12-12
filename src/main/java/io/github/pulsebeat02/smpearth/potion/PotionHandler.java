package io.github.pulsebeat02.smpearth.potion;

import static net.minecraft.item.Items.*;
import static net.minecraft.potion.Potions.*;

import java.util.ArrayList;
import java.util.Collection;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public final class PotionHandler {

  private static @NotNull final Collection<PotionRecipe> RECIPES;
  public static @NotNull final Potion RESISTANCE;
  public static @NotNull final Potion LONG_RESISTANCE;
  public static @NotNull final Potion STRONG_RESISTANCE;
  public static @NotNull final Potion ABSORPTION;
  public static @NotNull final Potion LONG_ABSORPTION;
  public static @NotNull final Potion STRONG_ABSORPTION;
  public static @NotNull final Potion BLINDNESS;
  public static @NotNull final Potion LONG_BLINDNESS;
  public static @NotNull final Potion GLOWING;
  public static @NotNull final Potion LONG_GLOWING;
  public static @NotNull final Potion HEALTH_BOOST;
  public static @NotNull final Potion LONG_HEALTH_BOOST;
  public static @NotNull final Potion STRONG_HEALTH_BOOST;
  public static @NotNull final Potion LEVITATION;
  public static @NotNull final Potion LONG_LEVITATION;
  public static @NotNull final Potion STRONG_LEVITATION;
  public static @NotNull final Potion NAUSEA;
  public static @NotNull final Potion LONG_NAUSEA;
  public static @NotNull final Potion WITHER;
  public static @NotNull final Potion LONG_WITHER;
  public static @NotNull final Potion STRONG_WITHER;
  public static @NotNull final Potion LUCK;
  public static @NotNull final Potion UNLUCK;
  public static @NotNull final Potion SATURATION;
  public static @NotNull final Potion STRONG_SATURATION;
  public static @NotNull final Potion HUNGER;
  public static @NotNull final Potion LONG_HUNGER;
  public static @NotNull final Potion STRONG_HUNGER;
  public static @NotNull final Potion HASTE;
  public static @NotNull final Potion LONG_HASTE;
  public static @NotNull final Potion STRONG_HASTE;
  public static @NotNull final Potion MINING_FATIGUE;
  public static @NotNull final Potion LONG_MINING_FATIGUE;
  public static @NotNull final Potion STRONG_MINING_FATIGUE;
  public static @NotNull final Potion DARKNESS;
  public static @NotNull final Potion STRONG_DARKNESS;

  static {
    RECIPES = new ArrayList<>();
    RESISTANCE =
        register(
            "resistance", createPotion(StatusEffects.RESISTANCE, 3600, 0), NAUTILUS_SHELL, AWKWARD);
    LONG_RESISTANCE =
        register(
            "long_resistance",
            createPotion(StatusEffects.RESISTANCE, 9600, 0),
            REDSTONE,
            RESISTANCE);
    STRONG_RESISTANCE =
        register(
            "strong_resistance",
            createPotion(StatusEffects.RESISTANCE, 450, 1),
            GLOWSTONE,
            RESISTANCE);
    ABSORPTION =
        register(
            "absorption", createPotion(StatusEffects.ABSORPTION, 2400, 1), GOLDEN_APPLE, AWKWARD);
    LONG_ABSORPTION =
        register(
            "long_absorption",
            createPotion(StatusEffects.ABSORPTION, 4800, 1),
            REDSTONE,
            ABSORPTION);
    STRONG_ABSORPTION =
        register(
            "strong_absorption",
            createPotion(StatusEffects.ABSORPTION, 1200, 3),
            GLOWSTONE,
            ABSORPTION);
    BLINDNESS =
        register("blindness", createPotion(StatusEffects.BLINDNESS, 800, 0), INK_SAC, AWKWARD);
    LONG_BLINDNESS =
        register(
            "long_blindness", createPotion(StatusEffects.BLINDNESS, 1600, 0), REDSTONE, BLINDNESS);
    GLOWING = register("glowing", createPotion(StatusEffects.GLOWING, 800, 0), GLOWSTONE, MUNDANE);
    LONG_GLOWING =
        register("long_glowing", createPotion(StatusEffects.GLOWING, 1600, 0), REDSTONE, GLOWING);
    HEALTH_BOOST =
        register(
            "health_boost",
            createPotion(StatusEffects.HEALTH_BOOST, 800, 0),
            GLISTERING_MELON_SLICE,
            ABSORPTION);
    LONG_HEALTH_BOOST =
        register(
            "long_health_boost",
            createPotion(StatusEffects.HEALTH_BOOST, 1600, 0),
            REDSTONE,
            HEALTH_BOOST);
    STRONG_HEALTH_BOOST =
        register(
            "strong_health_boost",
            createPotion(StatusEffects.HEALTH_BOOST, 400, 1),
            REDSTONE,
            HEALTH_BOOST);
    LEVITATION =
        register(
            "levitation",
            createPotion(StatusEffects.LEVITATION, 400, 0),
            FERMENTED_SPIDER_EYE,
            SLOW_FALLING);
    LONG_LEVITATION =
        register(
            "long_levitation",
            createPotion(StatusEffects.LEVITATION, 600, 0),
            REDSTONE,
            LEVITATION);
    STRONG_LEVITATION =
        register(
            "strong_levitation",
            createPotion(StatusEffects.LEVITATION, 40, 9),
            GLOWSTONE,
            LEVITATION);
    NAUSEA =
        register("nausea", createPotion(StatusEffects.NAUSEA, 1800, 0), POISONOUS_POTATO, AWKWARD);
    LONG_NAUSEA =
        register("long_nausea", createPotion(StatusEffects.NAUSEA, 2400, 0), REDSTONE, NAUSEA);
    WITHER = register("wither", createPotion(StatusEffects.WITHER, 1800, 0), WITHER_ROSE, AWKWARD);
    LONG_WITHER =
        register("long_wither", createPotion(StatusEffects.WITHER, 2400, 0), REDSTONE, WITHER);
    STRONG_WITHER =
        register("strong_wither", createPotion(StatusEffects.WITHER, 880, 1), GLOWSTONE, WITHER);
    LUCK = register("luck", createPotion(StatusEffects.LUCK, 6000, 0), EMERALD, AWKWARD);
    UNLUCK =
        register("unluck", createPotion(StatusEffects.UNLUCK, 6000, 0), FERMENTED_SPIDER_EYE, LUCK);
    SATURATION =
        register("saturation", createPotion(StatusEffects.SATURATION, 1, 7), BEETROOT, AWKWARD);
    STRONG_SATURATION =
        register(
            "strong_saturation",
            createPotion(StatusEffects.SATURATION, 1, 16),
            GLOWSTONE,
            SATURATION);
    HUNGER =
        register(
            "hunger",
            createPotion(StatusEffects.HUNGER, 2400, 0),
            FERMENTED_SPIDER_EYE,
            SATURATION);
    LONG_HUNGER =
        register("long_hunger", createPotion(StatusEffects.HUNGER, 3600, 0), REDSTONE, HUNGER);
    STRONG_HUNGER =
        register("strong_hunger", createPotion(StatusEffects.HUNGER, 1200, 1), GLOWSTONE, HUNGER);
    HASTE = register("haste", createPotion(StatusEffects.HASTE, 3600, 0), GOLD_NUGGET, SWIFTNESS);
    LONG_HASTE =
        register("long_haste", createPotion(StatusEffects.HASTE, 9600, 0), REDSTONE, HASTE);
    STRONG_HASTE =
        register("strong_haste", createPotion(StatusEffects.HASTE, 2400, 1), GLOWSTONE, HASTE);
    MINING_FATIGUE =
        register(
            "mining_fatigue",
            createPotion(StatusEffects.MINING_FATIGUE, 3600, 0),
            FERMENTED_SPIDER_EYE,
            HASTE);
    LONG_MINING_FATIGUE =
        register(
            "long_mining_fatigue",
            createPotion(StatusEffects.MINING_FATIGUE, 9600, 0),
            REDSTONE,
            MINING_FATIGUE);
    STRONG_MINING_FATIGUE =
        register(
            "strong_mining_fatigue",
            createPotion(StatusEffects.MINING_FATIGUE, 2400, 1),
            GLOWSTONE,
            MINING_FATIGUE);
    DARKNESS =
        register("darkness", createPotion(StatusEffects.DARKNESS, 800, 0), ECHO_SHARD, BLINDNESS);
    STRONG_DARKNESS =
        register(
            "strong_darkness", createPotion(StatusEffects.DARKNESS, 1600, 0), REDSTONE, DARKNESS);
  }

  private static @NotNull Potion createPotion(
      @NotNull final StatusEffect effect, final int duration, final int amplifier) {
    return new Potion(new StatusEffectInstance(effect, duration, amplifier));
  }

  private static @NotNull Potion register(
      @NotNull final String id,
      @NotNull final Potion potion,
      @NotNull final Item ingredient,
      @NotNull final Potion from) {
    RECIPES.add(new PotionRecipe(from, ingredient, potion));
    return Registry.register(Registries.POTION, new Identifier("smpearth", id), potion);
  }

  public static void registerPotions() {
    RECIPES.forEach(PotionRecipe::register);
  }
}
