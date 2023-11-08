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
              target =
                  "Lnet/minecraft/entity/LivingEntity;setVelocity(Lnet/minecraft/util/math/Vec3d;)V",
              ordinal = 6,
              shift = At.Shift.AFTER))
  private void nerfElytraVelocity(@NotNull final Vec3d movement, @NotNull final CallbackInfo ci) {
    final LivingEntity entity = (LivingEntity) (Object) this;
    final Vec3d vec = entity.getVelocity();
    System.out.println("Old: " + vec);
    final Vec3d newVec = vec.multiply(10E-5);
    entity.setVelocity(newVec);
    System.out.println("New: " + newVec);
  }
}
