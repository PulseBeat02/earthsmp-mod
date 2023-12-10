package io.github.pulsebeat02.smpearth.mixin;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.graph.Graph;
import com.mojang.authlib.GameProfile;
import io.github.pulsebeat02.smpearth.SMPEarth;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.common.SyncedClientOptions;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Mixin(ServerPlayerEntity.class)
public final class PlayerHeadDropMixin {

  private static final LoadingCache<String, ItemStack> CACHE;

  static {
    CACHE =
        CacheBuilder.newBuilder()
            .maximumSize(10000)
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .build(
                new CacheLoader<>() {
                  @Override
                  public @NotNull ItemStack load(@NotNull final String key) {
                    return getHead(key);
                  }
                });
  }

  private static @NotNull ItemStack getHead(@NotNull final String key) {
    final UUID uuid = UUID.fromString(key);
    final MinecraftServer server = SMPEarth.getServer();
    final Optional<GameProfile> profile = server.getUserCache().getByUuid(uuid);
    final ItemStack stack = new ItemStack(Items.PLAYER_HEAD);
    stack
        .getOrCreateNbt()
        .putString("SkullOwner", profile.map(GameProfile::getName).orElse("Steve"));
    return stack;
  }

  @Inject(method = "onDeath", at = @At("TAIL"))
  private void dropHead(@NotNull final DamageSource damageSource, @NotNull final CallbackInfo ci) {
    final ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;
    final ServerWorld world = player.getServerWorld();
    final Vec3d location = player.getPos();
    final String uuid = player.getUuidAsString();
    final ItemStack stack = CACHE.getUnchecked(uuid).copy();
    final ItemEntity itemEntity =
        new ItemEntity(world, location.getX(), location.getY(), location.getZ(), stack);
    world.spawnEntity(itemEntity);
  }
}
