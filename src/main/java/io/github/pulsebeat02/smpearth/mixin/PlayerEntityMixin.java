package io.github.pulsebeat02.smpearth.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.GameRules;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {
  @Redirect(
      method = "dropInventory",
      at =
          @At(
              value = "INVOKE",
              target =
                  "net/minecraft/world/GameRules.getBoolean(Lnet/minecraft/world/GameRules$Key;)Z"))
  private boolean checkPlayerKillInventory(
      @NotNull final GameRules rules, @NotNull final GameRules.Key<GameRules.BooleanRule> key) {
    return this.checkPlayerKilled(rules, key);
  }

  @Redirect(
      method = "getXpToDrop",
      at =
          @At(
              value = "INVOKE",
              target =
                  "net/minecraft/world/GameRules.getBoolean(Lnet/minecraft/world/GameRules$Key;)Z"))
  private boolean checkPlayerKillXP(
      @NotNull final GameRules rules, @NotNull final GameRules.Key<GameRules.BooleanRule> key) {
    return this.checkPlayerKilled(rules, key);
  }

  @Unique
  private boolean checkPlayerKilled(
      @NotNull final GameRules rules, @NotNull final GameRules.Key<GameRules.BooleanRule> key) {
    final PlayerEntity player = (PlayerEntity) (Object) this;
    final LivingEntity attacker = player.getAttacker();
    return rules.getBoolean(key) && (attacker == null || attacker.getType() != EntityType.PLAYER);
  }
}
