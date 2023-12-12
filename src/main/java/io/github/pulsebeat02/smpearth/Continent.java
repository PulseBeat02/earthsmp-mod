package io.github.pulsebeat02.smpearth;

import io.github.pulsebeat02.smpearth.utils.XYPos;
import org.jetbrains.annotations.NotNull;

public enum Continent {
  NA(new XYPos(-12288, -5734), new XYPos(-1365, -478)),
  SA(new XYPos(-5530, -819), new XYPos(-2389, 3755)),
  EU(new XYPos(-1707, -2389), new XYPos(4437, -4915)),
  AF(new XYPos(-1161, -2526), new XYPos(3413, 2389)),
  AS(new XYPos(3345, -4915), new XYPos(12288, -1024)),
  AU(new XYPos(7782, 819), new XYPos(10513, 2594));

  private final XYPos topLeft;
  private final XYPos bottomRight;

  Continent(@NotNull final XYPos topLeft, @NotNull final XYPos bottomRight) {
    this.topLeft = topLeft;
    this.bottomRight = bottomRight;
  }

  public @NotNull XYPos getTopLeft() {
    return this.topLeft;
  }

  public @NotNull XYPos getBottomRight() {
    return this.bottomRight;
  }
}
