package com.rogueknyt.infinknyt.item.custom;

import com.rogueknyt.infinknyt.entity.custom.FrostwoodThrowingKnifeEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FrostwoodThrowingKnifeItem extends Item {
    private static final int COOLDOWN_TICKS = 12;

    public FrostwoodThrowingKnifeItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);

        world.playSound(
                null,
                user.getX(),
                user.getY(),
                user.getZ(),
                SoundEvents.ITEM_TRIDENT_THROW,
                SoundCategory.PLAYERS,
                0.6F,
                1.6F
        );

        user.getItemCooldownManager().set(this, COOLDOWN_TICKS);

        if (!world.isClient()) {
            FrostwoodThrowingKnifeEntity knife = new FrostwoodThrowingKnifeEntity(world, user);

            ItemStack thrownStack = stack.copy();
            thrownStack.setCount(1);
            knife.setItem(thrownStack);

            knife.setVelocity(
                    user,
                    user.getPitch(),
                    user.getYaw(),
                    0.0F,
                    1.6F,
                    1.0F
            );

            world.spawnEntity(knife);
        }

        user.incrementStat(Stats.USED.getOrCreateStat(this));

        if (!user.getAbilities().creativeMode) {
            stack.decrement(1);
        }

        return TypedActionResult.success(stack, world.isClient());
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("tooltip.infinknyt.frostwood_throwing_knife.damage")
                .formatted(Formatting.GRAY));

        tooltip.add(Text.translatable("tooltip.infinknyt.frostwood_throwing_knife.slowness")
                .formatted(Formatting.AQUA));

        tooltip.add(Text.translatable("tooltip.infinknyt.frostwood_throwing_knife.consumable")
                .formatted(Formatting.DARK_GRAY));

        super.appendTooltip(stack, world, tooltip, context);
    }
}