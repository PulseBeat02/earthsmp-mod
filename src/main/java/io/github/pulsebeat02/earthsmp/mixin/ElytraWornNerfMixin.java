package io.github.pulsebeat02.earthsmp.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(LivingEntity.class)
public abstract class ElytraWornNerfMixin extends Entity {

  @Shadow protected int roll;

  protected ElytraWornNerfMixin(
      @NotNull final EntityType<? extends LivingEntity> entityType, @NotNull final World world) {
    super(entityType, world);
  }

  /**
   * @author PulseBeat_02
   * @reason elytra durability nerf
   */
  @Overwrite
  private void tickFallFlying() {
    final LivingEntity entity = (LivingEntity) (Object) this;
    boolean bl = this.getFlag(Entity.FALL_FLYING_FLAG_INDEX);
    if (bl
        && !entity.isOnGround()
        && !entity.hasVehicle()
        && !entity.hasStatusEffect(StatusEffects.LEVITATION)) {
      final ItemStack itemStack = entity.getEquippedStack(EquipmentSlot.CHEST);
      if (itemStack.isOf(Items.ELYTRA) && ElytraItem.isUsable(itemStack)) {
        bl = true;
        final int i = this.roll + 1;
        if (!entity.getWorld().isClient && i % 10 == 0) {
          final int j = i / 10;
          if (j % 2 == 0) {
            itemStack.damage(
                1, entity, player -> player.sendEquipmentBreakStatus(EquipmentSlot.CHEST));
          }
          entity.emitGameEvent(GameEvent.ELYTRA_GLIDE);
        }
      } else {
        bl = false;
      }
    } else {
      bl = false;
    }
    if (!this.getWorld().isClient) {
      this.setFlag(Entity.FALL_FLYING_FLAG_INDEX, bl);
    }
  }
}
