package io.github.pulsebeat02.smpearth.mixin;

import net.minecraft.screen.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(LoomScreenHandler.class)
public final class LoomScreenHandlerMixin {
  @ModifyConstant(
      method = "onContentChanged(Lnet/minecraft/inventory/Inventory;)V",
      constant = @Constant(intValue = 6))
  private int maxBannerPattern(final int orig) {
    return Integer.MAX_VALUE;
  }
}
