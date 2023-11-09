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
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(BrushableBlockEntity.class)
public abstract class SuspiciousBlockMixin extends BlockEntity {

  @Unique
  private static final List<ItemStack> SAND_DROPS;
  @Unique
  private static final List<ItemStack> GRAVEL_DROPS;
  @Unique
  private static final List<ItemStack> JUNK_DROPS;
  @Unique
  private static final SplittableRandom RANDOM;

  static {
    SAND_DROPS = new ArrayList<>();
    GRAVEL_DROPS = new ArrayList<>();
    JUNK_DROPS = new ArrayList<>();
    registerSandDrops();
    registerGravelDrops();
    registerJunkDrops();
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
    final String[] types =
        new String[] {
          "ponder_goat_horn",
          "sing_goat_horn",
          "seek_goat_horn",
          "feel_goat_horn",
          "admire_goat_horn",
          "call_goat_horn",
          "yearn_goat_horn",
          "dream_goat_horn"
        };
    for (final String type : types) {
      final String name = "minecraft:%s".formatted(type);
      final ItemStack stack = new ItemStack(Items.GOAT_HORN);
      final NbtCompound nbtCompound = stack.getOrCreateNbt();
      nbtCompound.putString("instrument", name);
      SAND_DROPS.add(stack);
    }
  }

  @Unique
  private static void registerJunkDrops() {
    final Item[] drops =
        new Item[] {
          Items.AIR,
          Items.BONE,
          Items.FLINT,
          Items.GOLD_INGOT,
          Items.IRON_INGOT,
          Items.COBBLESTONE,
          Items.DIORITE,
          Items.ANDESITE,
          Items.SNIFFER_EGG,
          Items.BAMBOO,
          Items.DEEPSLATE,
          Items.WHEAT_SEEDS,
          Items.BEETROOT_SEEDS,
          Items.MELON_SEEDS,
          Items.PUMPKIN_SEEDS,
          Items.DIAMOND,
          Items.EMERALD,
          Items.LAPIS_LAZULI,
          Items.REDSTONE,
          Items.COAL,
          Items.CHARCOAL,
          Items.FIRE_CHARGE,
          Items.BRICK,
          Items.DIRT,
          Items.SAND,
          Items.GRANITE
        };
    for (final Item drop : drops) {
      final ItemStack stack = new ItemStack(drop);
      JUNK_DROPS.add(stack);
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
    if (!this.item.isOf(Items.AIR)) {
      return;
    }
    final BlockState state = this.getCachedState();
    this.item = this.generateItem(state.isOf(Blocks.SUSPICIOUS_GRAVEL));
    this.lootTable = null;
    this.markDirty();
  }

  @Unique
  private @NotNull ItemStack generateItem(final boolean gravel) {
    final int rand = RANDOM.nextInt(0, 10); // 0, 1, 2, 3, 4, 5, 6, 7, 8, 9
    if (rand <= 7) {
      final int index = RANDOM.nextInt(JUNK_DROPS.size());
      return JUNK_DROPS.get(index).copy();
    } else {
      if (gravel) {
        final int index = RANDOM.nextInt(GRAVEL_DROPS.size());
        return GRAVEL_DROPS.get(index).copy();
      } else {
        final int index = RANDOM.nextInt(SAND_DROPS.size());
        return SAND_DROPS.get(index).copy();
      }
    }
  }
}
