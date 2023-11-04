package io.github.pulsebeat02.earthsmp.table;

import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.fabricmc.fabric.api.loot.v2.LootTableSource;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Items;
import net.minecraft.loot.LootManager;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.entry.LeafEntry;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public final class EmeraldLootTable {

  public EmeraldLootTable() {
    this.modifyLootTable();
  }

  public void modifyLootTable() {
    LootTableEvents.MODIFY.register(this::modifyEvokerLootTable);
    LootTableEvents.MODIFY.register(this::modifyVindicatorLootTable);
  }

  private void modifyEvokerLootTable(
      @NotNull final ResourceManager resourceManager,
      @NotNull final LootManager lootManager,
      @NotNull final Identifier id,
      @NotNull final LootTable.Builder tableBuilder,
      @NotNull final LootTableSource source) {
    final Identifier table = EntityType.EVOKER.getLootTableId();
    this.removeEmeraldDrop(id, tableBuilder, source, table);
  }

  private void modifyVindicatorLootTable(
      @NotNull final ResourceManager resourceManager,
      @NotNull final LootManager lootManager,
      @NotNull final Identifier id,
      @NotNull final LootTable.Builder tableBuilder,
      @NotNull final LootTableSource source) {
    final Identifier table = EntityType.VINDICATOR.getLootTableId();
    this.removeEmeraldDrop(id, tableBuilder, source, table);
  }

  private void removeEmeraldDrop(
      @NotNull final Identifier id,
      @NotNull final LootTable.Builder tableBuilder,
      @NotNull final LootTableSource source,
      final Identifier table) {
    if (!id.equals(table)) {
      return;
    }
    if (!source.isBuiltin()) {
      return;
    }
    final LeafEntry.Builder<?> entry = ItemEntry.builder(Items.EMERALD);
    entry.weight(0);
    final LootPool.Builder builder = LootPool.builder();
    builder.rolls(UniformLootNumberProvider.create(0, 0));
    builder.with(entry);
    tableBuilder.pool(builder.build());
  }
}
