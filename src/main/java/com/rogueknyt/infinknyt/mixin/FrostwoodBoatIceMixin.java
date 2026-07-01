package com.rogueknyt.infinknyt.mixin;

import com.rogueknyt.infinknyt.block.entity.ModBoats;
import com.rogueknyt.infinknyt.world.FrostwoodBoatIceManager;
import com.terraformersmc.terraform.boat.api.TerraformBoatType;
import com.terraformersmc.terraform.boat.api.TerraformBoatTypeRegistry;
import com.terraformersmc.terraform.boat.impl.entity.TerraformBoatHolder;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FrostedIceBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BoatEntity.class)
public abstract class FrostwoodBoatIceMixin {
    private static final int FROSTWOOD_BOAT_ICE_RADIUS = 3;
    private static final int FROSTWOOD_BOAT_ICE_LIFETIME_TICKS = 160;
    private static final double FROSTWOOD_BOAT_ICE_SAMPLE_SPACING = 0.45D;
    private static final double FROSTWOOD_BOAT_WATER_LIFT_IMPULSE = 0.08D;
    private static final double FROSTWOOD_BOAT_WATER_LIFT_MAX_UPWARD_SPEED = 0.18D;
    private static final double FROSTWOOD_BOAT_WATER_LIFT_ACTIVE_HEIGHT = 0.55D;
    private static final int FROSTWOOD_BOAT_WATER_LIFT_RADIUS = 2;

    @Inject(method = "tick", at = @At("HEAD"))
    private void infinknyt$liftFrostwoodBoatNearWater(CallbackInfo ci) {
        BoatEntity boat = (BoatEntity) (Object) this;
        World world = boat.getWorld();

        if (world.isClient()) {
            return;
        }

        if (!infinknyt$isFrostwoodBoat(boat)) {
            return;
        }

        infinknyt$applyWaterLiftImpulse(boat, world);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void infinknyt$freezeWaterAroundFrostwoodBoat(CallbackInfo ci) {
        BoatEntity boat = (BoatEntity) (Object) this;
        World world = boat.getWorld();

        if (world.isClient()) {
            return;
        }

        if (!infinknyt$isFrostwoodBoat(boat)) {
            return;
        }

        infinknyt$freezeNearbyWater(boat, world);
    }

    private static boolean infinknyt$isFrostwoodBoat(BoatEntity boat) {
        if (!(boat instanceof TerraformBoatHolder holder) || !holder.hasValidTerraformBoat()) {
            return false;
        }

        TerraformBoatType boatType = holder.getTerraformBoat();
        Identifier boatId = TerraformBoatTypeRegistry.INSTANCE.getId(boatType);

        return ModBoats.FROSTWOOD_BOAT_ID.equals(boatId);
    }

    private static void infinknyt$applyWaterLiftImpulse(BoatEntity boat, World world) {
        double highestWaterTop = infinknyt$getHighestNearbyWaterTop(boat, world);

        if (Double.isNaN(highestWaterTop)) {
            return;
        }

        if (boat.getY() > highestWaterTop + FROSTWOOD_BOAT_WATER_LIFT_ACTIVE_HEIGHT) {
            return;
        }

        Vec3d velocity = boat.getVelocity();

        if (velocity.y >= FROSTWOOD_BOAT_WATER_LIFT_MAX_UPWARD_SPEED) {
            return;
        }

        double impulse = Math.min(
                FROSTWOOD_BOAT_WATER_LIFT_IMPULSE,
                FROSTWOOD_BOAT_WATER_LIFT_MAX_UPWARD_SPEED - velocity.y
        );

        if (impulse > 0.0D) {
            boat.addVelocity(0.0D, impulse, 0.0D);
        }
    }

    private static double infinknyt$getHighestNearbyWaterTop(BoatEntity boat, World world) {
        Vec3d velocity = boat.getVelocity();
        double horizontalSpeed = Math.sqrt(velocity.x * velocity.x + velocity.z * velocity.z);
        int samples = Math.max(1, MathHelper.ceil(horizontalSpeed / FROSTWOOD_BOAT_ICE_SAMPLE_SPACING));
        double highestWaterTop = Double.NaN;

        for (int sample = 0; sample <= samples; sample++) {
            double progress = sample / (double) samples;
            double x = boat.getX() + velocity.x * progress;
            double z = boat.getZ() + velocity.z * progress;
            BlockPos center = BlockPos.ofFloored(x, boat.getY(), z);
            double sampleWaterTop = infinknyt$getHighestWaterTopAround(world, center);

            if (!Double.isNaN(sampleWaterTop) && (Double.isNaN(highestWaterTop) || sampleWaterTop > highestWaterTop)) {
                highestWaterTop = sampleWaterTop;
            }
        }

        return highestWaterTop;
    }

    private static double infinknyt$getHighestWaterTopAround(World world, BlockPos center) {
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        double highestWaterTop = Double.NaN;

        for (int xOffset = -FROSTWOOD_BOAT_WATER_LIFT_RADIUS; xOffset <= FROSTWOOD_BOAT_WATER_LIFT_RADIUS; xOffset++) {
            for (int zOffset = -FROSTWOOD_BOAT_WATER_LIFT_RADIUS; zOffset <= FROSTWOOD_BOAT_WATER_LIFT_RADIUS; zOffset++) {
                for (int yOffset = -2; yOffset <= 1; yOffset++) {
                    mutable.set(center.getX() + xOffset, center.getY() + yOffset, center.getZ() + zOffset);

                    FluidState fluidState = world.getFluidState(mutable);

                    if (!fluidState.isIn(FluidTags.WATER)) {
                        continue;
                    }

                    double waterTop = mutable.getY() + fluidState.getHeight(world, mutable);

                    if (Double.isNaN(highestWaterTop) || waterTop > highestWaterTop) {
                        highestWaterTop = waterTop;
                    }
                }
            }
        }

        return highestWaterTop;
    }

    private static void infinknyt$freezeNearbyWater(BoatEntity boat, World world) {
        if (!(world instanceof ServerWorld serverWorld)) {
            return;
        }

        Vec3d start = new Vec3d(boat.prevX, boat.prevY, boat.prevZ);
        Vec3d end = boat.getPos();
        double distance = start.distanceTo(end);
        int samples = Math.max(1, MathHelper.ceil(distance / FROSTWOOD_BOAT_ICE_SAMPLE_SPACING));

        for (int sample = 0; sample <= samples; sample++) {
            double progress = sample / (double) samples;
            double x = MathHelper.lerp(progress, start.x, end.x);
            double y = MathHelper.lerp(progress, start.y, end.y);
            double z = MathHelper.lerp(progress, start.z, end.z);

            infinknyt$freezeWaterAt(serverWorld, BlockPos.ofFloored(x, y, z));
        }
    }

    private static void infinknyt$freezeWaterAt(ServerWorld world, BlockPos center) {
        BlockState frostedIce = Blocks.FROSTED_ICE.getDefaultState().with(FrostedIceBlock.AGE, 0);

        BlockPos.Mutable mutable = new BlockPos.Mutable();
        BlockPos.Mutable above = new BlockPos.Mutable();

        for (int yOffset = -2; yOffset <= 1; yOffset++) {
            int y = center.getY() + yOffset;

            for (int xOffset = -FROSTWOOD_BOAT_ICE_RADIUS; xOffset <= FROSTWOOD_BOAT_ICE_RADIUS; xOffset++) {
                for (int zOffset = -FROSTWOOD_BOAT_ICE_RADIUS; zOffset <= FROSTWOOD_BOAT_ICE_RADIUS; zOffset++) {
                    if (xOffset * xOffset + zOffset * zOffset > FROSTWOOD_BOAT_ICE_RADIUS * FROSTWOOD_BOAT_ICE_RADIUS) {
                        continue;
                    }

                    mutable.set(center.getX() + xOffset, y, center.getZ() + zOffset);
                    above.set(mutable.getX(), mutable.getY() + 1, mutable.getZ());

                    if (!world.getBlockState(above).isAir()) {
                        continue;
                    }

                    BlockState currentState = world.getBlockState(mutable);
                    FluidState fluidState = world.getFluidState(mutable);

                    if (currentState.isOf(Blocks.WATER)
                            && fluidState.isStill()
                            && frostedIce.canPlaceAt(world, mutable)
                            && world.canPlace(frostedIce, mutable, ShapeContext.absent())) {
                        BlockPos target = mutable.toImmutable();

                        world.setBlockState(target, frostedIce);
                        world.scheduleBlockTick(
                                target,
                                Blocks.FROSTED_ICE,
                                MathHelper.nextInt(world.getRandom(), 20, 40)
                        );
                        FrostwoodBoatIceManager.track(world, target, FROSTWOOD_BOAT_ICE_LIFETIME_TICKS);
                    }
                }
            }
        }
    }
}
