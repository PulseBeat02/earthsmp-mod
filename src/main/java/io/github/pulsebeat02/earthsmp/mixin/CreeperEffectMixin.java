package io.github.pulsebeat02.earthsmp.mixin;

import io.github.pulsebeat02.earthsmp.Continent;
import io.github.pulsebeat02.earthsmp.utils.Utils;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CreeperEntity.class)
public final class CreeperEffectMixin {

  @Inject(at = @At("HEAD"), method = "spawnEffectsCloud", cancellable = true)
  private void spawnEffectsCloud(@NotNull final CallbackInfo ci) {
    final CreeperEntity entity = (CreeperEntity) (Object) this;
    final BlockPos pos = entity.getBlockPos();
    final int x = pos.getX();
    final int z = pos.getZ();
    final Continent continent = Continent.AF;
    if (Utils.withinContinent(continent, x, z)) {
      ci.cancel();
    }
  }
}
