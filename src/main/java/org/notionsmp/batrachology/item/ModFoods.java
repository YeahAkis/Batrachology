package org.notionsmp.batrachology.item;

import net.minecraft.world.food.FoodProperties;

public class ModFoods {
    public static final FoodProperties FROG_BOWL = new FoodProperties.Builder()
            .nutrition(0)
            .saturationMod(0.0f)
            .alwaysEat()
            .build();
}