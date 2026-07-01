package com.rogueknyt.infinknyt.item;

import com.rogueknyt.infinknyt.InfinKnyt;
import com.rogueknyt.infinknyt.block.ModBlocks;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroups {
    public static final ItemGroup INFINKNYT_ITEMS_GROUP = Registry.register(Registries.ITEM_GROUP,
            Identifier.of(InfinKnyt.MOD_ID, "infinknyt_items"),
            FabricItemGroup.builder().icon(() -> new ItemStack(ModItems.FROSTWOOD_THROWING_KNIFE))
                    .displayName(Text.translatable("itemgroup.infinknyt.infinknyt_items"))
                    .entries((displayContext, entries) -> {
                        entries.add(ModItems.FROSTWOOD_THROWING_KNIFE);
                        entries.add(ModItems.IRON_BILLET);
                        entries.add(ModItems.HEATED_IRON_BILLET);

                    }).build());

    public static final ItemGroup INFINKNYT_BLOCKS_GROUP = Registry.register(Registries.ITEM_GROUP,
            Identifier.of(InfinKnyt.MOD_ID, "infinknyt_blocks"),
            FabricItemGroup.builder().icon(() -> new ItemStack(ModBlocks.FROSTWOOD_PLANK))
                    .displayName(Text.translatable("itemgroup.infinknyt.infinknyt_blocks"))
                    .entries((displayContext, entries) -> {
                        entries.add(ModBlocks.FROSTWOOD_PLANK);
                        entries.add(ModBlocks.FROSTWOOD_LOG);
                        entries.add(ModBlocks.STRIPPED_FROSTWOOD_LOG);
                        entries.add(ModBlocks.STRIPPED_FROSTWOOD_WOOD);
                        entries.add(ModBlocks.FROSTWOOD_WOOD);
                        entries.add(ModBlocks.FROSTWOOD_CRAFTING_TABLE);
                        entries.add(ModBlocks.BLACKSMITH_FORGE);

                    }).build());

    public static void registerItemGroups() {
        InfinKnyt.LOGGER.info("Registering Item Groups for " + InfinKnyt.MOD_ID);
    }
}