package io.github.pulsebeat02.earthsmp.mixin;

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
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.IndexedIterable;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(BrushableBlockEntity.class)
public abstract class SuspiciousBlockMixin extends BlockEntity {

  private static final List<ItemStack> SAND_DROPS;
  private static final List<ItemStack> GRAVEL_DROPS;
  private static final SplittableRandom RANDOM;

  static {
    SAND_DROPS = new ArrayList<>();
    GRAVEL_DROPS = new ArrayList<>();
    registerSandDrops();
    registerGravelDrops();
    registerCommonDrops();
    RANDOM = new SplittableRandom();
  }

  @Unique
  private static void registerGravelDrops() {
    final Item[] drops =
        new Item[] {
          Items.ANGLER_POTTERY_SHERD,
          Items.ARCHER_POTTERY_SHERD,
          Items.ARMS_UP_POTTERY_SHERD,
          Items.BLADE_POTTERY_SHERD,
          Items.BREWER_POTTERY_SHERD,
          Items.BURN_POTTERY_SHERD,
          Items.DANGER_POTTERY_SHERD,
          Items.EXPLORER_POTTERY_SHERD,
          Items.FRIEND_POTTERY_SHERD,
          Items.HEART_POTTERY_SHERD,
          Items.HEARTBREAK_POTTERY_SHERD,
          Items.HOWL_POTTERY_SHERD,
          Items.MINER_POTTERY_SHERD,
          Items.MOURNER_POTTERY_SHERD,
          Items.PLENTY_POTTERY_SHERD,
          Items.PRIZE_POTTERY_SHERD,
          Items.SHEAF_POTTERY_SHERD,
          Items.SHELTER_POTTERY_SHERD,
          Items.SKULL_POTTERY_SHERD,
          Items.SNORT_POTTERY_SHERD,
        };
    for (final Item drop : drops) {
      final ItemStack stack = new ItemStack(drop);
      GRAVEL_DROPS.add(stack);
    }
  }

  @Unique
  private static void registerSandDrops() {
    final IndexedIterable<RegistryEntry<Instrument>> instruments =
        Registries.INSTRUMENT.getIndexedEntries();
    while (instruments.iterator().hasNext()) {
      final RegistryEntry<Instrument> entry = instruments.iterator().next();
      final ItemStack stack = new ItemStack(Items.GOAT_HORN);
      final NbtCompound nbtCompound = stack.getOrCreateNbt();
      nbtCompound.putString("instrument", entry.getKey().orElseThrow().getValue().toString());
      SAND_DROPS.add(stack);
    }
  }

  @Unique
  private static void registerCommonDrops() {
    final Item[] drops =
        new Item[] {
          Items.AIR,
          Items.BONE,
          Items.BONE_MEAL,
          Items.FLINT,
          Items.GOLD_NUGGET,
          Items.GOLD_INGOT,
          Items.IRON_NUGGET,
          Items.COBBLESTONE,
          Items.DEEPSLATE,
          Items.WHEAT_SEEDS,
          Items.BEETROOT_SEEDS,
          Items.MELON_SEEDS,
          Items.PUMPKIN_SEEDS,
          Items.TORCHFLOWER_SEEDS
        };
    for (final Item drop : drops) {
      final ItemStack stack = new ItemStack(drop);
      SAND_DROPS.add(stack);
      GRAVEL_DROPS.add(stack);
    }
  }

  @Shadow private ItemStack item;
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
      final int index = RANDOM.nextInt(GRAVEL_DROPS.size());
      this.item = GRAVEL_DROPS.get(index).copy();
    } else if (state.isOf(Blocks.SUSPICIOUS_SAND)) {
      final int index = RANDOM.nextInt(SAND_DROPS.size());
      this.item = SAND_DROPS.get(index).copy();
    }
    this.lootTable = null;
    this.markDirty();
  }
}
