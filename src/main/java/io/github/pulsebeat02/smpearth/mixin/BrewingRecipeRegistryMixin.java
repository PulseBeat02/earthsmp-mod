package io.github.pulsebeat02.smpearth.mixin;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.recipe.BrewingRecipeRegistry;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BrewingRecipeRegistry.class)
public final class BrewingRecipeRegistryMixin {

  @Inject(method = "craft", at = @At("RETURN"), cancellable = true)
  private static void addName(
      @NotNull final ItemStack ingredient,
      @NotNull final ItemStack input,
      @NotNull final CallbackInfoReturnable<ItemStack> cir) {

    final ItemStack stack = cir.getReturnValue().copy();
    final Text name = stack.getName();
    final String text = name.getString();
    if (!isCustomPotions(text)) {
      return;
    }

    final MutableText component = createTextComponent(text);
    stack.setCustomName(component);

    final NbtCompound compound = stack.getOrCreateSubNbt("display");
    compound.put("Lore", new NbtList());

    cir.setReturnValue(stack);
  }

  @Unique
  private static @NotNull MutableText createTextComponent(@NotNull final String text) {
    final String modified = "Potion of " + getNameString(text);
    final TextContent literal = new LiteralTextContent(modified);
    final Style style = Style.EMPTY.withColor(Formatting.BLUE).withItalic(false);
    return MutableText.of(literal).setStyle(style);
  }

  @Unique
  private static boolean isCustomPotions(@NotNull final String text) {
    return text.contains(".");
  }

  @Unique
  private static @NotNull String getNameString(@NotNull final String name) {
    final int lastPeriodIndex = name.lastIndexOf('.');
    final String sub = name.substring(lastPeriodIndex + 1);
    final String[] split = sub.split("_");
    final StringBuilder sb = new StringBuilder();
    for (final String word : split) {
      final char c = Character.toUpperCase(word.charAt(0));
      sb.append(c);
      sb.append(word.substring(1));
      sb.append(" ");
    }
    return sb.toString().trim();
  }
}
