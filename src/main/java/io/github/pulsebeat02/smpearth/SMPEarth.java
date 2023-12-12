package io.github.pulsebeat02.smpearth;

import io.github.pulsebeat02.smpearth.drops.AppleCrate;
import io.github.pulsebeat02.smpearth.drops.SmithingCrate;
import io.github.pulsebeat02.smpearth.drops.TotemCrate;
import io.github.pulsebeat02.smpearth.events.PlayerTeleportationHandler;
import io.github.pulsebeat02.smpearth.potion.PotionHandler;
import net.fabricmc.api.ModInitializer;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class SMPEarth implements ModInitializer {
  private static @Nullable MinecraftServer SERVER;

  @Override
  public void onInitialize() {
    this.registerCrates();
    this.registerEvents();
    this.registerPotions();
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
