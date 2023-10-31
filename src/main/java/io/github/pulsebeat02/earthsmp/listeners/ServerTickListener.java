package io.github.pulsebeat02.earthsmp.listeners;

import io.github.pulsebeat02.earthsmp.EarthSMPMod;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.NotNull;

public final class ServerTickListener implements ServerTickEvents.StartTick {

  private @NotNull final EarthSMPMod mod;

  public ServerTickListener(@NotNull final EarthSMPMod mod) {
    this.mod = mod;
  }

  @Override
  public void onStartTick(@NotNull final MinecraftServer server) {
    this.mod.setServer(server);
  }
}
