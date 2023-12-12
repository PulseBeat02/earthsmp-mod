package io.github.pulsebeat02.smpearth.potion;

import static io.github.pulsebeat02.smpearth.potion.PotionHandler.mapPotions;

import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.recipe.BrewingRecipeRegistry;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public final class PotionRecipe {
  private @NotNull final Potion input;
  private @NotNull final Item ingredient;
  private @NotNull final Potion output;

  public PotionRecipe(
      @NotNull final Potion input, @NotNull final Item ingredient, @NotNull final Potion output) {
    this.input = input;
    this.ingredient = ingredient;
    this.output = output;
  }

  public @NotNull Potion getInput() {
    return this.input;
  }

  public @NotNull Item getIngredient() {
    return this.ingredient;
  }

  public @NotNull Potion getOutput() {
    return this.output;
  }

  public void register() {
    mapPotions(this.input, this.ingredient, this.output);
    this.registerVariantRecipe();
  }

  private void registerVariantRecipe() {
    final Identifier id = Registries.POTION.getId(this.output);
    final String path = id.getPath();
    final String namespace = id.getNamespace();
    final Identifier longId = new Identifier(namespace, "long_" + path);
    final Identifier strongId = new Identifier(namespace, "strong_" + path);
    Registries.POTION.getOrEmpty(longId).ifPresent(this::registerLongIngredient);
    Registries.POTION.getOrEmpty(strongId).ifPresent(this::registerStrongIngredient);
  }

  private void registerStrongIngredient(@NotNull final Potion potion) {
    BrewingRecipeRegistry.registerPotionRecipe(potion, Items.GLOWSTONE_DUST, potion);
  }

  private void registerLongIngredient(@NotNull final Potion potion) {
    BrewingRecipeRegistry.registerPotionRecipe(this.output, Items.REDSTONE, potion);
  }
}
