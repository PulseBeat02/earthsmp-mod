package io.github.pulsebeat02.smpearth.events;

import io.github.pulsebeat02.smpearth.Continent;
import io.github.pulsebeat02.smpearth.utils.Utils;
import java.util.*;
import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.TeleportTarget;
import org.jetbrains.annotations.NotNull;

public final class PlayerTeleportationHandler {

  private static final Queue<UUID> PLAYER_QUEUE;

  static {
    PLAYER_QUEUE = new ArrayDeque<>();
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
    final ServerWorld world = player.getServerWorld();
    final BlockPos pos = player.getBlockPos();
    if (!world.isChunkLoaded(pos)) {
      return;
    }
    final BlockPos randPos = createRandomPosition();
    teleportPlayer(randPos, player);
    PLAYER_QUEUE.poll();
  }

  private static void teleportPlayer(
      @NotNull final BlockPos randPos, @NotNull final ServerPlayerEntity player) {
    final ServerWorld world = player.getServerWorld();
    final Vec3d vec = new Vec3d(randPos.getX(), randPos.getY(), randPos.getZ());
    final Vec3d velocity = new Vec3d(0, 0, 0);
    final TeleportTarget target = new TeleportTarget(vec, velocity, 0, 0);
    FabricDimensions.teleport(player, world, target);
  }

  private static @NotNull BlockPos createRandomPosition() {
    final Continent random = Utils.getRandomEnum(Continent.class);
    return Utils.generateRandomPosition(random);
  }

  public static void addPlayerToQueue(@NotNull final UUID uuid) {
    PLAYER_QUEUE.add(uuid);
  }
}
