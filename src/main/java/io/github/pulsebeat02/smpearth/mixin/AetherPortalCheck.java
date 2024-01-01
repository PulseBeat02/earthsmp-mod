package io.github.pulsebeat02.smpearth.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockItem.class)
public final class AetherPortalCheck {

  //
  // X X X X
  // X     X
  // X     X
  // X     X
  // X X X X
  //

  @Unique
  private static final boolean[][] PORTAL_SHAPE;

  static {
    PORTAL_SHAPE =
        new boolean[][] {
          new boolean[] {true, true, true, true, true},
          new boolean[] {true, false, false, false, true},
          new boolean[] {true, false, false, false, true},
          new boolean[] {true, false, false, false, true},
          new boolean[] {true, false, false, false, true},
          new boolean[] {true, true, true, true, true},
        };
  }

  @Inject(
      method = "place(Lnet/minecraft/item/ItemPlacementContext;Lnet/minecraft/block/BlockState;)Z",
      at = @At("HEAD"))
  private void onPlace(
      @NotNull final ItemPlacementContext context,
      @NotNull final BlockState state,
      @NotNull final CallbackInfoReturnable<Boolean> cir) {

    final PlayerEntity entity = context.getPlayer();
    final BlockPos pos = context.getBlockPos();
    if (!this.checkWater(state)) {
      return;
    }

    final BlockPos topLeft = this.getTopLeft(entity, pos);
    if (!this.checkPortal(entity, topLeft)) {
      return;
    }

    this.lightPortal(entity, topLeft);
  }

  @Unique
  private void lightPortal(@NotNull final PlayerEntity entity, @NotNull final BlockPos topLeft) {
    final World world = entity.getWorld();
    for (int x = 0; x < PORTAL_SHAPE.length; x++) {
      for (int z = 0; z < PORTAL_SHAPE[x].length; z++) {
        final BlockPos pos = topLeft.add(x, z, 0);
        if (!PORTAL_SHAPE[x][z]) {
          world.setBlockState(pos, Blocks.NETHER_PORTAL.getDefaultState());
        }
      }
    }
  }

  @Unique
  private boolean checkPortal(@NotNull final PlayerEntity entity, @NotNull final BlockPos topLeft) {
    final World world = entity.getWorld();
    for (int x = 0; x < PORTAL_SHAPE.length; x++) {
      for (int z = 0; z < PORTAL_SHAPE[x].length; z++) {
        final BlockPos temp = topLeft.add(x, z, 0);
        final BlockState state = world.getBlockState(temp);
        if (this.isGlowstone(state) == PORTAL_SHAPE[x][z]) {
          return false;
        }
      }
    }
    return true;
  }

  @Unique
  private boolean checkWater(@NotNull final BlockState state) {
    return state.isOf(Blocks.WATER);
  }

  @Unique
  private boolean isGlowstone(@NotNull final BlockState state) {
    return state.isOf(Blocks.GLOWSTONE);
  }

  @Unique
  private @NotNull BlockPos getTopLeft(
      @NotNull final PlayerEntity entity, @NotNull final BlockPos pos) {
    final World world = entity.getWorld();
    BlockPos temp = pos;
    while (true) {
      final BlockPos top = temp.up(1);
      final BlockPos left = temp.west(1);
      final BlockState topState = world.getBlockState(top);
      final BlockState leftState = world.getBlockState(left);
      if (this.isGlowstone(topState)) {
        temp = top;
      } else if (this.isGlowstone(leftState)) {
        temp = left;
      } else {
        return temp;
      }
    }
  }
}
