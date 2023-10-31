package io.github.pulsebeat02.earthsmp.listeners;

import static io.github.pulsebeat02.earthsmp.EarthSMPMod.*;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class PlayerMovementTickListener implements ServerTickEvents.StartTick {

  @Override
  public void onStartTick(@NotNull final MinecraftServer server) {
    final PlayerManager manager = server.getPlayerManager();
    final List<ServerPlayerEntity> players = manager.getPlayerList();
    for (final ServerPlayerEntity player : players) {
      final BlockPos pos = player.getBlockPos();
      final int x = pos.getX();
      final int z = pos.getZ();
      if (this.checkBondsX(x)) {
        player.teleport(x * -1, pos.getY(), z);
      }
      if (this.checkBondsZ(z)) {
        player.teleport(x, pos.getY(), z * -1);
      }
    }
  }

  private boolean checkBondsX(final int x) {
    return Math.abs(x) >= (MAX_X - 50);
  }

  private boolean checkBondsZ(final int z) {
    return Math.abs(z) >= (MAX_Z - 25);
  }
}
