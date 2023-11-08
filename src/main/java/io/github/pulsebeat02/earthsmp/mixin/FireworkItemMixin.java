package io.github.pulsebeat02.earthsmp.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FireworkRocketEntity.class)
public final class FireworkItemMixin {

  @Shadow private @Nullable LivingEntity shooter;

  @Inject(
      method = "tick",
      at =
          @At(
              value = "INVOKE",
              target =
                  "Lnet/minecraft/entity/LivingEntity;getRotationVector()Lnet/minecraft/util/math/Vec3d;"))
  public void nerfShooterVelocity(@NotNull final CallbackInfo ci) {
    final Vec3d rotation = this.shooter.getRotationVector().multiply(0.5);
    final Vec3d velocity = this.shooter.getVelocity().multiply(0.5);
    this.shooter.setVelocity(rotation);
    this.shooter.setVelocity(velocity);
  }
}
