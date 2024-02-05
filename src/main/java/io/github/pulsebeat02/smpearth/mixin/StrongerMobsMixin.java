package io.github.pulsebeat02.smpearth.mixin;

import static java.lang.Integer.MAX_VALUE;
import static net.minecraft.entity.effect.StatusEffects.*;
import static net.minecraft.item.Items.*;

import io.github.pulsebeat02.smpearth.Continent;
import io.github.pulsebeat02.smpearth.utils.Utils;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerWorld.class)
public final class StrongerMobsMixin {

  @Unique private static @NotNull final Map<EquipmentSlot, ItemStack> ARMOR_PROVIDER;
  @Unique private static @NotNull final Collection<StatusEffectInstance> POTION_EFFECTS;

  static {
    ARMOR_PROVIDER =
        Map.of(
            EquipmentSlot.CHEST, createArmor(DIAMOND_CHESTPLATE),
            EquipmentSlot.FEET, createArmor(DIAMOND_BOOTS));
    POTION_EFFECTS = List.of(new StatusEffectInstance(SPEED, MAX_VALUE, 3));
  }

  @Unique
  private static @NotNull ItemStack createArmor(@NotNull final Item item) {
    final ItemStack stack = new ItemStack(item);
    final NbtCompound tag = stack.getOrCreateNbt();
    stack.addEnchantment(Enchantments.THORNS, 1);
    tag.putBoolean("Unbreakable", true);
    return stack;
  }

  @Inject(at = @At("TAIL"), method = "spawnEntity")
  private void spawnBuffedAfricaEntity(
      @NotNull final Entity entity, @NotNull final CallbackInfoReturnable<Boolean> cir) {

    if (!(entity instanceof final HostileEntity hostile)) {
      return;
    }

    if (entity.isInvulnerable()) {
      return;
    }

    if (!checkBounds(entity)) {
      return;
    }

    this.modifyCreeper(hostile);
    this.modifyEntity(hostile);
  }

  @Unique
  private static boolean checkBounds(@NotNull final Entity entity) {
    final Continent continent = Continent.AF;
    final BlockPos pos = entity.getBlockPos();
    final int x = pos.getX();
    final int z = pos.getZ();
    final World world = entity.getWorld();
    return isOverworld(world) && Utils.withinContinent(continent, x, z);
  }

  @Unique
  private static boolean isOverworld(@NotNull final World world) {
    return world.getRegistryKey() == World.OVERWORLD;
  }

  @Unique
  private void modifyCreeper(@NotNull final HostileEntity entity) {
    if (entity instanceof final CreeperEntity creeper) {
      final NbtCompound tag = new NbtCompound();
      tag.putBoolean("powered", true);
      tag.putShort("Fuse", (short) 3);
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
    for (final EquipmentSlot slot : EquipmentSlot.values()) {
      entity.setEquipmentDropChance(slot, 0);
    }
    this.setArmor(entity);
  }

  @Unique
  private void setArmor(@NotNull final HostileEntity entity) {
    ARMOR_PROVIDER.forEach((key, value) -> entity.equipStack(key, value.copy()));
  }

  @Unique
  private void addStatusEffects(@NotNull final HostileEntity entity) {
    POTION_EFFECTS.forEach(entity::addStatusEffect);
  }
}
