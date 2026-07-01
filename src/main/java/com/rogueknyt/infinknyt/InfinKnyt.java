package com.rogueknyt.infinknyt;

import com.rogueknyt.infinknyt.block.ModBlocks;
import com.rogueknyt.infinknyt.block.entity.ModBlockEntities;
import com.rogueknyt.infinknyt.item.ModItems;
import net.fabricmc.api.ModInitializer;

import net.minecraft.util.Identifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InfinKnyt implements ModInitializer {
	public static final String MOD_ID = "infinknyt";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModItems.registerModItems();
		ModBlocks.registerModBlocks();
		ModBlockEntities.registerBlockEntities();
	}

	public static Identifier id(String path) {
		return new Identifier(MOD_ID, path);
	}
}
