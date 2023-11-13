package io.github.pulsebeat02.smpearth.mixin;

import io.github.pulsebeat02.smpearth.SMPEarth;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public final class ServerMixin {

  @Inject(
      method =
          "<init>(Ljava/lang/Thread;Lnet/minecraft/world/level/storage/LevelStorage$Session;Lnet/minecraft/resource/ResourcePackManager;Lnet/minecraft/server/SaveLoader;Ljava/net/Proxy;Lcom/mojang/datafixers/DataFixer;Lnet/minecraft/util/ApiServices;Lnet/minecraft/server/WorldGenerationProgressListenerFactory;)V",
      at = @At("TAIL"))
  private void assignMinecraftServer(@NotNull final CallbackInfo ci) {
    final MinecraftServer server = (MinecraftServer) (Object) this;
    SMPEarth.setServer(server);
  }
}
