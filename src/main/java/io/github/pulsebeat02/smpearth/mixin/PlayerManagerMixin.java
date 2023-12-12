package io.github.pulsebeat02.smpearth.mixin;

import io.github.pulsebeat02.smpearth.events.PlayerTeleportationHandler;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ConnectedClientData;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stat;
import net.minecraft.stat.StatHandler;
import net.minecraft.stat.Stats;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerManager.class)
public class PlayerManagerMixin {

  @Inject(at = @At(value = "TAIL"), method = "onPlayerConnect")
  private void onPlayerJoin(
      @NotNull final ClientConnection connection,
      @NotNull final ServerPlayerEntity player,
      @NotNull final ConnectedClientData clientData,
      @NotNull final CallbackInfo ci) {
    final int leaveGame = this.getLeaveGame(player);
    if (leaveGame < 1) {
      PlayerTeleportationHandler.addPlayerToQueue(player.getUuid());
    }
  }

  @Unique
  private int getLeaveGame(@NotNull final ServerPlayerEntity player) {
    final StatHandler stats = player.getStatHandler();
    final Stat<?> stat = Stats.CUSTOM.getOrCreateStat(Stats.PLAY_TIME);
    return stats.getStat(stat);
  }
}
