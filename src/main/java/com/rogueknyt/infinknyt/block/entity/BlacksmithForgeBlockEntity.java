package com.rogueknyt.infinknyt.block.entity;

import com.rogueknyt.infinknyt.item.ModItems;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlacksmithForgeBlockEntity extends BlockEntity {
    private ItemStack storedStack = ItemStack.EMPTY;

    private int progress = 0;
    private int maxProgress = 200; // 10 seconds, because 20 ticks = 1 second

    public BlacksmithForgeBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.BLACKSMITH_FORGE_BLOCK_ENTITY, pos, state);
    }

    public static void tick(World world, BlockPos pos, BlockState state, BlacksmithForgeBlockEntity blockEntity) {
        if (world.isClient()) {
            return;
        }

        if (blockEntity.hasBillet() && !blockEntity.isHeated()) {
            blockEntity.progress++;

            if (blockEntity.progress >= blockEntity.maxProgress) {
                blockEntity.progress = blockEntity.maxProgress;
            }

            markDirty(world, pos, state);
        }
    }

    public boolean hasBillet() {
        return !storedStack.isEmpty();
    }

    public boolean isHeated() {
        return hasBillet() && progress >= maxProgress;
    }

    public boolean insertBillet(ItemStack stack) {
        if (hasBillet()) {
            return false;
        }

        if (!stack.isOf(ModItems.IRON_BILLET)) {
            return false;
        }

        storedStack = new ItemStack(ModItems.IRON_BILLET, 1);
        progress = 0;
        markDirty();

        return true;
    }

    public ItemStack removeHeatedBillet() {
        if (!isHeated()) {
            return ItemStack.EMPTY;
        }

        storedStack = ItemStack.EMPTY;
        progress = 0;
        markDirty();

        return new ItemStack(ModItems.HEATED_IRON_BILLET, 1);
    }

    public int getProgressPercent() {
        if (maxProgress <= 0) {
            return 0;
        }

        return (progress * 100) / maxProgress;
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);

        nbt.put("StoredStack", storedStack.writeNbt(new NbtCompound()));
        nbt.putInt("Progress", progress);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);

        storedStack = ItemStack.fromNbt(nbt.getCompound("StoredStack"));
        progress = nbt.getInt("Progress");
    }
}