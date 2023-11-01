package io.github.pulsebeat02.earthsmp.mixin;

import io.github.pulsebeat02.earthsmp.Continent;
import io.github.pulsebeat02.earthsmp.utils.Utils;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class MinerFatigueNerfMixin {

  @Shadow public @NotNull ServerPlayerEntity player;

  @Unique private long ms;

  public MinerFatigueNerfMixin() {
    this.ms = System.currentTimeMillis();
  }

  @Inject(at = @At("TAIL"), method = "syncWithPlayerPosition")
  public void syncWithPlayerPosition(@NotNull final CallbackInfo ci) {
    final long now = System.currentTimeMillis();
    if (now - this.ms < 10000L) {
      this.ms = now;
      return;
    }
    final ServerPlayerEntity player = this.player;
    final BlockPos pos = player.getBlockPos();
    final Continent continent = Continent.AF;
    final int x = pos.getX();
    final int z = pos.getZ();
    if (Utils.withinContinent(continent, x, z)) {
      player.addStatusEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE, 1, 1));
    }
  }
}
