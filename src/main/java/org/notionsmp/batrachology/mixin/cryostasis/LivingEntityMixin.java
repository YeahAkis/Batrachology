package org.notionsmp.batrachology.mixin.cryostasis;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.notionsmp.batrachology.effect.ModEffects;
import org.notionsmp.batrachology.item.custom.FrogBowlItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    private float cryostasisLockedYaw = 0;

    public LivingEntityMixin(EntityType<?> type, Level level) {
        super(type, level);
    }

    @Inject(method = "getEquipmentSlotForItem", at = @At("HEAD"), cancellable = true)
    private static void batrachology$getFrogBowlSlot(ItemStack stack, CallbackInfoReturnable<EquipmentSlot> cir) {
        if (stack.getItem() instanceof FrogBowlItem) {
            cir.setReturnValue(EquipmentSlot.HEAD);
        }
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void batrachology$cryostasisTick(CallbackInfo ci) {
        LivingEntity self = (LivingEntity) (Object) this;

        if (ModEffects.isCryostasis(self)) {
            this.cryostasisLockedYaw = this.getYRot();
            this.setXRot(90);
            this.setYRot(this.cryostasisLockedYaw);
        }
    }

    @ModifyVariable(method = "hurt", at = @At("HEAD"), argsOnly = true)
    private float batrachology$melanotoxinDamageBoost(float amount) {
        LivingEntity self = (LivingEntity) (Object) this;

        if (self.hasEffect(ModEffects.MELANOTOXIN.get())) {
            var effect = self.getEffect(ModEffects.MELANOTOXIN.get());
            if (effect != null) {
                return amount * (1.5f + (effect.getAmplifier() * 0.5f));
            }
        }
        return amount;
    }
}