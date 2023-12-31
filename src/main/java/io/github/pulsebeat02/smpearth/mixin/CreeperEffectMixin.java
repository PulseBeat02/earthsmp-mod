package io.github.pulsebeat02.smpearth.mixin;

import io.github.pulsebeat02.smpearth.Continent;
import io.github.pulsebeat02.smpearth.utils.Utils;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CreeperEntity.class)
public final class CreeperEffectMixin extends HostileEntity {

  private CreeperEffectMixin(
      @NotNull final EntityType<? extends HostileEntity> entityType, @NotNull final World world) {
    super(entityType, world);
  }

  @Inject(at = @At("HEAD"), method = "spawnEffectsCloud", cancellable = true)
  private void removeEffectsCloud(@NotNull final CallbackInfo ci) {
    final BlockPos pos = this.getBlockPos();
    final int x = pos.getX();
    final int z = pos.getZ();
    final Continent continent = Continent.AF;
    if (Utils.withinContinent(continent, x, z)) {
      ci.cancel();
    }
  }
}
