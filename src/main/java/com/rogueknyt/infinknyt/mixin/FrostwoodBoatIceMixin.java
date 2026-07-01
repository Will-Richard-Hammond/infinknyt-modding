package com.rogueknyt.infinknyt.mixin;

import com.rogueknyt.infinknyt.block.entity.ModBoats;
import com.terraformersmc.terraform.boat.api.TerraformBoatType;
import com.terraformersmc.terraform.boat.api.TerraformBoatTypeRegistry;
import com.terraformersmc.terraform.boat.impl.entity.TerraformBoatHolder;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FrostedIceBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BoatEntity.class)
public abstract class FrostwoodBoatIceMixin {
    private static final int FROSTWOOD_BOAT_ICE_RADIUS = 3;

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

    private static void infinknyt$freezeNearbyWater(BoatEntity boat, World world) {
        BlockState frostedIce = Blocks.FROSTED_ICE.getDefaultState();
        BlockPos center = boat.getBlockPos();

        BlockPos.Mutable mutable = new BlockPos.Mutable();
        BlockPos.Mutable above = new BlockPos.Mutable();

        for (int yOffset = -1; yOffset <= 1; yOffset++) {
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

                    if (currentState == FrostedIceBlock.getMeltedState()
                            && frostedIce.canPlaceAt(world, mutable)
                            && world.canPlace(frostedIce, mutable, ShapeContext.absent())) {
                        BlockPos target = mutable.toImmutable();

                        world.setBlockState(target, frostedIce);
                        world.scheduleBlockTick(
                                target,
                                Blocks.FROSTED_ICE,
                                MathHelper.nextInt(world.getRandom(), 60, 120)
                        );
                    }
                }
            }
        }
    }
}