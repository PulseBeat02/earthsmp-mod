package io.github.pulsebeat02.earthsmp.mixin;

import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(FireworkRocketEntity.class)
public final class FireworkNerfMixin {
  @ModifyArg(
      method = "tick",
      at =
          @At(
              value = "INVOKE",
              target =
                  "Lnet/minecraft/entity/LivingEntity;setVelocity(Lnet/minecraft/util/math/Vec3d;)V",
              ordinal = 0))
  private @NotNull Vec3d nerf(@NotNull final Vec3d vec3) {
    return vec3.multiply(10E-5);
  }
}
