package io.github.pulsebeat02.earthsmp;

import net.minecraft.util.Pair;

public enum Continent {
  NA(new Pair<>(-10000, 10000), new Pair<>(-10000, 10000)),
  SA(new Pair<>(-10000, 10000), new Pair<>(-10000, 10000)),
  EU(new Pair<>(-10000, 10000), new Pair<>(-10000, 10000)),
  AF(new Pair<>(-10000, 10000), new Pair<>(-10000, 10000)),
  AS(new Pair<>(-10000, 10000), new Pair<>(-10000, 10000)),
  OC(new Pair<>(-10000, 10000), new Pair<>(-10000, 10000));

  private final Pair<Integer, Integer> topLeft;
  private final Pair<Integer, Integer> bottomRight;

  Continent(final Pair<Integer, Integer> topLeft, final Pair<Integer, Integer> bottomRight) {
    this.topLeft = topLeft;
    this.bottomRight = bottomRight;
  }

  public Pair<Integer, Integer> getTopLeft() {
    return this.topLeft;
  }

  public Pair<Integer, Integer> getBottomRight() {
    return this.bottomRight;
  }
}
