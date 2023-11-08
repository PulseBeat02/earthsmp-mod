package io.github.pulsebeat02.earthsmp.mixin;

import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(FireworkRocketEntity.class)
public final class FireworkNerfMixin {
  @ModifyArg(
      method = "tick",
      at =
          @At(
              value = "INVOKE",
              target =
                  "Lnet/minecraft/entity/LivingEntity;setVelocity(Lnet/minecraft/util/math/Vec3d;)V"))
  private @NotNull Vec3d nerfShooterVelocity(@NotNull final Vec3d vec) {
    return vec.multiply(0.0005);
  }
}
