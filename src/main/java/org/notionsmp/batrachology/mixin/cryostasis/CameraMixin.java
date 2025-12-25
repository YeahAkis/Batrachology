package org.notionsmp.batrachology.mixin.cryostasis;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.entity.player.Player;
import org.notionsmp.batrachology.effect.ModEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(Camera.class)
public abstract class CameraMixin {

    @ModifyArg(
            method = "setup",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Camera;setRotation(FF)V"),
            index = 0
    )
    private float modifyYaw(float originalYaw) {
        Player player = Minecraft.getInstance().player;
        if (player != null && ModEffects.isCryostasis(player)) {
            return player.getYRot();
        }
        return originalYaw;
    }

    @ModifyArg(
            method = "setup",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Camera;setRotation(FF)V"),
            index = 1
    )
    private float modifyPitch(float originalPitch) {
        Player player = Minecraft.getInstance().player;
        if (player != null && ModEffects.isCryostasis(player)) {
            return 90.0F;
        }
        return originalPitch;
    }
}