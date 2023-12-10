package io.github.pulsebeat02.smpearth.mixin;

import net.minecraft.SharedConstants;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(SharedConstants.class)
public final class SharedConstantsMixin {

  /**
   * @author PulseBeat_02
   * @reason defers non-essential initialization
   */
  @Overwrite
  public static void enableDataFixerOptimization() {}
}
