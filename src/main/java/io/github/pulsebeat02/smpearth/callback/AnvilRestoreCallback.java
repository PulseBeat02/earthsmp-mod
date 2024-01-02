package io.github.pulsebeat02.smpearth.callback;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.AnvilBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public final class AnvilRestoreCallback {

  public AnvilRestoreCallback() {
    UseBlockCallback.EVENT.register(this::onAnvilClick);
  }

  public @NotNull ActionResult onAnvilClick(
      @NotNull final PlayerEntity player,
      @NotNull final World world,
      @NotNull final Hand hand,
      @NotNull final BlockHitResult blockHitResult) {

    final ItemStack itemStack = player.getStackInHand(hand);
    final Item item = itemStack.getItem();
    if (!this.isIronIngot(item)) {
      return ActionResult.PASS;
    }

    final BlockPos pos = blockHitResult.getBlockPos();
    final BlockState state = world.getBlockState(pos);
    final Block block = state.getBlock();
    if (!this.isValidAnvilBlock(block)) {
      return ActionResult.PASS;
    }

    final BlockState newState = this.getNewState(state, block);
    world.setBlockState(pos, newState, 3);
    if (!player.isCreative()) {
      itemStack.setCount(itemStack.getCount() - 1);
    }

    this.playSound(world, pos);

    return ActionResult.SUCCESS;
  }

  private void playSound(@NotNull final World world, @NotNull final BlockPos pos) {
    final int x = pos.getX();
    final int y = pos.getY();
    final int z = pos.getZ();
    world.playSound(null, x, y, z, SoundEvents.BLOCK_ANVIL_PLACE, SoundCategory.BLOCKS, 0.5F, 1.0F);
  }

  private @NotNull BlockState getNewState(
      @NotNull final BlockState state, @NotNull final Block block) {
    final Direction rotation = state.get(AnvilBlock.FACING);
    final BlockState newState =
        block == Blocks.CHIPPED_ANVIL
            ? Blocks.ANVIL.getDefaultState()
            : Blocks.CHIPPED_ANVIL.getDefaultState();
    return newState.with(AnvilBlock.FACING, rotation);
  }

  private boolean isValidAnvilBlock(@NotNull final Block block) {
    return block.equals(Blocks.CHIPPED_ANVIL) || block.equals(Blocks.DAMAGED_ANVIL);
  }

  private boolean isIronIngot(@NotNull final Item item) {
    return item == Items.IRON_INGOT;
  }
}
