package com.rogueknyt.infinknyt.block;

import com.rogueknyt.infinknyt.InfinKnyt;
import com.rogueknyt.infinknyt.block.custom.BlacksmithForgeBlock;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.registry.StrippableBlockRegistry;
import com.rogueknyt.infinknyt.block.custom.FrostwoodCraftingTableBlock;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.PillarBlock;
import net.minecraft.block.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

public class ModBlocks {

    public static final Block FROSTWOOD_PLANK = registerBlock("frostwood_plank",
            new Block(AbstractBlock.Settings
                    .create()
                    .strength(2f, 3f)
                    .sounds(BlockSoundGroup.GLASS)
                    .nonOpaque()

            )
    );
    public static final Block FROSTWOOD_LOG = registerBlock("frostwood_log",
            new PillarBlock(AbstractBlock.Settings
                    .create()
                    .strength(2f, 3f)
                    .sounds(BlockSoundGroup.GLASS)
                    .nonOpaque()

            )
    );
    public static final Block STRIPPED_FROSTWOOD_LOG = registerBlock("stripped_frostwood_log",
            new PillarBlock(AbstractBlock.Settings
                    .create()
                    .strength(2f, 3f)
                    .sounds(BlockSoundGroup.GLASS)
                    .nonOpaque()

            )
    );
    public static final Block FROSTWOOD_CRAFTING_TABLE = registerBlock("frostwood_crafting_table",
            new FrostwoodCraftingTableBlock(AbstractBlock.Settings
                    .create()
                    .strength(2.5f, 3f)
                    .sounds(BlockSoundGroup.GLASS)
                    .nonOpaque()
            )
    );
    public static final Block STRIPPED_FROSTWOOD_WOOD = registerBlock("stripped_frostwood_wood",
            new PillarBlock(AbstractBlock.Settings
                    .create()
                    .strength(2f, 3f)
                    .sounds(BlockSoundGroup.GLASS)
                    .nonOpaque()

            )
    );
    public static final Block FROSTWOOD_WOOD = registerBlock("frostwood_wood",
            new PillarBlock(AbstractBlock.Settings
                    .create()
                    .strength(2f, 3f)
                    .sounds(BlockSoundGroup.GLASS)
                    .nonOpaque()

            )
    );
    public static final Block BLACKSMITH_FORGE = registerBlock("blacksmith_forge",
            new BlacksmithForgeBlock(FabricBlockSettings.copyOf(Blocks.STONE)));


    private static Block registerBlock(String name, Block block){
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, Identifier.of(InfinKnyt.MOD_ID, name), block);
    }

    private static void registerBlockItem(String name, Block block){
        Registry.register(Registries.ITEM, Identifier.of(InfinKnyt.MOD_ID, name),
                            new BlockItem(block, new Item.Settings()));
    }

    private static void addBlocksToFunctionalTab(FabricItemGroupEntries entries) {
        entries.add(BLACKSMITH_FORGE);
    }

    public static void registerModBlocks(){
        InfinKnyt.LOGGER.info("Registering Mod Blocks for " + InfinKnyt.MOD_ID);

        StrippableBlockRegistry.register(FROSTWOOD_LOG, STRIPPED_FROSTWOOD_LOG);
        StrippableBlockRegistry.register(FROSTWOOD_WOOD, STRIPPED_FROSTWOOD_WOOD);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register(entries ->{
            entries.add(ModBlocks.FROSTWOOD_PLANK);
            entries.add(ModBlocks.BLACKSMITH_FORGE);
            entries.add(ModBlocks.FROSTWOOD_LOG);
            entries.add(ModBlocks.STRIPPED_FROSTWOOD_LOG);
            entries.add(ModBlocks.FROSTWOOD_CRAFTING_TABLE);
            entries.add(ModBlocks.STRIPPED_FROSTWOOD_WOOD);
            entries.add(ModBlocks.FROSTWOOD_WOOD);
        } );

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(ModBlocks::addBlocksToFunctionalTab);
    }
}
