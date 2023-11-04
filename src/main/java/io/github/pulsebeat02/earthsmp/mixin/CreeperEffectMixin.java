package io.github.pulsebeat02.earthsmp.mixin;

import io.github.pulsebeat02.earthsmp.Continent;
import io.github.pulsebeat02.earthsmp.utils.Utils;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;

@Mixin(CreeperEntity.class)
public final class CreeperEffectMixin {

  /**
   * @author PulseBeat_02
   * @reason remove area effect clouds
   */
  @Overwrite
  private void spawnEffectsCloud() {
    final CreeperEntity entity = (CreeperEntity) (Object) this;
    final BlockPos pos = entity.getBlockPos();
    final int x = pos.getX();
    final int z = pos.getZ();
    final Continent continent = Continent.AF;
    if (Utils.withinContinent(continent, x, z)) {
      return;
    }
    final Collection<StatusEffectInstance> collection = entity.getStatusEffects();
    if (!collection.isEmpty()) {
      final AreaEffectCloudEntity areaEffectCloudEntity =
          new AreaEffectCloudEntity(entity.getWorld(), entity.getX(), entity.getY(), entity.getZ());
      areaEffectCloudEntity.setRadius(2.5f);
      areaEffectCloudEntity.setRadiusOnUse(-0.5f);
      areaEffectCloudEntity.setWaitTime(10);
      areaEffectCloudEntity.setDuration(areaEffectCloudEntity.getDuration() / 2);
      areaEffectCloudEntity.setRadiusGrowth(
          -areaEffectCloudEntity.getRadius() / (float) areaEffectCloudEntity.getDuration());
      for (final StatusEffectInstance statusEffectInstance : collection) {
        areaEffectCloudEntity.addEffect(new StatusEffectInstance(statusEffectInstance));
      }
      entity.getWorld().spawnEntity(areaEffectCloudEntity);
    }
  }
}
