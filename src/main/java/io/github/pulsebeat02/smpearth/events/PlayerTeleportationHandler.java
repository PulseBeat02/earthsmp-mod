package io.github.pulsebeat02.smpearth.events;

import io.github.pulsebeat02.smpearth.utils.Utils;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public final class PlayerTeleportationHandler {

  private static @NotNull final Queue<UUID> PLAYER_QUEUE;

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

    final Optional<ServerPlayerEntity> player = this.getPlayerEntity(server);
    if (player.isEmpty()) {
      return;
    }

    final ServerPlayerEntity entity = player.get();
    if (!isChunkLoaded(entity)) {
      return;
    }

    final BlockPos pos = Utils.generateRandomPlayerPosition();
    this.teleport(entity, pos);
    PLAYER_QUEUE.poll();
  }

  private @NotNull Optional<ServerPlayerEntity> getPlayerEntity(
      @NotNull final MinecraftServer server) {
    final UUID uuid = PLAYER_QUEUE.peek();
    final PlayerManager manager = server.getPlayerManager();
    final ServerPlayerEntity player = manager.getPlayer(uuid);
    return Optional.ofNullable(player);
  }

  private static boolean isChunkLoaded(@NotNull final ServerPlayerEntity player) {
    final World world = player.getWorld();
    final BlockPos pos = player.getBlockPos();
    return world.isChunkLoaded(pos);
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
