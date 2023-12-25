package io.github.pulsebeat02.smpearth.aether;

import net.minecraft.block.Blocks;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.Identifier;

public final class PortalRegistration {

  public static final Identifier DIMENSION_ID = new Identifier("smpearth", "aether");

  public PortalRegistration() {
    this.registerAetherPortal();
  }

  public void registerAetherPortal() {
//    CustomPortalBuilder.beginPortal()
//        .frameBlock(Blocks.GLOWSTONE)
//        .lightWithFluid(Fluids.WATER)
//        .destDimID(DIMENSION_ID)
//        .registerPortal();
  }
}
