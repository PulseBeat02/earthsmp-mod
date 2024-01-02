package io.github.pulsebeat02.smpearth.mixin;

import java.util.concurrent.CompletableFuture;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.PlainTextContent;
import net.minecraft.text.Style;
import net.minecraft.text.TextContent;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BucketItem.class)
public final class AetherPortalCheck {

  //
  // X X X X
  // X     X
  // X     X
  // X     X
  // X X X X
  //

  @Shadow @Final private Fluid fluid;
  @Unique private static final boolean[][] PORTAL_SHAPE;

  static {
    PORTAL_SHAPE =
        new boolean[][] {
          new boolean[] {true, true, true, true},
          new boolean[] {true, false, false, true},
          new boolean[] {true, false, false, true},
          new boolean[] {true, false, false, true},
          new boolean[] {true, true, true, true},
        };
  }

  @Inject(method = "onEmptied", at = @At("HEAD"))
  private void onBucketEmpty(
      @NotNull final PlayerEntity player,
      @NotNull final World world,
      @NotNull final ItemStack stack,
      @NotNull final BlockPos pos,
      @NotNull final CallbackInfo ci) {

    if (!this.checkWater()) {
      return;
    }

    final BlockPos topLeft = this.getTopLeft(player, pos);

    System.out.println(topLeft);

    if (!this.checkPortal(player, topLeft)) {
      return;
    }

    this.sendMessage(player);
  }

  @Unique
  private void sendMessage(@NotNull final PlayerEntity entity) {
    CompletableFuture.runAsync(
        () -> {
          entity.sendMessage(this.createLiteral("Did you just try to create an Aether portal?"));
          entity.sendMessage(
              this.createLiteral(
                  "Add the two pairs of coordinates you have gotten together, and you will find the end portal then."));
        });
  }

  @Unique
  private @NotNull MutableText createLiteral(@NotNull final String message) {
    final TextContent literal = new PlainTextContent.Literal(message);
    final Style style = Style.EMPTY.withColor(Formatting.GOLD).withItalic(false);
    return MutableText.of(literal).setStyle(style);
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
  private boolean checkWater() {
    return this.fluid.matchesType(Fluids.WATER);
  }

  @Unique
  private boolean isGlowstone(@NotNull final BlockState state) {
    return state.isOf(Blocks.GLOWSTONE);
  }

  @Unique
  private @NotNull BlockPos getTopLeft(
      @NotNull final PlayerEntity entity, @NotNull final BlockPos pos) {
    final World world = entity.getWorld();
    BlockPos temp = pos.add(0, -1, 0);
    while (true) {
      final BlockPos top = temp.add(0, 1, 0);
      final BlockPos left = temp.add(-1, 0, 0);
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
