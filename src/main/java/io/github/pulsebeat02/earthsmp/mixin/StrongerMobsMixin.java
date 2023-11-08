package io.github.pulsebeat02.earthsmp.mixin;

import io.github.pulsebeat02.earthsmp.Continent;
import io.github.pulsebeat02.earthsmp.utils.Utils;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerWorld.class)
public final class StrongerMobsMixin {
  @Inject(at = @At("TAIL"), method = "spawnEntity")
  private void spawnBuffedAfricaEntity(
      @NotNull final Entity entity, @NotNull final CallbackInfoReturnable<Boolean> cir) {
    if (!(entity instanceof final HostileEntity hostile)) {
      return;
    }
    if (entity.hasCustomName()) {
      return;
    }
    final Continent continent = Continent.AF;
    final BlockPos pos = entity.getBlockPos();
    final int x = pos.getX();
    final int z = pos.getZ();
    if (!Utils.withinContinent(continent, x, z)) {
      return;
    }
    this.modifyCreeper(hostile);
    this.modifyEntity(hostile);
  }

  @Unique
  private void modifyCreeper(@NotNull final HostileEntity entity) {
    if (entity instanceof final CreeperEntity creeper) {
      final NbtCompound tag = new NbtCompound();
      tag.putBoolean("powered", true);
      tag.putShort("Fuse", (short) 1);
      tag.putByte("ExplosionRadius", (byte) 3);
      creeper.readCustomDataFromNbt(tag);
    }
  }

  @Unique
  private void modifyEntity(@NotNull final HostileEntity entity) {
    this.addStatusEffects(entity);
    this.setLootOptions(entity);
    this.modifyOtherAttributes(entity);
  }

  @Unique
  private void modifyOtherAttributes(@NotNull final HostileEntity entity) {
    entity.setMovementSpeed(entity.getMovementSpeed() * 3);
  }

  @Unique
  private void setLootOptions(@NotNull final HostileEntity entity) {
    entity.setCanPickUpLoot(true);
    for (final EquipmentSlot slot : EquipmentSlot.values()) {
      entity.setEquipmentDropChance(slot, 0);
    }
    this.setArmor(entity);
  }

  @Unique
  private void setArmor(@NotNull final HostileEntity entity) {
    entity.equipStack(EquipmentSlot.HEAD, this.enchantArmor(new ItemStack(Items.NETHERITE_HELMET)));
    entity.equipStack(
        EquipmentSlot.CHEST, this.enchantArmor(new ItemStack(Items.NETHERITE_CHESTPLATE)));
    entity.equipStack(
        EquipmentSlot.LEGS, this.enchantArmor(new ItemStack(Items.NETHERITE_LEGGINGS)));
    entity.equipStack(EquipmentSlot.FEET, this.enchantArmor(new ItemStack(Items.NETHERITE_BOOTS)));
  }

  @Unique
  private @NotNull ItemStack enchantArmor(@NotNull final ItemStack stack) {
    stack.addEnchantment(Enchantments.THORNS, 3);
    final NbtCompound tag = stack.getOrCreateNbt();
    tag.putBoolean("Unbreakable", true);
    return stack;
  }

  @Unique
  private void addStatusEffects(@NotNull final HostileEntity entity) {
    final StatusEffectInstance[] effects =
        new StatusEffectInstance[] {
          new StatusEffectInstance(StatusEffects.HEALTH_BOOST, Integer.MAX_VALUE, 4),
          new StatusEffectInstance(StatusEffects.JUMP_BOOST, Integer.MAX_VALUE, 2),
          new StatusEffectInstance(StatusEffects.RESISTANCE, Integer.MAX_VALUE, 0),
          new StatusEffectInstance(StatusEffects.SPEED, Integer.MAX_VALUE, 3)
        };
    for (final StatusEffectInstance effect : effects) {
      entity.addStatusEffect(effect);
    }
  }
}
