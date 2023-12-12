package io.github.pulsebeat02.smpearth.mixin;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import io.github.pulsebeat02.smpearth.utils.HeadCacheLoader;
import java.util.concurrent.TimeUnit;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public final class PlayerHeadDropMixin {

  @Unique private static final LoadingCache<String, ItemStack> CACHE;

  static {
    CACHE =
        CacheBuilder.newBuilder()
            .maximumSize(10000)
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .build(new HeadCacheLoader());
  }

  @Inject(method = "onDeath", at = @At("TAIL"))
  private void dropHead(@NotNull final DamageSource damageSource, @NotNull final CallbackInfo ci) {
    final ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;
    final ServerWorld world = player.getServerWorld();
    final Vec3d location = player.getPos();
    final String uuid = player.getUuidAsString();
    final ItemEntity itemEntity = getItemEntity(uuid, location, world);
    world.spawnEntity(itemEntity);
  }

  @Unique
  private static @NotNull ItemEntity getItemEntity(
      @NotNull final String uuid, @NotNull final Vec3d location, @NotNull final ServerWorld world) {
    final ItemStack stack = CACHE.getUnchecked(uuid).copy();
    final double x = location.getX();
    final double y = location.getY();
    final double z = location.getZ();
    return new ItemEntity(world, x, y, z, stack);
  }
}
