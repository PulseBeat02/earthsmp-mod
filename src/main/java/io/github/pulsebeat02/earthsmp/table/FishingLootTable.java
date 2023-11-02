package io.github.pulsebeat02.earthsmp.table;

import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.fabricmc.fabric.api.loot.v2.LootTableSource;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.loot.LootManager;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.entry.LeafEntry;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Set;

import static java.util.Map.entry;

public final class FishingLootTable {

  private static final Map<Item, Integer> ITEMS;

  static {
    ITEMS =
        Map.ofEntries(
            entry(Items.SENTRY_ARMOR_TRIM_SMITHING_TEMPLATE, 4),
            entry(Items.DUNE_ARMOR_TRIM_SMITHING_TEMPLATE, 4),
            entry(Items.COAST_ARMOR_TRIM_SMITHING_TEMPLATE, 4),
            entry(Items.WILD_ARMOR_TRIM_SMITHING_TEMPLATE, 4),
            entry(Items.WARD_ARMOR_TRIM_SMITHING_TEMPLATE, 4),
            entry(Items.EYE_ARMOR_TRIM_SMITHING_TEMPLATE, 4),
            entry(Items.VEX_ARMOR_TRIM_SMITHING_TEMPLATE, 4),
            entry(Items.TIDE_ARMOR_TRIM_SMITHING_TEMPLATE, 4),
            entry(Items.SNOUT_ARMOR_TRIM_SMITHING_TEMPLATE, 4),
            entry(Items.RIB_ARMOR_TRIM_SMITHING_TEMPLATE, 4),
            entry(Items.SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE, 4),
            entry(Items.WAYFINDER_ARMOR_TRIM_SMITHING_TEMPLATE, 4),
            entry(Items.SHAPER_ARMOR_TRIM_SMITHING_TEMPLATE, 4),
            entry(Items.SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE, 2),
            entry(Items.RAISER_ARMOR_TRIM_SMITHING_TEMPLATE, 4),
            entry(Items.HOST_ARMOR_TRIM_SMITHING_TEMPLATE, 4),
            entry(Items.ENCHANTED_GOLDEN_APPLE, 2),
            entry(Items.TOTEM_OF_UNDYING, 2),
            entry(Items.GOLDEN_APPLE, 6),
            entry(Items.DIAMOND, 6),
            entry(Items.SUSPICIOUS_GRAVEL, 6),
            entry(Items.SUSPICIOUS_SAND, 6));
  }

  public FishingLootTable() {
    this.modifyLootTable();
  }

  public void modifyLootTable() {
    LootTableEvents.MODIFY.register(this::modifyTreasureLootTable);
  }

  private void modifyTreasureLootTable(
      @NotNull final ResourceManager resourceManager,
      @NotNull final LootManager lootManager,
      @NotNull final Identifier id,
      @NotNull final LootTable.Builder tableBuilder,
      @NotNull final LootTableSource source) {
    final Identifier table = LootTables.FISHING_TREASURE_GAMEPLAY;
    if (!id.equals(table)) {
      return;
    }
    if (!source.isBuiltin()) {
      return;
    }
    final LootPool.Builder builder = LootPool.builder();
    for (final Map.Entry<Item, Integer> entry : ITEMS.entrySet()) {
      final Item item = entry.getKey();
      final int weight = entry.getValue();
      final LeafEntry.Builder<?> build = ItemEntry.builder(item);
      build.weight(weight);
      builder.with(build);
    }
    tableBuilder.pool(builder);
  }
}
