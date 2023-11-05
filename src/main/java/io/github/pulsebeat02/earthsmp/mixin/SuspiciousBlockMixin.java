package io.github.pulsebeat02.earthsmp.mixin;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.ArrayList;
import java.util.List;
import java.util.SplittableRandom;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.BrushableBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.InstrumentTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.IndexedIterable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(BrushableBlockEntity.class)
public abstract class SuspiciousBlockMixin extends BlockEntity {

  private static final Identifier GRAVEL;
  private static final List<ItemStack> GOAT_HORNS;
  private static final SplittableRandom RANDOM;

  static {
    GRAVEL = new Identifier("earthsmp", "archaeology/gravel");
    final IndexedIterable<RegistryEntry<Instrument>> instruments =
        Registries.INSTRUMENT.getIndexedEntries();
    GOAT_HORNS = new ArrayList<>();
    while (instruments.iterator().hasNext()) {
      final GoatHornItem item =
          new GoatHornItem(new Item.Settings().maxCount(1), InstrumentTags.GOAT_HORNS);
      final RegistryEntry<Instrument> entry = instruments.iterator().next();
      final ItemStack stack = GoatHornItem.getStackForInstrument(item, entry);
      GOAT_HORNS.add(stack);
    }
    RANDOM = new SplittableRandom();
  }

  @Shadow private ItemStack item;
  @Shadow private long lootTableSeed;
  @Shadow private Identifier lootTable;

  public SuspiciousBlockMixin(
      @NotNull final BlockEntityType<?> type,
      @NotNull final BlockPos pos,
      @NotNull final BlockState state) {
    super(type, pos, state);
  }

  /**
   * @author PulseBeat_02
   * @reason rewrite loot tables
   */
  @Overwrite
  public void generateItem(@NotNull final PlayerEntity player) {
    if (this.world == null || this.world.isClient() || this.world.getServer() == null) {
      return;
    }
    if (player instanceof final ServerPlayerEntity serverPlayerEntity) {
      Criteria.PLAYER_GENERATES_CONTAINER_LOOT.trigger(serverPlayerEntity, this.lootTable);
    }
    final BlockState state = this.getCachedState();
    if (state.isOf(Blocks.SUSPICIOUS_GRAVEL)) {
      final LootTable table = this.world.getServer().getLootManager().getLootTable(GRAVEL);
      final LootContextParameterSet lootContextParameterSet =
          new LootContextParameterSet.Builder((ServerWorld) this.world)
              .add(LootContextParameters.ORIGIN, Vec3d.ofCenter(this.pos))
              .luck(player.getLuck())
              .add(LootContextParameters.THIS_ENTITY, player)
              .build(LootContextTypes.CHEST);
      final ObjectArrayList<ItemStack> objectArrayList =
          table.generateLoot(lootContextParameterSet, this.lootTableSeed);
      this.item =
          switch (objectArrayList.size()) {
            case 0 -> ItemStack.EMPTY;
            default -> objectArrayList.get(0);
          };
    } else if (state.isOf(Blocks.SUSPICIOUS_SAND)) {
      final int index = RANDOM.nextInt(GOAT_HORNS.size());
      this.item = GOAT_HORNS.get(index);
    }
    this.markDirty();
  }
}
