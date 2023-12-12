package io.github.pulsebeat02.smpearth;

import io.github.pulsebeat02.smpearth.drops.AppleCrate;
import io.github.pulsebeat02.smpearth.drops.SmithingCrate;
import io.github.pulsebeat02.smpearth.drops.TotemCrate;
import io.github.pulsebeat02.smpearth.events.PlayerTeleportationHandler;
import io.github.pulsebeat02.smpearth.potion.PotionHandler;
import net.fabricmc.api.ModInitializer;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class SMPEarth implements ModInitializer {

  public static final Logger LOGGER = LoggerFactory.getLogger("smpearth");
  private static MinecraftServer SERVER;

  @Override
  public void onInitialize() {
    this.registerCrates();
    this.registerEvents();
    this.registerPotions();
    LOGGER.info("[SMP Earth] Mod has been loaded!");
  }

  private void registerCrates() {
    new AppleCrate();
    new TotemCrate();
    new SmithingCrate();
  }

  private void registerPotions() {
    PotionHandler.registerPotions();
  }

  private void registerEvents() {
    new PlayerTeleportationHandler();
  }

  public static @NotNull MinecraftServer getServer() {
    return SERVER;
  }

  public static void setServer(@NotNull final MinecraftServer server) {
    SERVER = server;
  }
}
