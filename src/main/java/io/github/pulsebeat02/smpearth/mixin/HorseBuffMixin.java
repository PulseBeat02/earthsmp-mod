package io.github.pulsebeat02.smpearth.mixin;

import java.util.function.DoubleSupplier;
import java.util.function.IntUnaryOperator;
import net.minecraft.entity.passive.AbstractHorseEntity;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.*;

@Mixin(AbstractHorseEntity.class)
public final class HorseBuffMixin {

  @Mutable @Final @Shadow private static float MIN_MOVEMENT_SPEED_BONUS;
  @Mutable @Final @Shadow private static float MAX_MOVEMENT_SPEED_BONUS;
  @Mutable @Final @Shadow private static float MIN_JUMP_STRENGTH_BONUS;
  @Mutable @Final @Shadow private static float MAX_JUMP_STRENGTH_BONUS;
  @Mutable @Final @Shadow private static float MIN_HEALTH_BONUS;
  @Mutable @Final @Shadow private static float MAX_HEALTH_BONUS;

  static {
    MIN_MOVEMENT_SPEED_BONUS = (float) getChildMovementSpeedBonusRaw(() -> 2.0);
    MAX_MOVEMENT_SPEED_BONUS = (float) getChildMovementSpeedBonusRaw(() -> 2.0);
    MIN_JUMP_STRENGTH_BONUS = (float) getChildJumpStrengthBonusRaw(() -> 2.0);
    MAX_JUMP_STRENGTH_BONUS = (float) getChildJumpStrengthBonusRaw(() -> 2.0);
    MIN_HEALTH_BONUS = getChildHealthBonusRaw(max -> max - 1);
    MAX_HEALTH_BONUS = getChildHealthBonusRaw(max -> 3 * (max - 1));
  }

  @Unique
  private static double getChildJumpStrengthBonusRaw(
      @NotNull final DoubleSupplier randomDoubleGetter) {
    return (double) 0.4f
        + randomDoubleGetter.getAsDouble() * 0.2
        + randomDoubleGetter.getAsDouble() * 0.2
        + randomDoubleGetter.getAsDouble() * 0.2;
  }

  @Unique
  private static double getChildMovementSpeedBonusRaw(
      @NotNull final DoubleSupplier randomDoubleGetter) {
    return ((double) 0.45f
            + randomDoubleGetter.getAsDouble() * 0.3
            + randomDoubleGetter.getAsDouble() * 0.3
            + randomDoubleGetter.getAsDouble() * 0.3)
        * 0.25;
  }

  @Unique
  private static float getChildHealthBonusRaw(@NotNull final IntUnaryOperator randomIntGetter) {
    return 15.0f + (float) randomIntGetter.applyAsInt(8) + (float) randomIntGetter.applyAsInt(9);
  }
}
