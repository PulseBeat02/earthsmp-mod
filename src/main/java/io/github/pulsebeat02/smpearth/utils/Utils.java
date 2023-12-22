package io.github.pulsebeat02.smpearth.utils;

import io.github.pulsebeat02.smpearth.Continent;
import io.github.pulsebeat02.smpearth.SMPEarth;

import java.util.SplittableRandom;
import net.minecraft.block.BlockState;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public final class Utils {

  private static @NotNull final SplittableRandom RANDOM;

  static {
    RANDOM = new SplittableRandom();
  }

  private Utils() {
    throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
  }

  public static boolean withinContinent(
      @NotNull final Continent continent, final int x, final int z) {
    final XYPos topLeft = continent.getTopLeft();
    final XYPos bottomRight = continent.getBottomRight();
    final boolean withinX = checkXCoordinate(topLeft, bottomRight, x);
    final boolean withinZ = checkZCoordinate(topLeft, bottomRight, z);
    return withinX && withinZ;
  }

  private static boolean checkXCoordinate(
      @NotNull final XYPos topLeft, @NotNull final XYPos bottomRight, final int x) {
    return x > topLeft.x() && x < bottomRight.x();
  }

  private static boolean checkZCoordinate(
      @NotNull final XYPos topLeft, @NotNull final XYPos bottomRight, final int z) {
    return z > topLeft.z() && z < bottomRight.z();
  }

  public static @NotNull BlockPos generateRandomPlayerPosition() {
    return generateRandomPositionRaw(getRandomEnum(Continent.class));
  }

  public static @NotNull BlockPos generateRandomPositionRaw(@NotNull final Continent continent) {
    final XYPos topLeft = continent.getTopLeft();
    final XYPos bottomRight = continent.getBottomRight();
    final int x = generateRandomX(topLeft, bottomRight);
    final int z = generateRandomZ(topLeft, bottomRight);
    final MinecraftServer server = SMPEarth.getServer();
    final World world = server.getOverworld();
    for (int y = 320; y > -60; y--) {
      final BlockPos pos = new BlockPos(x, y, z);
      final BlockState state = world.getBlockState(pos);
      if (!state.isAir()) {
        return pos.add(0, 1, 0);
      }
    }
    return generateRandomPositionRaw(continent);
  }

  private static int generateRandomX(
      @NotNull final XYPos topLeft, @NotNull final XYPos bottomRight) {
    return RANDOM.nextInt(topLeft.x(), bottomRight.x());
  }

  private static int generateRandomZ(
      @NotNull final XYPos topLeft, @NotNull final XYPos bottomRight) {
    return RANDOM.nextInt(bottomRight.z(), topLeft.z());
  }

  public static @NotNull <T extends Enum<?>> T getRandomEnum(@NotNull final Class<T> clazz) {
    final int index = RANDOM.nextInt(clazz.getEnumConstants().length);
    return clazz.getEnumConstants()[index];
  }
}
