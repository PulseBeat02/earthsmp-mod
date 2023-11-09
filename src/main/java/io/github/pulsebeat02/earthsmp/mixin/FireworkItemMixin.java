package io.github.pulsebeat02.earthsmp.mixin;

import net.minecraft.entity.projectile.FireworkRocketEntity;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FireworkRocketEntity.class)
public final class FireworkItemMixin {

  @Shadow private int lifeTime;

  @Inject(
      method = "<init>(Lnet/minecraft/world/World;DDDLnet/minecraft/item/ItemStack;)V",
      at = @At("TAIL"))
  public void nerfLifeSpan(@NotNull final CallbackInfo ci) {
    this.lifeTime /= 5;
  }
}
