package com.rogueknyt.infinknyt.screen;

import com.rogueknyt.infinknyt.block.ModBlocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;

public class FrostwoodCraftingScreenHandler extends CraftingScreenHandler {
    private final ScreenHandlerContext context;

    public FrostwoodCraftingScreenHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(syncId, playerInventory, context);
        this.context = context;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return canUse(this.context, player, ModBlocks.FROSTWOOD_CRAFTING_TABLE);
    }
}