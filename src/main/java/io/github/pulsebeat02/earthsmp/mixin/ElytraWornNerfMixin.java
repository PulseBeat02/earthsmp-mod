package io.github.pulsebeat02.earthsmp.mixin;

import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(LivingEntity.class)
public abstract class ElytraWornNerfMixin {
  
  @ModifyConstant(method = "tickFallFlying", constant = @Constant(intValue = 1, ordinal = 2))
  private int nerf(final int constant) {
    return 3;
  }
}
