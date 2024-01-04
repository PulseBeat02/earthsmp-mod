package io.github.pulsebeat02.smpearth;

import io.github.pulsebeat02.smpearth.callback.AnvilRestoreCallback;
import io.github.pulsebeat02.smpearth.potion.PotionHandler;
import net.fabricmc.api.ModInitializer;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class SMPEarth implements ModInitializer {
  private static @Nullable MinecraftServer SERVER;

  @Override
  public void onInitialize() {
    this.registerPotions();
    this.registerCallbacks();
  }

  private void registerCallbacks() {
    new AnvilRestoreCallback();
  }

  private void registerPotions() {
    PotionHandler.registerPotions();
  }

  public static @NotNull MinecraftServer getServer() {
    return SERVER;
  }

  public static void setServer(@NotNull final MinecraftServer server) {
    SERVER = server;
  }
}
