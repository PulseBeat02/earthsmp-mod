package io.github.pulsebeat02.smpearth.mixin;

import java.util.*;

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

import static net.minecraft.item.Items.*;

@Mixin(BrushableBlockEntity.class)
public abstract class SuspiciousBlockMixin extends BlockEntity {

  @Unique private static @NotNull final List<ItemStack> SAND_DROPS;
  @Unique private static @NotNull final List<ItemStack> GRAVEL_DROPS;
  @Unique private static @NotNull final List<ItemStack> JUNK_DROPS;
  @Unique private static @NotNull final SplittableRandom RANDOM;

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
    final Collection<Item> drops =
        Set.of(
            ANGLER_POTTERY_SHERD,
            ARCHER_POTTERY_SHERD,
            ARMS_UP_POTTERY_SHERD,
            BLADE_POTTERY_SHERD,
            BREWER_POTTERY_SHERD,
            BURN_POTTERY_SHERD,
            DANGER_POTTERY_SHERD,
            EXPLORER_POTTERY_SHERD,
            FRIEND_POTTERY_SHERD,
            HEART_POTTERY_SHERD,
            HEARTBREAK_POTTERY_SHERD,
            HOWL_POTTERY_SHERD,
            MINER_POTTERY_SHERD,
            MOURNER_POTTERY_SHERD,
            PLENTY_POTTERY_SHERD,
            PRIZE_POTTERY_SHERD,
            SHEAF_POTTERY_SHERD,
            SHELTER_POTTERY_SHERD,
            SKULL_POTTERY_SHERD,
            SNORT_POTTERY_SHERD);
    drops.stream().map(ItemStack::new).forEach(GRAVEL_DROPS::add);
  }

  @Unique
  private static void registerSandDrops() {
    final Collection<String> types =
        Set.of(
            "ponder_goat_horn",
            "sing_goat_horn",
            "seek_goat_horn",
            "feel_goat_horn",
            "admire_goat_horn",
            "call_goat_horn",
            "yearn_goat_horn",
            "dream_goat_horn");
    types.stream().map("minecraft:%s"::formatted).forEach(SuspiciousBlockMixin::registerGoatHorn);
  }

  @Unique
  private static void registerGoatHorn(@NotNull final String id) {
    final ItemStack stack = new ItemStack(GOAT_HORN);
    final NbtCompound nbtCompound = stack.getOrCreateNbt();
    nbtCompound.putString("instrument", id);
    SAND_DROPS.add(stack);
  }

  @Unique
  private static void registerJunkDrops() {
    final Collection<Item> drops =
        Set.of(
            AIR,
            BONE,
            FLINT,
            GOLD_INGOT,
            IRON_INGOT,
            COBBLESTONE,
            DIORITE,
            ANDESITE,
            SNIFFER_EGG,
            BAMBOO,
            DEEPSLATE,
            WHEAT_SEEDS,
            BEETROOT_SEEDS,
            MELON_SEEDS,
            PUMPKIN_SEEDS,
            DIAMOND,
            EMERALD,
            LAPIS_LAZULI,
            REDSTONE,
            COAL,
            CHARCOAL,
            FIRE_CHARGE,
            BRICK,
            DIRT,
            SAND,
            GRANITE);
    drops.stream().map(ItemStack::new).forEach(JUNK_DROPS::add);
  }

  @Shadow private @NotNull ItemStack item;
  @Shadow private @NotNull Identifier lootTable;

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

    if (!this.item.isOf(AIR)) {
      return;
    }

    final BlockState state = this.getCachedState();
    final boolean gravel = state.isOf(Blocks.SUSPICIOUS_GRAVEL);
    this.item = this.generateItem(gravel);

    this.markDirty();
  }

  @Unique
  private @NotNull ItemStack generateItem(final boolean gravel) {
    final int rand = RANDOM.nextInt(0, 10); // 0, 1, 2, 3, 4, 5, 6, 7, 8, 9
    if (rand <= 7) {
      return getJunkItemStack();
    } else {
      return getTreasureItemStack(gravel);
    }
  }

  @Unique
  private static @NotNull ItemStack getJunkItemStack() {
    final int index = RANDOM.nextInt(JUNK_DROPS.size());
    return JUNK_DROPS.get(index).copy();
  }

  @Unique
  private static @NotNull ItemStack getTreasureItemStack(final boolean gravel) {
    if (gravel) {
      final int index = RANDOM.nextInt(GRAVEL_DROPS.size());
      return GRAVEL_DROPS.get(index).copy();
    } else {
      final int index = RANDOM.nextInt(SAND_DROPS.size());
      return SAND_DROPS.get(index).copy();
    }
  }
}
