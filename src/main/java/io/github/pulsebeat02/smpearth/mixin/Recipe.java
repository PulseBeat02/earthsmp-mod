package io.github.pulsebeat02.smpearth.mixin;

import net.minecraft.recipe.Ingredient;
import org.jetbrains.annotations.NotNull;

public final class Recipe<T> {

  private @NotNull final String id;
  private @NotNull final T input;
  private @NotNull final Ingredient ingredient;
  private @NotNull final T output;

  public Recipe(
      @NotNull final String id,
      @NotNull final T input,
      @NotNull final Ingredient ingredient,
      @NotNull final T output) {
    this.id = id;
    this.input = input;
    this.ingredient = ingredient;
    this.output = output;
  }

  public @NotNull String getId() {
    return this.id;
  }

  public @NotNull T getInput() {
    return this.input;
  }

  public @NotNull Ingredient getIngredient() {
    return this.ingredient;
  }

  public @NotNull T getOutput() {
    return this.output;
  }
}
