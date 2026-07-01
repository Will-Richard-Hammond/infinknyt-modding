package com.rogueknyt.infinknyt.world;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FrostedIceBlock;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class FrostwoodBoatIceManager {
    private static final Map<RegistryKey<World>, Map<BlockPos, Long>> TRACKED_ICE = new HashMap<>();

    public static void register() {
        ServerTickEvents.END_WORLD_TICK.register(FrostwoodBoatIceManager::tickWorld);
    }

    public static void track(ServerWorld world, BlockPos pos, int lifetimeTicks) {
        TRACKED_ICE
                .computeIfAbsent(world.getRegistryKey(), key -> new HashMap<>())
                .put(pos.toImmutable(), world.getTime() + lifetimeTicks);
    }

    private static void tickWorld(ServerWorld world) {
        Map<BlockPos, Long> trackedPositions = TRACKED_ICE.get(world.getRegistryKey());

        if (trackedPositions == null || trackedPositions.isEmpty()) {
            return;
        }

        long currentTime = world.getTime();
        Iterator<Map.Entry<BlockPos, Long>> iterator = trackedPositions.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<BlockPos, Long> entry = iterator.next();

            if (currentTime < entry.getValue()) {
                continue;
            }

            BlockPos pos = entry.getKey();

            if (!world.canSetBlock(pos)) {
                continue;
            }

            BlockState state = world.getBlockState(pos);

            if (state.isOf(Blocks.FROSTED_ICE)) {
                world.setBlockState(pos, FrostedIceBlock.getMeltedState());
                world.updateNeighbor(pos, FrostedIceBlock.getMeltedState().getBlock(), pos);
            }

            iterator.remove();
        }

        if (trackedPositions.isEmpty()) {
            TRACKED_ICE.remove(world.getRegistryKey());
        }
    }
}
