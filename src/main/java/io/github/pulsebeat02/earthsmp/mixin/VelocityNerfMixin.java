package io.github.pulsebeat02.earthsmp.mixin;

import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(PlayerEntity.class)
public final class VelocityNerfMixin {
  @ModifyConstant(method = "getVelocityMultiplier", constant = @Constant(floatValue = 1.0f))
  private float nerf(final float constant) {
    return 0.2f;
  }
}
