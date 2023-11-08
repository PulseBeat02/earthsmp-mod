package io.github.pulsebeat02.earthsmp.drops;

import io.github.pulsebeat02.earthsmp.Continent;
import io.github.pulsebeat02.earthsmp.EarthSMPMod;
import io.github.pulsebeat02.earthsmp.utils.Utils;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract sealed class LootCrate permits AncientCityCrate, TotemCrate, EndCityCrate {

  private static final ExecutorService SERVICE;

  static {
    SERVICE = Executors.newCachedThreadPool();
    Runtime.getRuntime().addShutdownHook(new Thread(SERVICE::shutdown));
  }

  private @NotNull final Continent continent;
  private @Nullable Identifier lootTableId;
  private @Nullable ItemStack stack;
  private final int count;

  public LootCrate(
      @NotNull final Continent continent, @NotNull final Identifier lootTableId, final int count) {
    this(continent, count);
    this.lootTableId = lootTableId;
  }

  public LootCrate(
      @NotNull final Continent continent, @NotNull final ItemStack stack, final int count) {
    this(continent, count);
    this.stack = stack;
  }

  private LootCrate(@NotNull final Continent continent, final int count) {
    this.continent = continent;
    this.count = count;
    this.spawn();
  }

  private void spawn() {
    CompletableFuture.runAsync(this::checks, SERVICE);
  }

  private void checks() {
    this.block();
    for (int i = 0; i < this.count; i++) {
      final BlockPos pos = Utils.generateRandomPosition(this.continent);
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
    final MinecraftServer server = EarthSMPMod.getServer();
    final World world = server.getOverworld();
    final BlockEntity blockEntity = world.getBlockEntity(pos);
    if (blockEntity instanceof final ChestBlockEntity chestEntity) {
      if (this.lootTableId == null) {
        chestEntity.setStack(13, this.stack);
      } else {
        chestEntity.setLootTable(this.lootTableId, world.random.nextLong());
      }
    }
  }

  private void waitTime() {
    try {
      Thread.sleep(15 * 60 * 1000L);
    } catch (final InterruptedException ignored) {
    }
  }

  private void broadcastMessage(@NotNull final BlockPos pos) {
    final MinecraftServer server = EarthSMPMod.getServer();
    final PlayerManager manager = server.getPlayerManager();
    final List<ServerPlayerEntity> players = manager.getPlayerList();
    for (final PlayerEntity player : players) {
      final String raw =
          "A loot crate will spawn at %d, %d, %d in 15 minutes!"
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
