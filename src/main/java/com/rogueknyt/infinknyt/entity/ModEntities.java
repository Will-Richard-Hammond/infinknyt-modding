package com.rogueknyt.infinknyt.entity;

import com.rogueknyt.infinknyt.InfinKnyt;
import com.rogueknyt.infinknyt.entity.custom.FrostwoodThrowingKnifeEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEntities {

    public static final EntityType<FrostwoodThrowingKnifeEntity> FROSTWOOD_THROWING_KNIFE =
            Registry.register(
                    Registries.ENTITY_TYPE,
                    new Identifier(InfinKnyt.MOD_ID, "frostwood_throwing_knife"),
                    FabricEntityTypeBuilder.<FrostwoodThrowingKnifeEntity>create(
                                    SpawnGroup.MISC,
                                    FrostwoodThrowingKnifeEntity::new
                            )
                            .dimensions(EntityDimensions.fixed(0.25F, 0.25F))
                            .trackRangeBlocks(4)
                            .trackedUpdateRate(10)
                            .build()
            );

    public static void registerModEntities() {
        InfinKnyt.LOGGER.info("Registering Mod Entities for " + InfinKnyt.MOD_ID);
    }
}