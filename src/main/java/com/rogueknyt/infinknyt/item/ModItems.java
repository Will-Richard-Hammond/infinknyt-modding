package com.rogueknyt.infinknyt.item;

import com.rogueknyt.infinknyt.InfinKnyt;
import com.rogueknyt.infinknyt.item.custom.HeatedIronBilletItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import com.rogueknyt.infinknyt.block.entity.ModBoats;
import com.terraformersmc.terraform.boat.api.item.TerraformBoatItemHelper;

public class ModItems {

    public static final Item FROSTWOOD_THROWING_KNIFE = registerItem("frostwood_throwing_knife", new Item(new Item.Settings()));
    public static final Item IRON_BILLET = registerItem("iron_billet", new Item(new FabricItemSettings()));
    public static final Item HEATED_IRON_BILLET = registerItem("heated_iron_billet", new HeatedIronBilletItem(new Item.Settings()));
    public static final Item FROSTWOOD_BOAT =
            TerraformBoatItemHelper.registerBoatItem(
                    ModBoats.FROSTWOOD_BOAT_ID,
                    ModBoats.FROSTWOOD_BOAT_KEY,
                    false
            );

    public static final Item FROSTWOOD_CHEST_BOAT =
            TerraformBoatItemHelper.registerBoatItem(
                    ModBoats.FROSTWOOD_CHEST_BOAT_ID,
                    ModBoats.FROSTWOOD_BOAT_KEY,
                    true
            );

    //foods
    public static final Item FROST_APPLE = registerItem("frost_apple", new Item(new FabricItemSettings().food(ModFoodComponents.FROST_APPLE)));

    //end of foods


    //helper methods
    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(InfinKnyt.MOD_ID, name), item);
    }
    public static void registerModItems(){
        InfinKnyt.LOGGER.info("Registering Mod Items for" + InfinKnyt.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> {
            entries.add(FROSTWOOD_THROWING_KNIFE);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries -> {
            entries.add(IRON_BILLET);
            entries.add(HEATED_IRON_BILLET);
        });
    }
}
