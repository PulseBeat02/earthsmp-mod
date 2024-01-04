package io.github.pulsebeat02.smpearth.callback;

import static net.minecraft.entity.effect.StatusEffects.SPEED;

import java.util.List;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public final class GoatHornCallback {

  private static final int BLOCK_RADIUS_SQUARED;

  static {
    BLOCK_RADIUS_SQUARED = 10 * 10;
  }

  public GoatHornCallback() {
    UseItemCallback.EVENT.register(this::onGoatHornUse);
  }

  public @NotNull TypedActionResult<ItemStack> onGoatHornUse(
      @NotNull final PlayerEntity player, @NotNull final World world, @NotNull final Hand hand) {
    final ItemStack itemStack = player.getStackInHand(hand);
    final Item item = itemStack.getItem();
    if (!this.isGoatHorn(item)) {
      return TypedActionResult.pass(itemStack);
    }
    final List<? extends PlayerEntity> players = world.getPlayers();
    for (final PlayerEntity entity : players) {
      if (entity.squaredDistanceTo(player) <= BLOCK_RADIUS_SQUARED) {
        this.addEffect(entity);
      }
    }
    return TypedActionResult.pass(itemStack);
  }

  private void addEffect(@NotNull final PlayerEntity entity) {
    entity.addStatusEffect(new StatusEffectInstance(SPEED, 80));
  }

  private boolean isGoatHorn(@NotNull final Item item) {
    return item == Items.GOAT_HORN;
  }
}
