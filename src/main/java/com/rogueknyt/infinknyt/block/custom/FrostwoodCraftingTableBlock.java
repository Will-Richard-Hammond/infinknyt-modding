package com.rogueknyt.infinknyt.block.custom;

import com.rogueknyt.infinknyt.screen.FrostwoodCraftingScreenHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.CraftingTableBlock;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class FrostwoodCraftingTableBlock extends CraftingTableBlock {
    private static final Text TITLE = Text.translatable("container.crafting");

    public FrostwoodCraftingTableBlock(Settings settings) {
        super(settings);
    }

    @Override
    public NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos) {
        return new SimpleNamedScreenHandlerFactory(
                (int syncId, PlayerInventory inventory, net.minecraft.entity.player.PlayerEntity player) ->
                        new FrostwoodCraftingScreenHandler(syncId, inventory, ScreenHandlerContext.create(world, pos)),
                TITLE
        );
    }
}