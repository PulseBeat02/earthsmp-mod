package io.github.pulsebeat02.earthsmp.mixin;

import com.mojang.datafixers.util.Pair;
import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.PoweredRailBlock;
import net.minecraft.block.enums.RailShape;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.entity.vehicle.AbstractMinecartEntity.Type;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
import java.util.function.Supplier;

@Mixin(
    AbstractMinecartEntity
        .class) // lower value, higher priority - apply first so other mods can still mixin
public abstract class MinecartVelocityMixin extends Entity {
  private MinecartVelocityMixin(
      @NotNull final EntityType<?> type, @NotNull final World world) {
    super(type, world);
  }

  @Shadow
  protected abstract boolean willHitBlockAt(@NotNull final BlockPos pos);

  @Shadow
  public abstract Vec3d snapPositionToRail(final double x, final double y, final double z);

  @Shadow
  protected abstract void applySlowdown();

  @Shadow
  protected abstract double getMaxSpeed();

  @Shadow
  public abstract Type getMinecartType();

  @Shadow
  private static @NotNull Pair<Vec3i, Vec3i> getAdjacentRailPositionsByShape(
      @NotNull final RailShape shape) {
    return Pair.of(Direction.NORTH.getVector(), Direction.SOUTH.getVector());
  }

  @Unique
  private static boolean isEligibleFastRail(@NotNull final BlockState state) {
    return state.isOf(Blocks.RAIL)
        || (state.isOf(Blocks.POWERED_RAIL) && state.get(PoweredRailBlock.POWERED));
  }

  @Unique
  private static RailShape getRailShape(@NotNull final BlockState state) {
    if (!(state.getBlock() instanceof final AbstractRailBlock railBlock)) {
      throw new IllegalArgumentException("No rail shape found");
    }

    return state.get(railBlock.getShapeProperty());
  }

  @Inject(at = @At("HEAD"), method = "moveOnRail", cancellable = true)
  private void velocityChange(
      @NotNull final BlockPos pos,
      @NotNull final BlockState state,
      @NotNull final CallbackInfo ci) {

    // We only change logic for rideable minecarts so we don't break hopper/chest minecart creations
    if (this.getMinecartType() != Type.RIDEABLE) {
      return;
    }

    // We only change logic when the minecart is currently being ridden by a living entity
    // (player/villager/mob)
    final boolean hasLivingRider = this.getFirstPassenger() instanceof LivingEntity;
    if (!hasLivingRider) {
      return;
    }

    this.modifiedMoveOnRail(pos, state);
    ci.cancel();
  }

  @Unique
  private void modifiedMoveOnRail(@NotNull final BlockPos pos, @NotNull final BlockState state) {
    this.onLanding();
    double d = this.getX();
    double e = this.getY();
    double f = this.getZ();

    final Vec3d vec3d = this.snapPositionToRail(d, e, f);

    e = pos.getY();
    boolean onPoweredRail = false;
    boolean onBrakeRail = false;
    if (state.isOf(Blocks.POWERED_RAIL)) {
      onPoweredRail = state.get(PoweredRailBlock.POWERED);
      onBrakeRail = !onPoweredRail;
    }

    double g = 0.0078125D;
    if (this.isTouchingWater()) {
      g *= 0.2D;
    }

    Vec3d velocity = this.getVelocity();
    final RailShape railShape = getRailShape(state);
    switch (railShape) {
      case ASCENDING_EAST -> {
        this.setVelocity(velocity.add(-g, 0.0D, 0.0D));
        ++e;
      }
      case ASCENDING_WEST -> {
        this.setVelocity(velocity.add(g, 0.0D, 0.0D));
        ++e;
      }
      case ASCENDING_NORTH -> {
        this.setVelocity(velocity.add(0.0D, 0.0D, g));
        ++e;
      }
      case ASCENDING_SOUTH -> {
        this.setVelocity(velocity.add(0.0D, 0.0D, -g));
        ++e;
      }
    }

    velocity = this.getVelocity();
    final Pair<Vec3i, Vec3i> adjacentRailPositions = getAdjacentRailPositionsByShape(railShape);
    final Vec3i adjacentRail1RelPos = adjacentRailPositions.getFirst();
    final Vec3i adjacentRail2RelPos = adjacentRailPositions.getSecond();
    double h = adjacentRail2RelPos.getX() - adjacentRail1RelPos.getX();
    double i = adjacentRail2RelPos.getZ() - adjacentRail1RelPos.getZ();
    final double j = Math.sqrt(h * h + i * i);
    final double k = velocity.x * h + velocity.z * i;
    if (k < 0.0D) {
      h = -h;
      i = -i;
    }

    final double vanillaMaxHorizontalMovementPerTick = 0.4D;
    final double horizontalMomentumPerTick = this.getVelocity().horizontalLength();

    final Supplier<Double> calculateMaxHorizontalMovementPerTick =
        () -> {
          double fallback = this.getMaxSpeed();

          if (!this.hasPassengers()) {
            return fallback;
          }

          if (horizontalMomentumPerTick < vanillaMaxHorizontalMovementPerTick) {
            return fallback;
          }

          if (!isEligibleFastRail(state)) {
            return fallback;
          }

          for (final Vec3i directlyAdjDiff :
              List.of(adjacentRailPositions.getFirst(), adjacentRailPositions.getSecond())) {
            final BlockPos directlyAdjPos = pos.add(directlyAdjDiff);
            final BlockState directlyAdjState = this.getWorld().getBlockState(directlyAdjPos);

            if (!isEligibleFastRail(directlyAdjState)) {
              return fallback;
            }
          }

          final double fallbackSpeedFactor = 1.15D;
          // Allow faster fallback speed when there is rail around the cart, but we have rail shape
          // changes
          fallback *= fallbackSpeedFactor;

          final boolean hasEligibleShape =
              railShape == RailShape.NORTH_SOUTH || railShape == RailShape.EAST_WEST;
          if (!hasEligibleShape) {
            return fallback;
          }

          final AtomicInteger eligibleNeighbors = new AtomicInteger();

          final HashSet<BlockPos> checkedPositions = new HashSet<>();
          checkedPositions.add(pos);

          final BiFunction<BlockPos, RailShape, ArrayList<Pair<BlockPos, RailShape>>>
              checkNeighbors =
                  (checkPos, checkRailShape) -> {
                    final Pair<Vec3i, Vec3i> adjDiffPair =
                        getAdjacentRailPositionsByShape(checkRailShape);

                    final ArrayList<Pair<BlockPos, RailShape>> newNeighbors = new ArrayList<>();

                    for (final Vec3i adjDiff :
                        List.of(adjDiffPair.getFirst(), adjDiffPair.getSecond())) {
                      final BlockPos nborPos = checkPos.add(adjDiff);

                      if (checkedPositions.contains(nborPos)) {
                        continue;
                      }

                      final BlockState nborState = this.getWorld().getBlockState(nborPos);
                      if (!isEligibleFastRail(nborState)) {
                        return new ArrayList<>();
                      }

                      final RailShape nborShape = getRailShape(nborState);
                      if (nborShape != railShape) {
                        return new ArrayList<>();
                      }

                      checkedPositions.add(nborPos);
                      eligibleNeighbors.incrementAndGet();
                      // Adding the neighbor rail shape currently has no use, since we abort on rail
                      // shape change anyway
                      // Code stays as is for now so we can differentiate between types of rail
                      // shape changes later
                      newNeighbors.add(Pair.of(nborPos, nborShape));
                    }

                    return newNeighbors;
                  };

          final ArrayList<Pair<BlockPos, RailShape>> newNeighbors =
              checkNeighbors.apply(pos, railShape);

          while (!newNeighbors.isEmpty() && eligibleNeighbors.get() < 16) {
            final ArrayList<Pair<BlockPos, RailShape>> tempNewNeighbors =
                new ArrayList<>(newNeighbors);
            newNeighbors.clear();

            for (final Pair<BlockPos, RailShape> newNeighbor : tempNewNeighbors) {
              final ArrayList<Pair<BlockPos, RailShape>> result =
                  checkNeighbors.apply(newNeighbor.getFirst(), newNeighbor.getSecond());

              if (result.isEmpty()) {
                newNeighbors.clear();
                break;
              }

              newNeighbors.addAll(result);
            }
          }

          final int eligibleForwardRailTrackCount = eligibleNeighbors.get() / 2;

          if (eligibleForwardRailTrackCount <= 1) {
            return fallback;
          }

          return (2.01D + eligibleForwardRailTrackCount * 4.0D) / 20.0D;
        };

    final double maxHorizontalMovementPerTick = calculateMaxHorizontalMovementPerTick.get();
    final double maxHorizontalMomentumPerTick = Math.max(maxHorizontalMovementPerTick * 5.0D, 4.2D);

    final double l = Math.min(maxHorizontalMomentumPerTick, velocity.horizontalLength());
    this.setVelocity(new Vec3d(l * h / j, velocity.y, l * i / j));

    final Entity entity = this.getFirstPassenger();
    if (entity instanceof PlayerEntity) {
      final Vec3d playerVelocity = entity.getVelocity();
      final double m = playerVelocity.horizontalLengthSquared();
      final double n = this.getVelocity().horizontalLengthSquared();
      if (m > 1.0E-4D && n < 0.01D) {
        this.setVelocity(
            this.getVelocity().add(playerVelocity.x * 0.1D, 0.0D, playerVelocity.z * 0.1D));
        onBrakeRail = false;
      }
    }

    double p;
    if (onBrakeRail) {
      p = this.getVelocity().horizontalLength();
      if (p < 0.03D) {
        this.setVelocity(Vec3d.ZERO);
      } else {
        double brakeFactor = 0.5D;

        if (horizontalMomentumPerTick > 4.0D * vanillaMaxHorizontalMovementPerTick) {
          brakeFactor =
              Math.pow(
                  brakeFactor,
                  1.0D
                      + ((horizontalMomentumPerTick - 3.99D * vanillaMaxHorizontalMovementPerTick)
                          / 1.2D));
        }

        this.setVelocity(this.getVelocity().multiply(brakeFactor, 0.0D, brakeFactor));
      }
    }

    p = (double) pos.getX() + 0.5D + (double) adjacentRail1RelPos.getX() * 0.5D;
    final double q = (double) pos.getZ() + 0.5D + (double) adjacentRail1RelPos.getZ() * 0.5D;
    final double r = (double) pos.getX() + 0.5D + (double) adjacentRail2RelPos.getX() * 0.5D;
    final double s = (double) pos.getZ() + 0.5D + (double) adjacentRail2RelPos.getZ() * 0.5D;
    h = r - p;
    i = s - q;
    final double x;
    double v;
    double w;
    if (h == 0.0D) {
      x = f - (double) pos.getZ();
    } else if (i == 0.0D) {
      x = d - (double) pos.getX();
    } else {
      v = d - p;
      w = f - q;
      x = (v * h + w * i) * 2.0D;
    }

    d = p + h * x;
    f = q + i * x;
    this.setPosition(d, e, f);
    v = this.hasPassengers() ? 0.75D : 1.0D;

    w = maxHorizontalMovementPerTick;

    velocity = this.getVelocity();
    final Vec3d movement =
        new Vec3d(
            MathHelper.clamp(v * velocity.x, -w, w), 0.0D, MathHelper.clamp(v * velocity.z, -w, w));

    this.move(MovementType.SELF, movement);

    if (adjacentRail1RelPos.getY() != 0
        && MathHelper.floor(this.getX()) - pos.getX() == adjacentRail1RelPos.getX()
        && MathHelper.floor(this.getZ()) - pos.getZ() == adjacentRail1RelPos.getZ()) {
      this.setPosition(this.getX(), this.getY() + (double) adjacentRail1RelPos.getY(), this.getZ());
    } else if (adjacentRail2RelPos.getY() != 0
        && MathHelper.floor(this.getX()) - pos.getX() == adjacentRail2RelPos.getX()
        && MathHelper.floor(this.getZ()) - pos.getZ() == adjacentRail2RelPos.getZ()) {
      this.setPosition(this.getX(), this.getY() + (double) adjacentRail2RelPos.getY(), this.getZ());
    }

    this.applySlowdown();
    final Vec3d vec3d4 = this.snapPositionToRail(this.getX(), this.getY(), this.getZ());
    Vec3d vec3d7;
    double af;
    if (vec3d4 != null && vec3d != null) {
      final double aa = (vec3d.y - vec3d4.y) * 0.05D;
      vec3d7 = this.getVelocity();
      af = vec3d7.horizontalLength();
      if (af > 0.0D) {
        this.setVelocity(vec3d7.multiply((af + aa) / af, 1.0D, (af + aa) / af));
      }

      this.setPosition(this.getX(), vec3d4.y, this.getZ());
    }

    final int ac = MathHelper.floor(this.getX());
    final int ad = MathHelper.floor(this.getZ());
    if (ac != pos.getX() || ad != pos.getZ()) {
      vec3d7 = this.getVelocity();
      af = vec3d7.horizontalLength();
      this.setVelocity(
          af * MathHelper.clamp(ac - pos.getX(), -1.0D, 1.0D),
          vec3d7.y,
          af * MathHelper.clamp(ad - pos.getZ(), -1.0D, 1.0D));
    }

    if (onPoweredRail) {
      vec3d7 = this.getVelocity();
      final double momentum = vec3d7.horizontalLength();
      final double basisAccelerationPerTick = 0.021D;
      if (momentum > 0.01D) {

        if (this.hasPassengers()) {
          // Based on a 10 ticks per second basis spent per powered block we calculate a fair
          // acceleration per tick
          // due to spending less ticks per powered block on higher speeds (and even skipping
          // blocks)
          final double basisTicksPerSecond = 10.0D;
          // Tps = Ticks per second
          final double tickMovementForBasisTps = 1.0D / basisTicksPerSecond;
          final double maxSkippedBlocksToConsider = 3.0D;

          double acceleration = basisAccelerationPerTick;
          final double distanceMovedHorizontally = movement.horizontalLength();

          if (distanceMovedHorizontally > tickMovementForBasisTps) {
            acceleration *=
                Math.min(
                    (1.0D + maxSkippedBlocksToConsider) * basisTicksPerSecond,
                    distanceMovedHorizontally / tickMovementForBasisTps);

            // Add progressively slower (or faster) acceleration for higher speeds;
            final double highspeedFactor =
                1.0D
                    + MathHelper.clamp(
                        -0.45D
                            * (distanceMovedHorizontally
                                / tickMovementForBasisTps
                                / basisTicksPerSecond),
                        -0.7D,
                        2.0D);
            acceleration *= highspeedFactor;
          }
          this.setVelocity(
              vec3d7.add(
                  acceleration * (vec3d7.x / momentum),
                  0.0D,
                  acceleration * (vec3d7.z / momentum)));
        } else {
          this.setVelocity(
              vec3d7.add(vec3d7.x / momentum * 0.06D, 0.0D, vec3d7.z / momentum * 0.06D));
        }

      } else {
        final Vec3d vec3d8 = this.getVelocity();
        double ah = vec3d8.x;
        double ai = vec3d8.z;
        final double railStopperAcceleration = basisAccelerationPerTick * 16.0D;
        if (railShape == RailShape.EAST_WEST) {
          if (this.willHitBlockAt(pos.west())) {
            ah = railStopperAcceleration;
          } else if (this.willHitBlockAt(pos.east())) {
            ah = -railStopperAcceleration;
          }
        } else {
          if (railShape != RailShape.NORTH_SOUTH) {
            return;
          }

          if (this.willHitBlockAt(pos.north())) {
            ai = railStopperAcceleration;
          } else if (this.willHitBlockAt(pos.south())) {
            ai = -railStopperAcceleration;
          }
        }

        this.setVelocity(ah, vec3d8.y, ai);
      }
    }
  }
}
