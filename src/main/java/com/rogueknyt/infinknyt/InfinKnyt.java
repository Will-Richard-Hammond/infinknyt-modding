package com.rogueknyt.infinknyt;

import com.rogueknyt.infinknyt.block.ModBlocks;
import com.rogueknyt.infinknyt.block.entity.ModBlockEntities;
import com.rogueknyt.infinknyt.block.entity.ModBoats;
import com.rogueknyt.infinknyt.entity.ModEntities;
import com.rogueknyt.infinknyt.item.ModItemGroups;
import com.rogueknyt.infinknyt.item.ModItems;
import com.rogueknyt.infinknyt.util.ModFuels;
import com.rogueknyt.infinknyt.world.FrostwoodBoatIceManager;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.util.Identifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InfinKnyt implements ModInitializer {
	public static final String MOD_ID = "infinknyt";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModItemGroups.registerItemGroups();
		ModItems.registerModItems();
		ModBlocks.registerModBlocks();
		ModBlockEntities.registerBlockEntities();
		ModEntities.registerModEntities();
		ModBoats.registerBoats();
		FrostwoodBoatIceManager.register();

		ModFuels.registerFuels();

	}

	public static Identifier id(String path) {
		return new Identifier(MOD_ID, path);
	}
}
