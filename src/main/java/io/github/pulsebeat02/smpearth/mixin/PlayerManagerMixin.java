package io.github.pulsebeat02.smpearth.mixin;

import io.github.pulsebeat02.smpearth.Continent;
import io.github.pulsebeat02.smpearth.utils.Utils;
import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ConnectedClientData;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.StatHandler;
import net.minecraft.stat.Stats;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
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
    final StatHandler handler = player.getStatHandler();
    final int leaveGame = handler.getStat(Stats.CUSTOM.getOrCreateStat(Stats.PLAY_TIME));
    if (leaveGame < 1) {
      final Continent random = Utils.getRandomEnum(Continent.class);
      final BlockPos pos = Utils.generateRandomPosition(random);
      final ServerWorld world = player.getServer().getWorld(World.OVERWORLD);
      final Vec3d vec = new Vec3d(pos.getX(), pos.getY(), pos.getZ());
      final Vec3d velocity = new Vec3d(0, 0, 0);
      final TeleportTarget target = new TeleportTarget(vec, velocity, 0, 0);
      FabricDimensions.teleport(player, world, target);
    }
  }
}
