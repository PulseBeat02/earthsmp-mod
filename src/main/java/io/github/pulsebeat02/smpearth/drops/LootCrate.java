package io.github.pulsebeat02.smpearth.drops;

import io.github.pulsebeat02.smpearth.Continent;
import io.github.pulsebeat02.smpearth.SMPEarth;
import io.github.pulsebeat02.smpearth.utils.Utils;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract sealed class LootCrate permits AppleCrate, TotemCrate, SmithingCrate {

  private static final ExecutorService EXECUTOR;

  static {
    EXECUTOR = Executors.newCachedThreadPool();
    Runtime.getRuntime().addShutdownHook(new Thread(EXECUTOR::shutdown));
  }

  private @NotNull final Continent continent;
  private @NotNull final ItemStack stack;
  private final int count;

  public LootCrate(
      @NotNull final Continent continent, @NotNull final ItemStack stack, final int count) {
    this.continent = continent;
    this.count = count;
    this.stack = stack;
    this.spawn();
  }

  private void spawn() {
    CompletableFuture.runAsync(this::checks, EXECUTOR);
  }

  private void checks() {
    this.block();
    for (int i = 0; i < this.count; i++) {
      final BlockPos pos = Utils.generateRandomPositionRaw(this.continent);
      this.spawnChest(pos);
    }
  }

  private void block() {
    while (!this.condition()) {
      try {
        Thread.sleep(60000);
      } catch (final InterruptedException ignored) {
      }
    }
  }

  public abstract boolean condition();

  private void spawnChest(@NotNull final BlockPos pos) {
    this.broadcastMessage(pos);
    this.waitTime();
    final MinecraftServer server = SMPEarth.getServer();
    final World world = server.getOverworld();
    final BlockEntity blockEntity = world.getBlockEntity(pos);
    if (blockEntity instanceof final ChestBlockEntity chestEntity) {
      chestEntity.setStack(13, this.stack);
    }
  }

  private void waitTime() {
    try {
      Thread.sleep(30 * 60 * 1000L);
    } catch (final InterruptedException ignored) {
    }
  }

  private void broadcastMessage(@NotNull final BlockPos pos) {
    final MinecraftServer server = SMPEarth.getServer();
    final PlayerManager manager = server.getPlayerManager();
    final List<ServerPlayerEntity> players = manager.getPlayerList();
    for (final PlayerEntity player : players) {
      final String raw =
          "A loot crate will spawn at %d, %d, %d in 30 minutes!"
              .formatted(pos.getX(), pos.getY(), pos.getZ());
      final TextContent literal = new LiteralTextContent(raw);
      final MutableText text = MutableText.of(literal);
      final TextColor color = TextColor.fromFormatting(Formatting.GOLD);
      final Style style = Style.EMPTY.withBold(true).withColor(color);
      text.setStyle(style);
      player.sendMessage(text);
    }
  }
}
