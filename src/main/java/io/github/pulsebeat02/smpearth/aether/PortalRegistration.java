package io.github.pulsebeat02.smpearth.aether;

import net.kyrptonaught.customportalapi.api.CustomPortalBuilder;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.Fluids;

public final class PortalRegistration {

  public PortalRegistration() {
    this.registerAetherPortal();
  }

  public void registerAetherPortal() {
    CustomPortalBuilder.beginPortal()
        .frameBlock(Blocks.GLOWSTONE)
        .lightWithFluid(Fluids.WATER)
        .destDimID(AetherDimension.DIMENSION_ID)
        .registerPortal();
  }
}
