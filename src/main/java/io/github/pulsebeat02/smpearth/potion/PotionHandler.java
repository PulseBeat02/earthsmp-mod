package io.github.pulsebeat02.smpearth.potion;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.recipe.BrewingRecipeRegistry;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public final class PotionHandler {

  private static final Collection<PotionRecipe> RECIPES;
  public static final Potion RESISTANCE;
  public static final Potion LONG_RESISTANCE;
  public static final Potion STRONG_RESISTANCE;
  public static final Potion ABSORPTION;
  public static final Potion LONG_ABSORPTION;
  public static final Potion STRONG_ABSORPTION;
  public static final Potion BLINDNESS;
  public static final Potion LONG_BLINDNESS;
  public static final Potion GLOWING;
  public static final Potion LONG_GLOWING;
  public static final Potion HEALTH_BOOST;
  public static final Potion LONG_HEALTH_BOOST;
  public static final Potion STRONG_HEALTH_BOOST;
  public static final Potion LEVITATION;
  public static final Potion LONG_LEVITATION;
  public static final Potion STRONG_LEVITATION;
  public static final Potion NAUSEA;
  public static final Potion LONG_NAUSEA;
  public static final Potion WITHER;
  public static final Potion LONG_WITHER;
  public static final Potion STRONG_WITHER;
  public static final Potion LUCK;
  public static final Potion UNLUCK;
  public static final Potion SATURATION;
  public static final Potion STRONG_SATURATION;
  public static final Potion HUNGER;
  public static final Potion LONG_HUNGER;
  public static final Potion STRONG_HUNGER;
  public static final Potion HASTE;
  public static final Potion LONG_HASTE;
  public static final Potion STRONG_HASTE;
  public static final Potion MINING_FATIGUE;
  public static final Potion LONG_MINING_FATIGUE;
  public static final Potion STRONG_MINING_FATIGUE;
  public static final Potion DARKNESS;
  public static final Potion STRONG_DARKNESS;

  static {
    RECIPES = new ArrayList<>();
    RESISTANCE =
        register(
            "resistance",
            new Potion(new StatusEffectInstance(StatusEffects.RESISTANCE, 3600, 0)),
            Items.NAUTILUS_SHELL,
            Potions.AWKWARD);
    LONG_RESISTANCE =
        register(
            "long_resistance",
            new Potion(new StatusEffectInstance(StatusEffects.RESISTANCE, 9600, 0)),
            Items.REDSTONE,
            RESISTANCE);
    STRONG_RESISTANCE =
        register(
            "strong_resistance",
            new Potion(new StatusEffectInstance(StatusEffects.RESISTANCE, 450, 1)),
            Items.GLOWSTONE,
            RESISTANCE);
    ABSORPTION =
        register(
            "absorption",
            new Potion(new StatusEffectInstance(StatusEffects.ABSORPTION, 2400, 1)),
            Items.GOLDEN_APPLE,
            Potions.AWKWARD);
    LONG_ABSORPTION =
        register(
            "long_absorption",
            new Potion(new StatusEffectInstance(StatusEffects.ABSORPTION, 4800, 1)),
            Items.REDSTONE,
            ABSORPTION);
    STRONG_ABSORPTION =
        register(
            "strong_absorption",
            new Potion(new StatusEffectInstance(StatusEffects.ABSORPTION, 1200, 3)),
            Items.GLOWSTONE,
            ABSORPTION);
    BLINDNESS =
        register(
            "blindness",
            new Potion(new StatusEffectInstance(StatusEffects.BLINDNESS, 800, 0)),
            Items.INK_SAC,
            Potions.AWKWARD);
    LONG_BLINDNESS =
        register(
            "long_blindness",
            new Potion(new StatusEffectInstance(StatusEffects.BLINDNESS, 1600, 0)),
            Items.REDSTONE,
            BLINDNESS);
    GLOWING =
        register(
            "glowing",
            new Potion(new StatusEffectInstance(StatusEffects.GLOWING, 800, 0)),
            Items.GLOWSTONE,
            Potions.MUNDANE);
    LONG_GLOWING =
        register(
            "long_glowing",
            new Potion(new StatusEffectInstance(StatusEffects.GLOWING, 1600, 0)),
            Items.REDSTONE,
            GLOWING);
    HEALTH_BOOST =
        register(
            "health_boost",
            new Potion(new StatusEffectInstance(StatusEffects.HEALTH_BOOST, 800, 0)),
            Items.GLISTERING_MELON_SLICE,
            ABSORPTION);
    LONG_HEALTH_BOOST =
        register(
            "long_health_boost",
            new Potion(new StatusEffectInstance(StatusEffects.HEALTH_BOOST, 1600, 0)),
            Items.REDSTONE,
            HEALTH_BOOST);
    STRONG_HEALTH_BOOST =
        register(
            "strong_health_boost",
            new Potion(new StatusEffectInstance(StatusEffects.HEALTH_BOOST, 400, 1)),
            Items.REDSTONE,
            HEALTH_BOOST);
    LEVITATION =
        register(
            "levitation",
            new Potion(new StatusEffectInstance(StatusEffects.LEVITATION, 400, 0)),
            Items.FERMENTED_SPIDER_EYE,
            Potions.SLOW_FALLING);
    LONG_LEVITATION =
        register(
            "long_levitation",
            new Potion(new StatusEffectInstance(StatusEffects.LEVITATION, 600, 0)),
            Items.REDSTONE,
            LEVITATION);
    STRONG_LEVITATION =
        register(
            "strong_levitation",
            new Potion(new StatusEffectInstance(StatusEffects.LEVITATION, 40, 9)),
            Items.GLOWSTONE,
            LEVITATION);
    NAUSEA =
        register(
            "nausea",
            new Potion(new StatusEffectInstance(StatusEffects.NAUSEA, 1800, 0)),
            Items.POISONOUS_POTATO,
            Potions.AWKWARD);
    LONG_NAUSEA =
        register(
            "long_nausea",
            new Potion(new StatusEffectInstance(StatusEffects.NAUSEA, 2400, 0)),
            Items.REDSTONE,
            NAUSEA);
    WITHER =
        register(
            "wither",
            new Potion(new StatusEffectInstance(StatusEffects.WITHER, 1800, 0)),
            Items.WITHER_ROSE,
            Potions.AWKWARD);
    LONG_WITHER =
        register(
            "long_wither",
            new Potion(new StatusEffectInstance(StatusEffects.WITHER, 2400, 0)),
            Items.REDSTONE,
            WITHER);
    STRONG_WITHER =
        register(
            "strong_wither",
            new Potion(new StatusEffectInstance(StatusEffects.WITHER, 880, 1)),
            Items.GLOWSTONE,
            WITHER);
    LUCK =
        register(
            "luck",
            new Potion(new StatusEffectInstance(StatusEffects.LUCK, 6000, 0)),
            Items.EMERALD,
            Potions.AWKWARD);
    UNLUCK =
        register(
            "unluck",
            new Potion(new StatusEffectInstance(StatusEffects.UNLUCK, 6000, 0)),
            Items.FERMENTED_SPIDER_EYE,
            LUCK);
    SATURATION =
        register(
            "saturation",
            new Potion(new StatusEffectInstance(StatusEffects.SATURATION, 1, 7)),
            Items.BEETROOT,
            Potions.AWKWARD);
    STRONG_SATURATION =
        register(
            "strong_saturation",
            new Potion(new StatusEffectInstance(StatusEffects.SATURATION, 1, 16)),
            Items.GLOWSTONE,
            SATURATION);
    HUNGER =
        register(
            "hunger",
            new Potion(new StatusEffectInstance(StatusEffects.HUNGER, 2400, 0)),
            Items.FERMENTED_SPIDER_EYE,
            SATURATION);
    LONG_HUNGER =
        register(
            "long_hunger",
            new Potion(new StatusEffectInstance(StatusEffects.HUNGER, 3600, 0)),
            Items.REDSTONE,
            HUNGER);
    STRONG_HUNGER =
        register(
            "strong_hunger",
            new Potion(new StatusEffectInstance(StatusEffects.HUNGER, 1200, 1)),
            Items.GLOWSTONE,
            HUNGER);
    HASTE =
        register(
            "haste",
            new Potion(new StatusEffectInstance(StatusEffects.HASTE, 3600, 0)),
            Items.GOLD_NUGGET,
            Potions.SWIFTNESS);
    LONG_HASTE =
        register(
            "long_haste",
            new Potion(new StatusEffectInstance(StatusEffects.HASTE, 9600, 0)),
            Items.REDSTONE,
            HASTE);
    STRONG_HASTE =
        register(
            "strong_haste",
            new Potion(new StatusEffectInstance(StatusEffects.HASTE, 2400, 1)),
            Items.GLOWSTONE,
            HASTE);
    MINING_FATIGUE =
        register(
            "mining_fatigue",
            new Potion(new StatusEffectInstance(StatusEffects.MINING_FATIGUE, 3600, 0)),
            Items.FERMENTED_SPIDER_EYE,
            HASTE);
    LONG_MINING_FATIGUE =
        register(
            "long_mining_fatigue",
            new Potion(new StatusEffectInstance(StatusEffects.MINING_FATIGUE, 9600, 0)),
            Items.REDSTONE,
            MINING_FATIGUE);
    STRONG_MINING_FATIGUE =
        register(
            "strong_mining_fatigue",
            new Potion(new StatusEffectInstance(StatusEffects.MINING_FATIGUE, 2400, 1)),
            Items.GLOWSTONE,
            MINING_FATIGUE);
    DARKNESS =
        register(
            "darkness",
            new Potion(new StatusEffectInstance(StatusEffects.DARKNESS, 800, 0)),
            Items.ECHO_SHARD,
            BLINDNESS);
    STRONG_DARKNESS =
        register(
            "strong_darkness",
            new Potion(new StatusEffectInstance(StatusEffects.DARKNESS, 1600, 0)),
            Items.REDSTONE,
            DARKNESS);
  }

  private static Potion register(
      @NotNull final String id,
      @NotNull final Potion potion,
      @NotNull final Item ingredient,
      @NotNull final Potion from) {
    RECIPES.add(new PotionRecipe(from, ingredient, potion));
    return register(id, potion);
  }

  private static Potion register(@NotNull final String id, @NotNull final Potion potion) {
    return Registry.register(Registries.POTION, new Identifier("smpearth", id), potion);
  }

  public static void registerPotions() {
    RECIPES.forEach(PotionRecipe::register);
    mapPotions(ABSORPTION, Items.FERMENTED_SPIDER_EYE, Potions.HARMING);
    mapPotions(HEALTH_BOOST, Items.FERMENTED_SPIDER_EYE, Potions.HARMING);
  }

  static void mapPotions(
      @NotNull final Potion in, @NotNull final Item ingredient, @NotNull final Potion result) {

    final Identifier potionInId = Registries.POTION.getId(in);
    final String pathIn = potionInId.getPath();
    final String namespaceIn = potionInId.getNamespace();
    final Identifier longIdIn = new Identifier(namespaceIn, "long_" + pathIn);
    final Identifier strongIdIn = new Identifier(namespaceIn, "strong_" + pathIn);

    final Identifier potionOutId = Registries.POTION.getId(result);
    final String pathOut = potionOutId.getPath();
    final String namespaceOut = potionOutId.getNamespace();
    final Identifier longIdOut = new Identifier(namespaceOut, "long_" + pathOut);
    final Identifier strongIdOut = new Identifier(namespaceOut, "strong_" + pathOut);

    final Optional<Potion> inLong = Registries.POTION.getOrEmpty(longIdIn);
    final Optional<Potion> inStrong = Registries.POTION.getOrEmpty(strongIdIn);
    final Optional<Potion> outLong = Registries.POTION.getOrEmpty(longIdOut);
    final Optional<Potion> outStrong = Registries.POTION.getOrEmpty(strongIdOut);
    if (outLong.isPresent() && inLong.isPresent()) {
      BrewingRecipeRegistry.registerPotionRecipe(inLong.get(), ingredient, outLong.get());
    }
    if (outStrong.isPresent() && inStrong.isPresent()) {
      BrewingRecipeRegistry.registerPotionRecipe(inStrong.get(), ingredient, outStrong.get());
    }
    BrewingRecipeRegistry.registerPotionRecipe(in, ingredient, result);
  }
}
