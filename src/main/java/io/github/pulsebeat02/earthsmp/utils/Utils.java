package io.github.pulsebeat02.earthsmp.utils;

import io.github.pulsebeat02.earthsmp.Continent;
import io.github.pulsebeat02.earthsmp.EarthSMPMod;
import net.minecraft.block.BlockState;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.SplittableRandom;

public final class Utils {

  private static final SplittableRandom RANDOM;

  static {
    RANDOM = new SplittableRandom();
  }

  private Utils() {
    throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
  }

  public static boolean withinContinent(
      @NotNull final Continent continent, final int x, final int z) {
    final Pair<Integer, Integer> topLeft = continent.getTopLeft();
    final Pair<Integer, Integer> bottomRight = continent.getBottomRight();
    final boolean withinX = checkXCoordinate(topLeft, bottomRight, x);
    final boolean withinZ = checkZCoordinate(topLeft, bottomRight, z);
    return withinX && withinZ;
  }

  private static boolean checkXCoordinate(
      @NotNull final Pair<Integer, Integer> topLeft,
      @NotNull final Pair<Integer, Integer> bottomRight,
      final int x) {
    return x > topLeft.getLeft() && x < bottomRight.getLeft();
  }

  private static boolean checkZCoordinate(
      @NotNull final Pair<Integer, Integer> topLeft,
      @NotNull final Pair<Integer, Integer> bottomRight,
      final int z) {
    return z > topLeft.getRight() && z < bottomRight.getRight();
  }

  public static boolean checkTime(final int day, final int hour, final int minute) {
    final ZonedDateTime time = ZonedDateTime.now();
    return time.getDayOfWeek().getValue() == day
        && time.getHour() == hour
        && time.getMinute() == minute;
  }

  public static @NotNull BlockPos generateRandomPosition(@NotNull final Continent continent) {
    final Pair<Integer, Integer> topLeft = continent.getTopLeft();
    final Pair<Integer, Integer> bottomRight = continent.getBottomRight();
    outer:
    while (true) {
      final int x = generateRandomX(topLeft, bottomRight);
      final int z = generateRandomZ(topLeft, bottomRight);
      final MinecraftServer server = EarthSMPMod.getServer();
      final World world = server.getOverworld();
      BlockPos pos = null;
      for (int y = 255; y > -60; y--) {
        pos = new BlockPos(x, y, z);
        final BlockState state = world.getBlockState(pos);
        if (state.isAir()) {
          continue;
        }
        if (state.isLiquid()) {
          continue outer;
        }
      }
      return pos.add(0, 1, 0);
    }
  }

  private static int generateRandomX(
      @NotNull final Pair<Integer, Integer> topLeft,
      @NotNull final Pair<Integer, Integer> bottomRight) {
    return (int) (RANDOM.nextDouble() * (bottomRight.getLeft() - topLeft.getLeft()))
        + topLeft.getLeft();
  }

  private static int generateRandomZ(
      @NotNull final Pair<Integer, Integer> topLeft,
      @NotNull final Pair<Integer, Integer> bottomRight) {
    return (int) (RANDOM.nextDouble() * (bottomRight.getRight() - topLeft.getRight()))
        + topLeft.getRight();
  }

  public static @NotNull <T extends Enum<?>> T randomEnum(@NotNull final Class<T> clazz) {
    final int index = RANDOM.nextInt(clazz.getEnumConstants().length);
    return clazz.getEnumConstants()[index];
  }

  public static @NotNull Vec3d cloneVector(@NotNull final Vec3d vector) {
    return new Vec3d(vector.getX(), vector.getY(), vector.getZ());
  }
}
