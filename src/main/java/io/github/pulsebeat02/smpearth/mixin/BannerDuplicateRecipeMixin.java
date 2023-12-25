package io.github.pulsebeat02.smpearth.mixin;

import net.minecraft.recipe.BannerDuplicateRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(BannerDuplicateRecipe.class)
public final class BannerDuplicateRecipeMixin {

  @ModifyConstant(
      method =
          "matches(Lnet/minecraft/inventory/RecipeInputInventory;Lnet/minecraft/world/World;)Z",
      constant = @Constant(intValue = 6))
  private int maxBannerPatternLS(final int orig) {
    return Integer.MAX_VALUE;
  }

  @ModifyConstant(
      method =
          "craft(Lnet/minecraft/inventory/RecipeInputInventory;Lnet/minecraft/registry/DynamicRegistryManager;)Lnet/minecraft/item/ItemStack;",
      constant = @Constant(intValue = 6))
  private int maxBannerPattern(final int orig) {
    return Integer.MAX_VALUE;
  }
}
