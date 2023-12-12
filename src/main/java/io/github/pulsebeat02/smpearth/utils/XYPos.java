package io.github.pulsebeat02.smpearth.utils;

public final class XYPos {

  private final int x;
  private final int y;

  public XYPos(final int x, final int y) {
    this.x = x;
    this.y = y;
  }

  public int x() {
    return this.x;
  }

  public int y() {
    return this.y;
  }
}
