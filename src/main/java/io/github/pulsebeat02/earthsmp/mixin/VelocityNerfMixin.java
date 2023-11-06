package io.github.pulsebeat02.earthsmp.mixin;

import io.github.pulsebeat02.earthsmp.utils.Utils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class VelocityNerfMixin {

  @Inject(method = "tickFallFlying", at = @At("TAIL"))
  private void tickFallFlying(@NotNull final CallbackInfo ci) {
    final LivingEntity entity = (LivingEntity) (Object) this;
    final double speedLimit = 0.5D;
    final Vec3d velocity = entity.getVelocity();
    final double speed = new Vec3d(velocity.getX(), 0.0f, velocity.getZ()).lengthSquared();
    final double hScale = Math.sqrt(speedLimit / speed);
    if (hScale >= 0.0D && hScale <= 100.0D) {
      final double vScale = 1.0D;
      final Vec3d reducedSpeed =
          Utils.cloneVector(velocity).multiply(new Vec3d(hScale, vScale, hScale));
      entity.setVelocity(reducedSpeed);
    }
  }
}
