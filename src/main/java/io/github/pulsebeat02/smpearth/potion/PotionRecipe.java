package io.github.pulsebeat02.smpearth.potion;

import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.recipe.BrewingRecipeRegistry;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public record PotionRecipe(
    @NotNull Potion input, @NotNull Item ingredient, @NotNull Potion output) {

  public void register() {
    this.mapPotions(this.input, this.ingredient, this.output);
    this.registerVariantRecipe();
  }

  private void registerVariantRecipe() {
    final Identifier id = Registries.POTION.getId(this.output);
    final Pair<Identifier, Identifier> pair = this.getLongStrong(id);
    final Identifier left = pair.getLeft();
    final Identifier right = pair.getRight();
    Registries.POTION.getOrEmpty(left).ifPresent(this::registerLongIngredient);
    Registries.POTION.getOrEmpty(right).ifPresent(this::registerStrongIngredient);
  }

  private void registerStrongIngredient(@NotNull final Potion potion) {
    BrewingRecipeRegistry.registerPotionRecipe(potion, Items.GLOWSTONE_DUST, potion);
  }

  private void registerLongIngredient(@NotNull final Potion potion) {
    BrewingRecipeRegistry.registerPotionRecipe(this.output, Items.REDSTONE, potion);
  }

  private void mapPotions(
      @NotNull final Potion in, @NotNull final Item ingredient, @NotNull final Potion result) {
    final Identifier potionOutId = Registries.POTION.getId(result);
    final Identifier potionInId = Registries.POTION.getId(in);
    final Pair<Identifier, Identifier> inPair = this.getLongStrong(potionInId);
    final Pair<Identifier, Identifier> outPair = this.getLongStrong(potionOutId);
    final Identifier longIdIn = inPair.getLeft();
    final Identifier strongIdIn = inPair.getRight();
    final Identifier longIdOut = outPair.getLeft();
    final Identifier strongIdOut = outPair.getRight();
    final Optional<Potion> inLong = Registries.POTION.getOrEmpty(longIdIn);
    final Optional<Potion> inStrong = Registries.POTION.getOrEmpty(strongIdIn);
    final Optional<Potion> outLong = Registries.POTION.getOrEmpty(longIdOut);
    final Optional<Potion> outStrong = Registries.POTION.getOrEmpty(strongIdOut);
    outLong.ifPresent(
        outPotion ->
            inLong.ifPresent(
                inPotion -> this.registerCustomPotionRecipe(inPotion, ingredient, outPotion)));
    outStrong.ifPresent(
        outPotion ->
            inStrong.ifPresent(
                inPotion -> this.registerCustomPotionRecipe(inPotion, ingredient, outPotion)));
    BrewingRecipeRegistry.registerPotionRecipe(in, ingredient, result);
  }

  private void registerCustomPotionRecipe(
      @NotNull final Potion in, @NotNull final Item ingredient, @NotNull final Potion result) {
    BrewingRecipeRegistry.registerPotionRecipe(in, ingredient, result);
  }

  private @NotNull Pair<Identifier, Identifier> getLongStrong(@NotNull final Identifier base) {
    final String pathOut = base.getPath();
    final String namespaceOut = base.getNamespace();
    final Identifier longId = new Identifier(namespaceOut, "long_" + pathOut);
    final Identifier strongId = new Identifier(namespaceOut, "strong_" + pathOut);
    return new Pair<>(longId, strongId);
  }
}
