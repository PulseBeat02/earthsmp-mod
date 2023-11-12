package io.github.pulsebeat02.smpearth;

import net.minecraft.util.Pair;

public enum Continent {
  NA(new Pair<>(-12288, -5734), new Pair<>(-1365, -478)),
  SA(new Pair<>(-5530, -819), new Pair<>(-2389, 3755)),
  EU(new Pair<>(-1707, -2389), new Pair<>(4437, -4915)),
  AF(new Pair<>(-1161, -2526), new Pair<>(3413, 2389)),
  AS(new Pair<>(3345, -4915), new Pair<>(12288, -1024)),
  AU(new Pair<>(7782, 819), new Pair<>(10513, 2594));

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
