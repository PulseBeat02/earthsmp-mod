package io.github.pulsebeat02.earthsmp.drops;

import io.github.pulsebeat02.earthsmp.Continent;
import io.github.pulsebeat02.earthsmp.EarthSMPMod;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.loot.LootManager;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.List;
import java.util.SplittableRandom;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

public abstract class LootCrate {

  private static final SplittableRandom RANDOM;
  private static final ExecutorService SERVICE;

  static {
    RANDOM = new SplittableRandom();
    SERVICE = Executors.newCachedThreadPool();
  }

  private @NotNull final EarthSMPMod mod;
  private @NotNull final Continent continent;
  private @NotNull final Identifier lootTableId;
  private final int count;

  public LootCrate(
      @NotNull final EarthSMPMod mod,
      @NotNull final Continent continent,
      @NotNull final Identifier lootTableId,
      @NotNull final int count) {
    this.mod = mod;
    this.continent = continent;
    this.lootTableId = lootTableId;
    this.count = count;
    this.spawn();
  }

  public @NotNull BlockPos generateRandomPosition() {
    final Pair<Integer, Integer> topLeft = this.continent.getTopLeft();
    final Pair<Integer, Integer> bottomRight = this.continent.getBottomRight();
    outer:
    while (true) {
      final int x = this.generateRandomX(topLeft, bottomRight);
      final int z = this.generateRandomZ(topLeft, bottomRight);
      final MinecraftServer server = this.mod.getServer();
      final World world = server.getOverworld();
      BlockPos pos = null;
      for (int y = 255; y > -60; y--) {
        pos = new BlockPos(x, y, z);
        final BlockState state = world.getBlockState(pos);
        if (state.isAir()) {
          continue;
        }
        if (state.isLiquid()) {
          continue outer;
        }
      }
      return pos.add(0, 1, 0);
    }
  }

  private int generateRandomX(
      @NotNull final Pair<Integer, Integer> topLeft,
      @NotNull final Pair<Integer, Integer> bottomRight) {
    return (int) (RANDOM.nextDouble() * (bottomRight.getLeft() - topLeft.getLeft()))
        + topLeft.getLeft();
  }

  private int generateRandomZ(
      @NotNull final Pair<Integer, Integer> topLeft,
      @NotNull final Pair<Integer, Integer> bottomRight) {
    return (int) (RANDOM.nextDouble() * (bottomRight.getRight() - topLeft.getRight()))
        + topLeft.getRight();
  }

  public void spawn() {
    CompletableFuture.runAsync(this::checks, SERVICE);
  }

  private void checks() {
    while (!this.condition()) {}
    for (int i = 0; i < this.count; i++) {
      final BlockPos pos = this.generateRandomPosition();
      this.spawnChest(pos);
    }
  }

  public abstract boolean condition();

  public void spawnChest(@NotNull final BlockPos pos) {
    final MinecraftServer server = this.mod.getServer();
    final World world = server.getOverworld();
    final BlockEntity blockEntity = world.getBlockEntity(pos);
    if (blockEntity instanceof final ChestBlockEntity chestEntity) {
      chestEntity.setLootTable(this.lootTableId, world.random.nextLong());
    }
    this.broadcastMessage(pos);
  }

  public void broadcastMessage(@NotNull final BlockPos pos) {
    final MinecraftServer server = this.mod.getServer();
    final PlayerManager manager = server.getPlayerManager();
    final List<ServerPlayerEntity> players = manager.getPlayerList();
    for (final ServerPlayerEntity player : players) {
      final String raw =
          "A loot crate has spawned at %d, %d, %d!".formatted(pos.getX(), pos.getY(), pos.getZ());
      final TextContent literal = new LiteralTextContent(raw);
      final MutableText text = MutableText.of(literal);
      final TextColor color = TextColor.fromFormatting(Formatting.GOLD);
      final Style style = Style.EMPTY.withBold(true).withColor(color);
      text.setStyle(style);
      player.sendMessage(text);
    }
  }
}
