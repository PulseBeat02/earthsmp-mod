package io.github.pulsebeat02.smpearth.mixin;

import net.minecraft.item.BannerItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(BannerItem.class)
public final class BannerItemMixin {
  @ModifyConstant(
      method = "appendBannerTooltip(Lnet/minecraft/item/ItemStack;Ljava/util/List;)V",
      constant = @Constant(intValue = 6))
  private static int maxBannerPattern(final int orig) {
    return Integer.MAX_VALUE;
  }
}
