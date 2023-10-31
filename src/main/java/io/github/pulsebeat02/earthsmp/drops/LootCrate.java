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
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

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
  private @NotNull final BlockPos pos;
  private final int count;

  public LootCrate(
      @NotNull final EarthSMPMod mod,
      @NotNull final Continent continent,
      @NotNull final Identifier lootTableId,
      @NotNull final int count) {
    this.mod = mod;
    this.continent = continent;
    this.lootTableId = lootTableId;
    this.pos = this.generateRandomPosition();
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
      this.spawnChest();
    }
  }

  public abstract boolean condition();

  public void spawnChest() {
    final MinecraftServer server = this.mod.getServer();
    final World world = server.getOverworld();
    final BlockEntity blockEntity = world.getBlockEntity(this.pos);
    if (blockEntity instanceof final ChestBlockEntity chestEntity) {
      chestEntity.setLootTable(this.lootTableId, world.random.nextLong());
    }
  }
}
