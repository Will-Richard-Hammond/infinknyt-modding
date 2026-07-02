package com.rogueknyt.infinknyt.item;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.FoodComponent;

public class ModFoodComponents {
    public static final FoodComponent FROST_APPLE = new FoodComponent.Builder()
            .hunger(4)
            .saturationModifier(1)
            .statusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 200), 0.25f)
            .build();
}
