package com.rogueknyt.infinknyt.block.entity;

import com.rogueknyt.infinknyt.InfinKnyt;
import com.rogueknyt.infinknyt.block.ModBlocks;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlockEntities {
    public static final BlockEntityType<BlacksmithForgeBlockEntity> BLACKSMITH_FORGE_BLOCK_ENTITY =
            Registry.register(
                    Registries.BLOCK_ENTITY_TYPE,
                    new Identifier(InfinKnyt.MOD_ID, "blacksmith_forge_be"),
                    FabricBlockEntityTypeBuilder.create(
                            BlacksmithForgeBlockEntity::new,
                            ModBlocks.BLACKSMITH_FORGE
                    ).build()
            );

    public static void registerBlockEntities() {
        InfinKnyt.LOGGER.info("Registering Block Entities for " + InfinKnyt.MOD_ID);
    }
}