package io.github.pulsebeat02.smpearth.events;

import io.github.pulsebeat02.smpearth.utils.Utils;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public final class PlayerTeleportationHandler {

  private static final Queue<UUID> PLAYER_QUEUE;

  static {
    PLAYER_QUEUE = new ConcurrentLinkedQueue<>();
  }

  public PlayerTeleportationHandler() {
    this.register();
  }

  private void register() {
    ServerTickEvents.END_SERVER_TICK.register(this::handleServerTick);
  }

  private void handleServerTick(@NotNull final MinecraftServer server) {
    if (PLAYER_QUEUE.isEmpty()) {
      return;
    }
    final UUID uuid = PLAYER_QUEUE.peek();
    final PlayerManager manager = server.getPlayerManager();
    final ServerPlayerEntity player = manager.getPlayer(uuid);
    if (player == null) {
      return;
    }
    final World world = player.getWorld();
    final BlockPos pos = player.getBlockPos();
    if (!world.isChunkLoaded(pos)) {
      return;
    }
    CompletableFuture.supplyAsync(Utils::generateRandomPlayerPosition)
        .thenAccept(rand -> this.teleport(player, rand))
        .thenRun(PLAYER_QUEUE::poll);
  }

  private void teleport(@NotNull final ServerPlayerEntity player, @NotNull final BlockPos pos) {
    player.teleport(pos.getX(), pos.getY(), pos.getZ());
    player.setSpawnPoint(World.OVERWORLD, pos, 0, false, false);
  }

  public static void addPlayerToQueue(@NotNull final UUID uuid) {
    if (PLAYER_QUEUE.contains(uuid)) {
      return;
    }
    PLAYER_QUEUE.add(uuid);
  }
}
