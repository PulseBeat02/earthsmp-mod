package io.github.pulsebeat02.earthsmp.mixin;

import java.util.function.DoubleSupplier;
import java.util.function.IntUnaryOperator;
import net.minecraft.entity.passive.AbstractHorseEntity;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(AbstractHorseEntity.class)
public final class HorseBuffMixin {

  @Mutable @Final @Shadow private static float MIN_MOVEMENT_SPEED_BONUS;
  @Mutable @Final @Shadow private static float MAX_MOVEMENT_SPEED_BONUS;
  @Mutable @Final @Shadow private static float MIN_JUMP_STRENGTH_BONUS;
  @Mutable @Final @Shadow private static float MAX_JUMP_STRENGTH_BONUS;
  @Mutable @Final @Shadow private static float MIN_HEALTH_BONUS;
  @Mutable @Final @Shadow private static float MAX_HEALTH_BONUS;

  static {
    MIN_MOVEMENT_SPEED_BONUS = (float) getChildMovementSpeedBonus(() -> 1.0);
    MAX_MOVEMENT_SPEED_BONUS = (float) getChildMovementSpeedBonus(() -> 2.0);
    MIN_JUMP_STRENGTH_BONUS = (float) getChildJumpStrengthBonus(() -> 1.0);
    MAX_JUMP_STRENGTH_BONUS = (float) getChildJumpStrengthBonus(() -> 2.0);
    MIN_HEALTH_BONUS = getChildHealthBonus(max -> max - 1);
    MAX_HEALTH_BONUS = getChildHealthBonus(max -> 2 * (max - 1));
  }

  private static double getChildJumpStrengthBonus(
      @NotNull final DoubleSupplier randomDoubleGetter) {
    return (double) 0.4f
        + randomDoubleGetter.getAsDouble() * 0.2
        + randomDoubleGetter.getAsDouble() * 0.2
        + randomDoubleGetter.getAsDouble() * 0.2;
  }

  private static double getChildMovementSpeedBonus(
      @NotNull final DoubleSupplier randomDoubleGetter) {
    return ((double) 0.45f
            + randomDoubleGetter.getAsDouble() * 0.3
            + randomDoubleGetter.getAsDouble() * 0.3
            + randomDoubleGetter.getAsDouble() * 0.3)
        * 0.25;
  }

  private static float getChildHealthBonus(@NotNull final IntUnaryOperator randomIntGetter) {
    return 15.0f + (float) randomIntGetter.applyAsInt(8) + (float) randomIntGetter.applyAsInt(9);
  }
}
