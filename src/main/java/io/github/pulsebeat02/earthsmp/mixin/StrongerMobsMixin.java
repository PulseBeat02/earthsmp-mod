package io.github.pulsebeat02.earthsmp.mixin;

import io.github.pulsebeat02.earthsmp.Continent;
import io.github.pulsebeat02.earthsmp.utils.Utils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.HostileEntity;
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
public class StrongerMobsMixin {
  @Inject(at = @At("TAIL"), method = "spawnEntity")
  public void spawnEntity(
      @NotNull final Entity entity, @NotNull final CallbackInfoReturnable<Boolean> cir) {
    final Continent continent = Continent.AF;
    final BlockPos pos = entity.getBlockPos();
    final int x = pos.getX();
    final int z = pos.getZ();
    if (!Utils.withinContinent(continent, x, z)) {
      return;
    }
    if (!(entity instanceof final HostileEntity hostile)) {
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
      tag.putByte("ExplosionRadius", (byte) 4);
      tag.putInt("Fuse", 1);
      creeper.readCustomDataFromNbt(tag);
    }
  }

  @Unique
  private void modifyEntity(@NotNull final HostileEntity entity) {
    final float hp = entity.getMaxHealth();
    entity.setHealth(hp * 3.0f);
    entity.setCanPickUpLoot(true);
    entity.setMovementSpeed(2f);
  }
}
