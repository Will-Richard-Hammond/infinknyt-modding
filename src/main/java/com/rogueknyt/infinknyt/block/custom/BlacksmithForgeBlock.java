package com.rogueknyt.infinknyt.block.custom;

import com.rogueknyt.infinknyt.block.entity.BlacksmithForgeBlockEntity;
import com.rogueknyt.infinknyt.block.entity.ModBlockEntities;
import com.rogueknyt.infinknyt.item.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

public class BlacksmithForgeBlock extends BlockWithEntity {
    public BlacksmithForgeBlock(Settings settings) {
        super(settings);
    }

    @Override
    public net.minecraft.block.BlockRenderType getRenderType(BlockState state) {
        return net.minecraft.block.BlockRenderType.MODEL;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos,
                              PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient()) {
            return ActionResult.SUCCESS;
        }

        BlockEntity blockEntity = world.getBlockEntity(pos);

        if (!(blockEntity instanceof BlacksmithForgeBlockEntity forgeBlockEntity)) {
            return ActionResult.PASS;
        }

        ItemStack heldStack = player.getStackInHand(hand);

        // If forge is empty and player is holding an iron billet, insert it
        if (!forgeBlockEntity.hasBillet() && heldStack.isOf(ModItems.IRON_BILLET)) {
            boolean inserted = forgeBlockEntity.insertBillet(heldStack);

            if (inserted) {
                if (!player.isCreative()) {
                    heldStack.decrement(1);
                }

                player.sendMessage(Text.literal("Iron Billet placed in the forge."), true);
                return ActionResult.SUCCESS;
            }
        }

        // If forge has a heated billet, let the player take it
        if (forgeBlockEntity.isHeated()) {
            ItemStack heatedBillet = forgeBlockEntity.removeHeatedBillet();

            if (!player.getInventory().insertStack(heatedBillet)) {
                player.dropItem(heatedBillet, false);
            }

            player.sendMessage(Text.literal("Heated Iron Billet retrieved."), true);
            return ActionResult.SUCCESS;
        }

        // If forge has a billet, but it is not ready, show progress
        if (forgeBlockEntity.hasBillet()) {
            player.sendMessage(Text.literal("Heating Billet: " + forgeBlockEntity.getProgressPercent() + "%"), true);
            return ActionResult.SUCCESS;
        }

        player.sendMessage(Text.literal("Place an Iron Billet in the forge."), true);
        return ActionResult.SUCCESS;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new BlacksmithForgeBlockEntity(pos, state);
    }


    @Nullable
    @Override
    public <T extends BlockEntity> net.minecraft.block.entity.BlockEntityTicker<T> getTicker(
            World world,
            BlockState state,
            net.minecraft.block.entity.BlockEntityType<T> type) {

        return checkType(type, ModBlockEntities.BLACKSMITH_FORGE_BLOCK_ENTITY,
                BlacksmithForgeBlockEntity::tick);
    }
}