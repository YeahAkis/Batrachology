package org.notionsmp.batrachology.mixin.cryostasis;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.LookControl;
import org.notionsmp.batrachology.effect.ModEffects;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LookControl.class)
public class LookControlMixin {

    @Shadow
    @Final
    protected Mob mob;

    @Inject(method = "setLookAt(DDDFF)V", at = @At("HEAD"), cancellable = true)
    public void lookAt(double x, double y, double z, float maxYawChange, float maxPitchChange, CallbackInfo ci) {
        if (this.mob.hasEffect(ModEffects.CRYOSTASIS.get())) {
            ci.cancel();
        }
    }
}