package com.rogueknyt.infinknyt.block.entity;

import com.rogueknyt.infinknyt.InfinKnyt;
import com.rogueknyt.infinknyt.block.ModBlocks;
import com.rogueknyt.infinknyt.item.ModItems;
import com.terraformersmc.terraform.boat.api.TerraformBoatType;
import com.terraformersmc.terraform.boat.api.TerraformBoatTypeRegistry;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

public class ModBoats {
    public static final Identifier FROSTWOOD_BOAT_ID =
            new Identifier(InfinKnyt.MOD_ID, "frostwood_boat");

    public static final Identifier FROSTWOOD_CHEST_BOAT_ID =
            new Identifier(InfinKnyt.MOD_ID, "frostwood_chest_boat");

    public static final RegistryKey<TerraformBoatType> FROSTWOOD_BOAT_KEY =
            TerraformBoatTypeRegistry.createKey(FROSTWOOD_BOAT_ID);

    public static void registerBoats() {
        TerraformBoatType frostwoodBoat = new TerraformBoatType.Builder()
                .item(ModItems.FROSTWOOD_BOAT)
                .chestItem(ModItems.FROSTWOOD_CHEST_BOAT)
                .planks(ModBlocks.FROSTWOOD_PLANK.asItem())
                .build();

        Registry.register(TerraformBoatTypeRegistry.INSTANCE, FROSTWOOD_BOAT_KEY, frostwoodBoat);
    }
}