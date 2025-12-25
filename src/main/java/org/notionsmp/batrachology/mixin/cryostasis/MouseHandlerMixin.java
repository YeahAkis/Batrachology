package org.notionsmp.batrachology.mixin.cryostasis;

import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.player.LocalPlayer;
import org.notionsmp.batrachology.effect.ModEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public class MouseHandlerMixin {

    @Inject(method = "onPress", at = @At("HEAD"), cancellable = true)
    private void onMouseButton(long window, int button, int action, int modifiers, CallbackInfo ci) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (ModEffects.isCryostasis(player) && !Minecraft.getInstance().isPaused()) {
            ci.cancel();
        }
    }

    @Inject(method = "onScroll", at = @At("HEAD"), cancellable = true)
    private void onMouseScroll(long window, double horizontal, double vertical, CallbackInfo ci) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (ModEffects.isCryostasis(player) && !Minecraft.getInstance().isPaused()) {
            ci.cancel();
        }
    }
}