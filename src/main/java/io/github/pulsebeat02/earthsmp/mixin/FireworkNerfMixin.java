package io.github.pulsebeat02.earthsmp.mixin;

import net.minecraft.entity.projectile.FireworkRocketEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(FireworkRocketEntity.class)
public final class FireworkNerfMixin {
  @ModifyConstant(method = "tick", constant = @Constant(doubleValue = 1.5D))
  private double nerf(final double constant) {
    return 0.9D;
  }
}
