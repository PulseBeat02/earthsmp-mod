package io.github.pulsebeat02.smpearth.mixin;

import java.util.Collection;
import java.util.Collections;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.recipe.Ingredient;
import org.jetbrains.annotations.NotNull;

public final class PotionHandler {
  public static final ItemStack RESISTANCE_POTION;
  public static final ItemStack RESISTANCE_POTION2;
  public static final ItemStack RESISTANCE_POTION3;
  public static final ItemStack ABSORPTION_POTION;
  public static final ItemStack ABSORPTION_POTION2;
  public static final ItemStack ABSORPTION_POTION3;
  public static final ItemStack BLINDNESS_POTION;
  public static final ItemStack BLINDNESS_POTION2;
  public static final ItemStack GLOWING_POTION;
  public static final ItemStack GLOWING_POTION2;
  public static final ItemStack HEALTH_BOOST_POTION;
  public static final ItemStack HEALTH_BOOST_POTION2;
  public static final ItemStack LEVITATION_POTION;
  public static final ItemStack LEVITATION_POTION2;
  public static final ItemStack LEVITATION_POTION3;
  public static final ItemStack NAUSEA_POTION;
  public static final ItemStack NAUSEA_POTION2;
  public static final ItemStack WITHERING_POTION;
  public static final ItemStack WITHERING_POTION2;
  public static final ItemStack WITHERING_POTION3;
  public static final ItemStack LUCK_POTION;
  public static final ItemStack UNLUCK_POTION;
  public static final ItemStack SATURATION_POTION;
  public static final ItemStack SATURATION_POTION2;
  public static final ItemStack HUNGER_POTION;
  public static final ItemStack HUNGER_POTION2;
  public static final ItemStack HUNGER_POTION3;
  public static final ItemStack HASTE_POTION;
  public static final ItemStack HASTE_POTION2;
  public static final ItemStack HASTE_POTION3;
  public static final ItemStack MINING_FATIGUE_POTION;
  public static final ItemStack MINING_FATIGUE_POTION2;
  public static final ItemStack MINING_FATIGUE_POTION3;
  public static final ItemStack DARKNESS_POTION;
  public static final ItemStack DARKNESS_POTION2;

  public static @NotNull ItemStack register(
      @NotNull final String name,
      @NotNull final StatusEffectInstance effect,
      @NotNull final Item ingredient,
      @NotNull final ItemStack input) {
    final Collection<StatusEffectInstance> effects = Collections.singleton(effect);
    final ItemStack potion =
        PotionUtil.setCustomPotionEffects(new ItemStack(Items.POTION), effects);
    BrewingRecipeRegistryMixin.addCustomPotionRecipe(
        name, input, Ingredient.ofItems(ingredient), potion);
    // Registry.register(Registries.POTION, new Identifier("earthsmp", name), potion);
    return potion;
  }

  static {
    RESISTANCE_POTION =
        register(
            "resistance_potion",
            new StatusEffectInstance(StatusEffects.RESISTANCE, 3600, 0),
            Items.NAUTILUS_SHELL,
            new ItemStack((ItemConvertible) Potions.AWKWARD));
    RESISTANCE_POTION2 =
        register(
            "resistance_potion2",
            new StatusEffectInstance(StatusEffects.RESISTANCE, 9600, 0),
            Items.REDSTONE,
            RESISTANCE_POTION);
    RESISTANCE_POTION3 =
        register(
            "resistance_potion3",
            new StatusEffectInstance(StatusEffects.RESISTANCE, 450, 1),
            Items.GLOWSTONE,
            RESISTANCE_POTION);
    ABSORPTION_POTION =
        register(
            "absorption_potion",
            new StatusEffectInstance(StatusEffects.ABSORPTION, 2400, 1),
            Items.GOLDEN_APPLE,
            new ItemStack((ItemConvertible) Potions.AWKWARD));
    ABSORPTION_POTION2 =
        register(
            "absorption_potion2",
            new StatusEffectInstance(StatusEffects.ABSORPTION, 4800, 1),
            Items.REDSTONE,
            ABSORPTION_POTION);
    ABSORPTION_POTION3 =
        register(
            "absorption_potion3",
            new StatusEffectInstance(StatusEffects.ABSORPTION, 1200, 3),
            Items.GLOWSTONE,
            ABSORPTION_POTION);
    BLINDNESS_POTION =
        register(
            "blindness_potion",
            new StatusEffectInstance(StatusEffects.BLINDNESS, 800, 0),
            Items.INK_SAC,
            new ItemStack((ItemConvertible) Potions.AWKWARD));
    BLINDNESS_POTION2 =
        register(
            "blindness_potion2",
            new StatusEffectInstance(StatusEffects.BLINDNESS, 1600, 0),
            Items.REDSTONE,
            BLINDNESS_POTION);
    GLOWING_POTION =
        register(
            "glowing_potion",
            new StatusEffectInstance(StatusEffects.GLOWING, 800, 0),
            Items.GLOWSTONE,
            new ItemStack((ItemConvertible) Potions.MUNDANE));
    GLOWING_POTION2 =
        register(
            "glowing_potion2",
            new StatusEffectInstance(StatusEffects.GLOWING, 1600, 0),
            Items.REDSTONE,
            GLOWING_POTION);
    HEALTH_BOOST_POTION =
        register(
            "health_boost_potion",
            new StatusEffectInstance(StatusEffects.HEALTH_BOOST, 800, 0),
            Items.GLISTERING_MELON_SLICE,
            ABSORPTION_POTION);
    HEALTH_BOOST_POTION2 =
        register(
            "health_boost_potion2",
            new StatusEffectInstance(StatusEffects.HEALTH_BOOST, 1600, 0),
            Items.REDSTONE,
            HEALTH_BOOST_POTION);
    LEVITATION_POTION =
        register(
            "levitation_potion",
            new StatusEffectInstance(StatusEffects.LEVITATION, 400, 0),
            Items.FERMENTED_SPIDER_EYE,
            new ItemStack((ItemConvertible) Potions.SLOW_FALLING));
    LEVITATION_POTION2 =
        register(
            "levitation_potion2",
            new StatusEffectInstance(StatusEffects.LEVITATION, 600, 0),
            Items.REDSTONE,
            LEVITATION_POTION);
    LEVITATION_POTION3 =
        register(
            "levitation_potion3",
            new StatusEffectInstance(StatusEffects.LEVITATION, 40, 9),
            Items.GLOWSTONE,
            LEVITATION_POTION);
    NAUSEA_POTION =
        register(
            "nausea_potion",
            new StatusEffectInstance(StatusEffects.NAUSEA, 1800, 0),
            Items.POISONOUS_POTATO,
            new ItemStack((ItemConvertible) Potions.AWKWARD));
    NAUSEA_POTION2 =
        register(
            "nausea_potion2",
            new StatusEffectInstance(StatusEffects.NAUSEA, 2400, 0),
            Items.REDSTONE,
            NAUSEA_POTION);
    WITHERING_POTION =
        register(
            "withering_potion",
            new StatusEffectInstance(StatusEffects.WITHER, 1800, 0),
            Items.WITHER_ROSE,
            new ItemStack((ItemConvertible) Potions.AWKWARD));
    WITHERING_POTION2 =
        register(
            "withering_potion2",
            new StatusEffectInstance(StatusEffects.WITHER, 2400, 0),
            Items.REDSTONE,
            WITHERING_POTION);
    WITHERING_POTION3 =
        register(
            "withering_potion3",
            new StatusEffectInstance(StatusEffects.WITHER, 880, 1),
            Items.GLOWSTONE,
            WITHERING_POTION);
    LUCK_POTION =
        register(
            "luck_potion",
            new StatusEffectInstance(StatusEffects.LUCK, 6000, 0),
            Items.EMERALD,
            new ItemStack((ItemConvertible) Potions.AWKWARD));
    UNLUCK_POTION =
        register(
            "unluck_potion",
            new StatusEffectInstance(StatusEffects.UNLUCK, 6000, 0),
            Items.FERMENTED_SPIDER_EYE,
            LUCK_POTION);
    SATURATION_POTION =
        register(
            "saturation_potion",
            new StatusEffectInstance(StatusEffects.SATURATION, 1, 7),
            Items.BEETROOT,
            new ItemStack((ItemConvertible) Potions.AWKWARD));
    SATURATION_POTION2 =
        register(
            "saturation_potion2",
            new StatusEffectInstance(StatusEffects.SATURATION, 1, 16),
            Items.GLOWSTONE,
            SATURATION_POTION);
    HUNGER_POTION =
        register(
            "hunger_potion",
            new StatusEffectInstance(StatusEffects.HUNGER, 2400, 0),
            Items.FERMENTED_SPIDER_EYE,
            SATURATION_POTION);
    HUNGER_POTION2 =
        register(
            "hunger_potion2",
            new StatusEffectInstance(StatusEffects.HUNGER, 3600, 0),
            Items.REDSTONE,
            HUNGER_POTION);
    HUNGER_POTION3 =
        register(
            "hunger_potion3",
            new StatusEffectInstance(StatusEffects.HUNGER, 1200, 1),
            Items.GLOWSTONE,
            HUNGER_POTION);
    HASTE_POTION =
        register(
            "haste_potion",
            new StatusEffectInstance(StatusEffects.HASTE, 3600, 0),
            Items.GOLD_NUGGET,
            new ItemStack((ItemConvertible) Potions.SWIFTNESS));
    HASTE_POTION2 =
        register(
            "haste_potion2",
            new StatusEffectInstance(StatusEffects.HASTE, 9600, 0),
            Items.REDSTONE,
            HASTE_POTION);
    HASTE_POTION3 =
        register(
            "haste_potion3",
            new StatusEffectInstance(StatusEffects.HASTE, 2400, 1),
            Items.GLOWSTONE,
            HASTE_POTION);
    MINING_FATIGUE_POTION =
        register(
            "mining_fatigue_potion",
            new StatusEffectInstance(StatusEffects.MINING_FATIGUE, 3600, 0),
            Items.FERMENTED_SPIDER_EYE,
            HASTE_POTION);
    MINING_FATIGUE_POTION2 =
        register(
            "mining_fatigue_potion2",
            new StatusEffectInstance(StatusEffects.MINING_FATIGUE, 9600, 0),
            Items.REDSTONE,
            MINING_FATIGUE_POTION);
    MINING_FATIGUE_POTION3 =
        register(
            "mining_fatigue_potion3",
            new StatusEffectInstance(StatusEffects.MINING_FATIGUE, 2400, 1),
            Items.GLOWSTONE,
            MINING_FATIGUE_POTION);
    DARKNESS_POTION =
        register(
            "darkness_potion",
            new StatusEffectInstance(StatusEffects.DARKNESS, 800, 0),
            Items.ECHO_SHARD,
            BLINDNESS_POTION);
    DARKNESS_POTION2 =
        register(
            "darkness_potion2",
            new StatusEffectInstance(StatusEffects.DARKNESS, 1600, 0),
            Items.REDSTONE,
            DARKNESS_POTION);
  }
}
