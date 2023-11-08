package io.github.pulsebeat02.earthsmp.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class VelocityNerfMixin {

  @Inject(at = @At("HEAD"), method = "move")
  private void nerfElytraVelocity(
      @NotNull final MovementType movementType,
      @NotNull final Vec3d movement,
      @NotNull final CallbackInfo ci) {
    final Entity entity = (Entity) (Object) this;
    if (!(entity instanceof final LivingEntity livingEntity)) {
      return;
    }
    if (!livingEntity.isFallFlying()) {
      return;
    }
    final ItemStack itemStack = livingEntity.getEquippedStack(EquipmentSlot.CHEST);
    if (!itemStack.isOf(Items.ELYTRA)) {
      return;
    }
    if (!ElytraItem.isUsable(itemStack)) {
      return;
    }
    movement.multiply(0.05);
  }
}
