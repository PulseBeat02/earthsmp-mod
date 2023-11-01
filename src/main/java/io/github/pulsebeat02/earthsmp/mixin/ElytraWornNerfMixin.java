package io.github.pulsebeat02.earthsmp.mixin;

import net.fabricmc.fabric.api.entity.event.v1.FabricElytraItem;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(FabricElytraItem.class)
public class ElytraWornNerfMixin {

  /**
   * @author PulseBeat_02
   * @reason implements proper Elytra nerf
   */
  @Overwrite
  public void doVanillaElytraTick(
      @NotNull final LivingEntity entity, @NotNull final ItemStack chestStack) {
    final int nextRoll = entity.getRoll() + 1;
    if (!entity.getWorld().isClient && nextRoll % 10 == 0) {
      if ((nextRoll / 10) % 2 == 0) {
        chestStack.damage(4, entity, p -> p.sendEquipmentBreakStatus(EquipmentSlot.CHEST));
      }
      entity.emitGameEvent(GameEvent.ELYTRA_GLIDE);
    }
  }
}
