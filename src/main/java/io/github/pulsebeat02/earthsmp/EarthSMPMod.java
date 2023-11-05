package io.github.pulsebeat02.earthsmp;

import io.github.pulsebeat02.earthsmp.drops.AncientCityCrate;
import io.github.pulsebeat02.earthsmp.drops.BastionCrate;
import io.github.pulsebeat02.earthsmp.drops.EndCityCrate;
import io.github.pulsebeat02.earthsmp.drops.VillagerCrate;
import net.fabricmc.api.ModInitializer;

import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EarthSMPMod implements ModInitializer {

  public static final Logger LOGGER = LoggerFactory.getLogger("earthsmp");
  private static MinecraftServer SERVER;

  @Override
  public void onInitialize() {
    LOGGER.info("Initializing EarthSMP Fabric Mod");
    this.registerCrates();
  }

  private void registerCrates() {
    new AncientCityCrate();
    new BastionCrate();
    new EndCityCrate();
    new VillagerCrate();
  }

  public static @NotNull MinecraftServer getServer() {
    return SERVER;
  }

  public static void setServer(@NotNull final MinecraftServer server) {
    SERVER = server;
  }
}
