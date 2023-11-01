package io.github.pulsebeat02.earthsmp.utils;

import io.github.pulsebeat02.earthsmp.Continent;
import net.minecraft.util.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.Calendar;

public final class Utils {

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
    final Calendar calendar = Calendar.getInstance();
    return calendar.get(Calendar.DAY_OF_WEEK) == day
        && calendar.get(Calendar.HOUR_OF_DAY) == hour
        && calendar.get(Calendar.MINUTE) == minute;
  }
}
