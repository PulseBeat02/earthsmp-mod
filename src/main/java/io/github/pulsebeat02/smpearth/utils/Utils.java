package io.github.pulsebeat02.smpearth.utils;

import io.github.pulsebeat02.smpearth.Continent;
import io.github.pulsebeat02.smpearth.SMPEarth;
import net.minecraft.block.BlockState;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.time.ZonedDateTime;
import java.util.Queue;
import java.util.SplittableRandom;
import java.util.concurrent.ConcurrentLinkedQueue;

public final class Utils {

  private static final SplittableRandom RANDOM;
  private static final Queue<BlockPos> SURFACE_CACHE;

  static {
    RANDOM = new SplittableRandom();
    SURFACE_CACHE = new ConcurrentLinkedQueue<>();
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

  public static @NotNull BlockPos generateRandomPlayerPosition() {
    final BlockPos pos = SURFACE_CACHE.poll();
    if (pos != null) {
      return pos;
    }
    for (int i = 0; i < 100; i++) {
      final Continent cont = getRandomEnum(Continent.class);
      final BlockPos rand = generateRandomPositionRaw(cont);
      SURFACE_CACHE.add(rand);
    }
    return SURFACE_CACHE.poll();
  }

  public static @NotNull BlockPos generateRandomPositionRaw(@NotNull final Continent continent) {
    final Pair<Integer, Integer> topLeft = continent.getTopLeft();
    final Pair<Integer, Integer> bottomRight = continent.getBottomRight();
    final int x = generateRandomX(topLeft, bottomRight);
    final int z = generateRandomZ(topLeft, bottomRight);
    final MinecraftServer server = SMPEarth.getServer();
    final World world = server.getOverworld();
    BlockPos pos = null;
    for (int y = 255; y > -60; y--) {
      pos = new BlockPos(x, y, z);
      final BlockState state = world.getBlockState(pos);
      if (!state.isAir()) {
        break;
      }
    }
    return pos.add(0, 1, 0);
  }

  private static int generateRandomX(
      @NotNull final Pair<Integer, Integer> topLeft,
      @NotNull final Pair<Integer, Integer> bottomRight) {
    return RANDOM.nextInt(topLeft.getLeft(), bottomRight.getLeft());
  }

  private static int generateRandomZ(
      @NotNull final Pair<Integer, Integer> topLeft,
      @NotNull final Pair<Integer, Integer> bottomRight) {
    return RANDOM.nextInt(topLeft.getRight(), bottomRight.getRight());
  }

  public static @NotNull <T extends Enum<?>> T getRandomEnum(@NotNull final Class<T> clazz) {
    final int index = RANDOM.nextInt(clazz.getEnumConstants().length);
    return clazz.getEnumConstants()[index];
  }
}
