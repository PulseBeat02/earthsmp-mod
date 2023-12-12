package io.github.pulsebeat02.smpearth.mixin;

import io.github.pulsebeat02.smpearth.Continent;
import io.github.pulsebeat02.smpearth.utils.Utils;
import java.util.Optional;
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

  @Unique private static final long DELAY;

  static {
    DELAY = 60_000L;
  }

  @Unique private long ms = -1;

  @Inject(at = @At("HEAD"), method = "playerTick")
  private void applyMinerFatigueEffect(@NotNull final CallbackInfo ci) {

    if (!this.checkDelay()) {
      return;
    }

    final Optional<ServerPlayerEntity> entity = this.checkBoundaries();
    entity.ifPresent(this::applyMinerFatigue);
  }

  @Unique
  private void applyMinerFatigue(@NotNull final ServerPlayerEntity entity) {
    entity.addStatusEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE, 60, 1));
  }

  @Unique
  private @NotNull Optional<ServerPlayerEntity> checkBoundaries() {
    final ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;
    final BlockPos pos = player.getBlockPos();
    final Continent continent = Continent.AF;
    final int x = pos.getX();
    final int z = pos.getZ();
    return Utils.withinContinent(continent, x, z) ? Optional.of(player) : Optional.empty();
  }

  @Unique
  private boolean checkDelay() {
    final long now = System.currentTimeMillis();
    if (this.ms == -1 || now - this.ms < DELAY) {
      this.ms = now;
      return false;
    }
    return true;
  }
}
