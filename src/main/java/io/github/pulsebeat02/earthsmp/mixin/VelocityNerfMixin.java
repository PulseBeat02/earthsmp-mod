package io.github.pulsebeat02.earthsmp.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class VelocityNerfMixin {

  @Inject(
          method = "travel",
          at =
          @At(
                  value = "INVOKE",
                  target = "Lnet/minecraft/entity/LivingEntity;limitFallDistance()V",
                  shift = At.Shift.AFTER))
  private void nerfElytraVelocity(
          @NotNull final Vec3d movementInput, @NotNull final CallbackInfo ci) {
    final LivingEntity entity = (LivingEntity) (Object) this;
    entity.setVelocity(entity.getVelocity().multiply(0.05));
  }
}
