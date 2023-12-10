package io.github.pulsebeat02.smpearth.mixin;

import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.recipe.BrewingRecipeRegistry;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BrewingRecipeRegistry.class)
public interface BrewingRecipeRegistryMixin {
  @Invoker("registerPotionRecipe")
  static void invokeRegisterPotionRecipe(
      @NotNull final Potion input, @NotNull final Item item, @NotNull final Potion output) {
    throw new AssertionError();
  }
}
