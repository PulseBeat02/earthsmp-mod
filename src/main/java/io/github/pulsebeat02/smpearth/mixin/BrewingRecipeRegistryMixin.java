package io.github.pulsebeat02.smpearth.mixin;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PotionItem;
import net.minecraft.potion.PotionUtil;
import net.minecraft.recipe.BrewingRecipeRegistry;
import net.minecraft.recipe.Ingredient;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BrewingRecipeRegistry.class)
public final class BrewingRecipeRegistryMixin {

  private static final List<Recipe<ItemStack>> CUSTOM_POTION_RECIPES;

  static {
    CUSTOM_POTION_RECIPES = Lists.newArrayList();
  }

  @Unique
  static void addCustomPotionRecipe(
      @NotNull final String name,
      @NotNull final ItemStack input,
      @NotNull final Ingredient ingredient,
      @NotNull final ItemStack output) {
    CUSTOM_POTION_RECIPES.add(new Recipe<>(name, input, ingredient, output));
  }

  @Inject(method = "isItemRecipeIngredient", at = @At("HEAD"), cancellable = true)
  private static void customIngredientCheck(
      @NotNull final ItemStack stack, @NotNull final CallbackInfoReturnable<Boolean> cir) {
    for (final Recipe<ItemStack> recipe : CUSTOM_POTION_RECIPES) {
      if (!recipe.getIngredient().test(stack)) {
          continue;
      }
      cir.setReturnValue(true);
    }
    cir.setReturnValue(false);
  }

  @Inject(method = "isPotionRecipeIngredient", at = @At("HEAD"), cancellable = true)
  private static void customPotionIngredientCheck(
      @NotNull final ItemStack stack, @NotNull final CallbackInfoReturnable<Boolean> cir) {
    for (final Recipe<ItemStack> recipe : CUSTOM_POTION_RECIPES) {
      if (!recipe.getIngredient().test(stack)) {
          continue;
      }
      cir.setReturnValue(true);
    }
    cir.setReturnValue(false);
  }

  @Inject(method = "hasItemRecipe", at = @At("HEAD"), cancellable = true)
  private static void hasItemRecipeCheck(
          final ItemStack input, final ItemStack ingredient, final CallbackInfoReturnable<Boolean> cir) {
    checkRecipe(input, ingredient, cir);
  }

  private static void checkRecipe(final ItemStack input, final ItemStack ingredient, final CallbackInfoReturnable<Boolean> cir) {
    for (final Recipe<ItemStack> recipe : CUSTOM_POTION_RECIPES) {
      if (!recipe.getIngredient().test(ingredient)) {
        continue;
      }
      final StatusEffectInstance check =
          PotionUtil.getCustomPotionEffects(recipe.getInput()).get(0);
      final StatusEffectInstance effect = PotionUtil.getCustomPotionEffects(input).get(0);
      if (effect.compareTo(check) != 0) {
        continue;
      }
      if (!recipe.getInput().equals(input)) {
        continue;
      }
      cir.setReturnValue(true);
    }
    cir.setReturnValue(false);
  }

  @Inject(method = "hasPotionRecipe", at = @At("HEAD"), cancellable = true)
  private static void hasPotionRecipeCheck(
          final ItemStack input, final ItemStack ingredient, final CallbackInfoReturnable<Boolean> cir) {
    checkRecipe(input, ingredient, cir);
  }

  @Inject(
      method =
          "craft(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;)Lnet/minecraft/item/ItemStack;",
      at =
          @At(
              value = "INVOKE",
              target = "Lnet/minecraft/item/ItemStack;getItem()Lnet/minecraft/item/Item;",
              shift = At.Shift.AFTER),
      cancellable = true)
  private static void customCraft(
      @NotNull final ItemStack ingredient,
      @NotNull final ItemStack input,
      @NotNull final CallbackInfoReturnable<ItemStack> cir) {
    if (!(input.getItem() instanceof PotionItem)) {
      return;
    }
    for (final Recipe<ItemStack> recipe : CUSTOM_POTION_RECIPES) {
      if (!recipe.getIngredient().test(ingredient)) {
        continue;
      }
      final StatusEffectInstance check =
          PotionUtil.getCustomPotionEffects(recipe.getInput()).get(0);
      final StatusEffectInstance effect = PotionUtil.getCustomPotionEffects(input).get(0);
      if (effect.compareTo(check) != 0) {
        continue;
      }
      if (!recipe.getInput().equals(input)) {
        continue;
      }
      cir.setReturnValue(recipe.getOutput().copy());
    }
  }
}
