package io.github.pulsebeat02.smpearth.events;

import io.github.pulsebeat02.smpearth.Continent;
import io.github.pulsebeat02.smpearth.utils.Utils;
import java.util.*;
import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;
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
    if (!PLAYER_QUEUE.isEmpty()) {
      final UUID uuid = PLAYER_QUEUE.poll();
      final ServerPlayerEntity player = server.getPlayerManager().getPlayer(uuid);
      final Continent random = Utils.getRandomEnum(Continent.class);
      final BlockPos pos = Utils.generateRandomPosition(random);
      final ServerWorld world = player.getServer().getWorld(World.OVERWORLD);
      final Vec3d vec = new Vec3d(pos.getX(), pos.getY(), pos.getZ());
      final Vec3d velocity = new Vec3d(0, 0, 0);
      final TeleportTarget target = new TeleportTarget(vec, velocity, 0, 0);
      FabricDimensions.teleport(player, world, target);
      System.out.printf("Teleportation State: %s", player.isInTeleportationState());
    }
  }

  public static void addPlayerToQueue(@NotNull final UUID uuid) {
    PLAYER_QUEUE.add(uuid);
  }
}
