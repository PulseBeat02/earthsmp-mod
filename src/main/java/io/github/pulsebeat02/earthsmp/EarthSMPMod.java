package io.github.pulsebeat02.earthsmp;

import io.github.pulsebeat02.earthsmp.drops.AncientCityCrate;
import io.github.pulsebeat02.earthsmp.drops.BastionCrate;
import io.github.pulsebeat02.earthsmp.drops.EndCityCrate;
import io.github.pulsebeat02.earthsmp.drops.VillagerCrate;
import io.github.pulsebeat02.earthsmp.listeners.PlayerMovementTickListener;
import io.github.pulsebeat02.earthsmp.listeners.ServerTickListener;
import io.github.pulsebeat02.earthsmp.nerfs.MinerFatigueNerf;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EarthSMPMod implements ModInitializer {

  public static final Logger LOGGER = LoggerFactory.getLogger("earthsmp");
  public static int MIN_X = -12288;
  public static int MAX_X = 12288;
  public static int MIN_Z = -6144;
  public static int MAX_Z = 6144;

  private MinecraftServer server;

  @Override
  public void onInitialize() {
    LOGGER.info("Initializing EarthSMP Fabric Mod");
    this.registerTickListeners();
    this.registerCrates();
    this.registerNerfs();
  }

  private void registerTickListeners() {
    ServerTickEvents.START_SERVER_TICK.register(new ServerTickListener(this));
    ServerTickEvents.START_SERVER_TICK.register(new PlayerMovementTickListener());
  }

  private void registerCrates() {
    new AncientCityCrate(this);
    new BastionCrate(this);
    new EndCityCrate(this);
    new VillagerCrate(this);
  }

  private void registerNerfs() {
    new MinerFatigueNerf();
  }

  public void setServer(@NotNull final MinecraftServer server) {
    this.server = server;
  }

  public @NotNull MinecraftServer getServer() {
    return this.server;
  }
}
