package io.github.pulsebeat02.earthsmp.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(LivingEntity.class)
public abstract class VelocityNerfMixin {

  @ModifyArg(
      method = "travel",
      at =
          @At(
              value = "INVOKE",
              target =
                  "Lnet/minecraft/entity/LivingEntity;setVelocity(Lnet/minecraft/util/math/Vec3d;)V",
              ordinal = 6))
  private @NotNull Vec3d modifyVelocity(@NotNull final Vec3d vec3) {
    return vec3.multiply(10E-5);
  }
}
