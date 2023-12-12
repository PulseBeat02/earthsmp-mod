package io.github.pulsebeat02.smpearth.utils;

import com.google.common.cache.CacheLoader;
import com.mojang.authlib.GameProfile;
import io.github.pulsebeat02.smpearth.SMPEarth;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.UserCache;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

public final class HeadCacheLoader extends CacheLoader<String, ItemStack> {

  private static @NotNull final String FALLBACK_OWNER;

  static {
    FALLBACK_OWNER = "Steve";
  }

  @Override
  public @NotNull ItemStack load(@NotNull final String key) {
    final String owner = this.getOwner(key);
    final ItemStack stack = new ItemStack(Items.PLAYER_HEAD);
    stack.getOrCreateNbt().putString("SkullOwner", owner);
    return stack;
  }

  private @NotNull String getOwner(@NotNull final String key) {
    final UUID uuid = UUID.fromString(key);
    final MinecraftServer server = SMPEarth.getServer();
    final UserCache cache = server.getUserCache();
    if (cache != null) {
      final Optional<GameProfile> profile = cache.getByUuid(uuid);
      return profile.map(GameProfile::getName).orElse(FALLBACK_OWNER);
    }
    return FALLBACK_OWNER;
  }
}
