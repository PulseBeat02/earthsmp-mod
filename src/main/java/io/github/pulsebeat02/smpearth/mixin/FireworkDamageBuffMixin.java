package io.github.pulsebeat02.smpearth.mixin;

import net.minecraft.entity.projectile.FireworkRocketEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(FireworkRocketEntity.class)
public final class FireworkDamageBuffMixin {

  @ModifyArg(
      method = "explode",
      at =
          @At(
              value = "INVOKE",
              target =
                  "Lnet/minecraft/entity/LivingEntity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"),
      index = 1)
  private float changeDamage(final float g) {
    return g * 1.7f;
  }
}
