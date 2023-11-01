package io.github.pulsebeat02.earthsmp.mixin;

import io.github.pulsebeat02.earthsmp.EarthSMPMod;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class ServerMixin {

  @Inject(method = "runServer", at = @At("TAIL"))
  private void serverLoaded(@NotNull final CallbackInfo ci) {
    final MinecraftServer server = (MinecraftServer) (Object) this;
    EarthSMPMod.setServer(server);
  }
}
