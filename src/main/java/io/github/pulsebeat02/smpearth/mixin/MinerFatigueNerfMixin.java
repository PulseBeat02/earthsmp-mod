package io.github.pulsebeat02.smpearth.mixin;

import io.github.pulsebeat02.smpearth.Continent;
import io.github.pulsebeat02.smpearth.utils.Utils;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public final class MinerFatigueNerfMixin {

  @Unique private long ms;

  public MinerFatigueNerfMixin() {
    this.ms = System.currentTimeMillis();
  }

  @Inject(at = @At("HEAD"), method = "playerTick")
  private void applyMinerFatigueEffect(@NotNull final CallbackInfo ci) {
    final long now = System.currentTimeMillis();
    if (now - this.ms < 60000L) {
      this.ms = now;
      return;
    }
    final ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;
    final BlockPos pos = player.getBlockPos();
    final Continent continent = Continent.AF;
    final int x = pos.getX();
    final int z = pos.getZ();
    if (!Utils.withinContinent(continent, x, z)) {
      return;
    }
    player.addStatusEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE, 60, 1));
  }
}
