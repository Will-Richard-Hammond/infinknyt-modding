package com.rogueknyt.infinknyt.util;

import com.rogueknyt.infinknyt.InfinKnyt;
import com.rogueknyt.infinknyt.block.ModBlocks;
import com.rogueknyt.infinknyt.item.ModItems;
import net.fabricmc.fabric.api.registry.FuelRegistry;

public class ModFuels {
    public static void registerFuels() {
        InfinKnyt.LOGGER.info("Registering Fuels for " + InfinKnyt.MOD_ID);

        // 20 ticks = 1 second
        // 200 ticks = enough to smelt 1 item
        // 300 ticks = same as vanilla planks/logs, smelts 1.5 items

        FuelRegistry.INSTANCE.add(ModBlocks.FROSTWOOD_PLANK, 300);
        FuelRegistry.INSTANCE.add(ModBlocks.FROSTWOOD_LOG, 300);
        FuelRegistry.INSTANCE.add(ModBlocks.STRIPPED_FROSTWOOD_LOG, 300);
        FuelRegistry.INSTANCE.add(ModBlocks.FROSTWOOD_WOOD, 300);
        FuelRegistry.INSTANCE.add(ModBlocks.STRIPPED_FROSTWOOD_WOOD, 300);

        // Optional, if you want the crafting table to burn too
        FuelRegistry.INSTANCE.add(ModBlocks.FROSTWOOD_CRAFTING_TABLE, 300);

        // Optional, if you want the boats to burn
        FuelRegistry.INSTANCE.add(ModItems.FROSTWOOD_BOAT, 1200);
        FuelRegistry.INSTANCE.add(ModItems.FROSTWOOD_CHEST_BOAT, 1200);
    }
}