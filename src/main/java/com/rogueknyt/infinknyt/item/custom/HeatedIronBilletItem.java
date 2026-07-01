package com.rogueknyt.infinknyt.item.custom;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class HeatedIronBilletItem extends Item {
    public HeatedIronBilletItem(Settings settings) {
        super(settings);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (world.isClient()) {
            return;
        }

        if (!(entity instanceof PlayerEntity player)) {
            return;
        }

        if (player.isCreative() || player.isSpectator()) {
            return;
        }

        // Damage once every 2 seconds
        if (player.age % 40 == 0) {
            player.damage(player.getDamageSources().onFire(), 1.0f);
        }
    }
}