package io.github.pulsebeat02.earthsmp.nerfs;

import io.github.pulsebeat02.earthsmp.Continent;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class MinerFatigueNerf implements ServerTickEvents.StartTick {

  @Override
  public void onStartTick(@NotNull final MinecraftServer server) {
    final PlayerManager manager = server.getPlayerManager();
    final List<ServerPlayerEntity> players = manager.getPlayerList();
    final Continent continent = Continent.AF;
    final Pair<Integer, Integer> topLeft = continent.getTopLeft();
    final Pair<Integer, Integer> bottomRight = continent.getBottomRight();
    for (final ServerPlayerEntity player : players) {
      final BlockPos pos = player.getBlockPos();
      final int x = pos.getX();
      final int z = pos.getZ();
      if (this.checkXCoordinate(topLeft, bottomRight, x)
          && this.checkZCoordinate(topLeft, bottomRight, z)) {
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE, 10, 1));
      }
    }
  }

  private boolean checkXCoordinate(
      @NotNull final Pair<Integer, Integer> topLeft,
      @NotNull final Pair<Integer, Integer> bottomRight,
      final int x) {
    return x > topLeft.getLeft() && x < bottomRight.getLeft();
  }

  private boolean checkZCoordinate(
      @NotNull final Pair<Integer, Integer> topLeft,
      @NotNull final Pair<Integer, Integer> bottomRight,
      final int z) {
    return z > topLeft.getRight() && z < bottomRight.getRight();
  }
}
