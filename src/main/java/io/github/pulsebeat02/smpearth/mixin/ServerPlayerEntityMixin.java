package io.github.pulsebeat02.smpearth.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.GameRules;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin {
  @Redirect(
      method = "copyFrom",
      at =
          @At(
              value = "INVOKE",
              target =
                  "net/minecraft/world/GameRules.getBoolean(Lnet/minecraft/world/GameRules$Key;)Z"))
  private boolean onCopyFrom(
      @NotNull final GameRules rules,
      @NotNull final GameRules.Key<GameRules.BooleanRule> key,
      @NotNull final ServerPlayerEntity oldPlayer) {
    final LivingEntity attacker = oldPlayer.getAttacker();
    final boolean gamerule = rules.getBoolean(key);
    final boolean playerAttack = (attacker == null || attacker.getType() != EntityType.PLAYER);
    return gamerule && playerAttack;
  }
}
