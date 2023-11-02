package io.github.pulsebeat02.earthsmp.drops;

import io.github.pulsebeat02.earthsmp.Continent;
import io.github.pulsebeat02.earthsmp.EarthSMPMod;
import io.github.pulsebeat02.earthsmp.utils.Utils;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
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

public abstract sealed class LootCrate
    permits AncientCityCrate, BastionCrate, EndCityCrate, VillagerCrate {

  private static final ExecutorService SERVICE;

  static {
    SERVICE = Executors.newCachedThreadPool();
  }

  private @NotNull final Continent continent;
  private @NotNull final Identifier lootTableId;
  private final int count;

  public LootCrate(
      @NotNull final Continent continent, @NotNull final Identifier lootTableId, final int count) {
    this.continent = continent;
    this.lootTableId = lootTableId;
    this.count = count;
    this.spawn();
  }

  private void spawn() {
    CompletableFuture.runAsync(this::checks, SERVICE);
  }

  private void checks() {
    while (!this.condition()) {}
    for (int i = 0; i < this.count; i++) {
      final BlockPos pos = Utils.generateRandomPosition(this.continent);
      this.spawnChest(pos);
    }
  }

  public abstract boolean condition();

  private void spawnChest(@NotNull final BlockPos pos) {
    final MinecraftServer server = EarthSMPMod.getServer();
    final World world = server.getOverworld();
    final BlockEntity blockEntity = world.getBlockEntity(pos);
    if (blockEntity instanceof final ChestBlockEntity chestEntity) {
      chestEntity.setLootTable(this.lootTableId, world.random.nextLong());
    }
    this.broadcastMessage(pos);
  }

  private void broadcastMessage(@NotNull final BlockPos pos) {
    final MinecraftServer server = EarthSMPMod.getServer();
    final PlayerManager manager = server.getPlayerManager();
    final List<ServerPlayerEntity> players = manager.getPlayerList();
    for (final PlayerEntity player : players) {
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
