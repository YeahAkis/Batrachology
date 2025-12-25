package org.notionsmp.batrachology.mixin;

import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.notionsmp.batrachology.item.custom.FrogBowlItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CustomHeadLayer.class)
public class CustomHeadLayerMixin<T extends LivingEntity> {

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    public void batrachology$cancelFrogBowlHelmetRender(
            com.mojang.blaze3d.vertex.PoseStack poseStack,
            net.minecraft.client.renderer.MultiBufferSource buffer,
            int packedLight,
            T entity,
            float limbSwing,
            float limbSwingAmount,
            float partialTick,
            float ageInTicks,
            float netHeadYaw,
            float headPitch,
            CallbackInfo ci
    ) {
        ItemStack helmetStack = entity.getItemBySlot(EquipmentSlot.HEAD);
        if (!helmetStack.isEmpty() && helmetStack.getItem() instanceof FrogBowlItem) {
            ci.cancel();
        }
    }
}