package org.notionsmp.batrachology.event;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.notionsmp.batrachology.Batrachology;
import org.notionsmp.batrachology.effect.ModEffects;

@Mod.EventBusSubscriber(modid = Batrachology.MODID)
public class ModEvents {

    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent event) {
        if (!event.getEntity().level().isClientSide()) {
            LivingEntity entity = event.getEntity();

            if (entity.hasEffect(ModEffects.CRYOSTASIS.get())) {
                MobEffectInstance effect = entity.getEffect(ModEffects.CRYOSTASIS.get());
                if (effect != null && event.getAmount() > 0) {
                    entity.removeEffect(ModEffects.CRYOSTASIS.get());
                }
            }
        }
    }
}