package org.notionsmp.batrachology.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.notionsmp.batrachology.Batrachology;

public class ModEffects {
    public static final DeferredRegister<MobEffect> EFFECTS =
            DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, Batrachology.MODID);

    public static final RegistryObject<MobEffect> CRYOSTASIS = EFFECTS.register("cryostasis",
            () -> new CryostasisEffect(MobEffectCategory.HARMFUL, 0x87CEEB));

    public static final RegistryObject<MobEffect> BATRACHOTOXIN = EFFECTS.register("batrachotoxin",
            () -> new BatrachotoxinEffect(MobEffectCategory.HARMFUL, 0xFFD700));

    public static final RegistryObject<MobEffect> MELANOTOXIN = EFFECTS.register("melanotoxin",
            () -> new MelanotoxinEffect(MobEffectCategory.HARMFUL, 0x228B22));

    public static final RegistryObject<MobEffect> AUREOTOXIN = EFFECTS.register("aureotoxin",
            () -> new AureotoxinEffect(MobEffectCategory.HARMFUL, 0xFFA500));

    public static final RegistryObject<MobEffect> HEMOGLUCIDE = EFFECTS.register("hemoglucide",
            () -> new HemoglucideEffect(MobEffectCategory.HARMFUL, 0xFF69B4));

    public static final RegistryObject<MobEffect> PYROCLASM = EFFECTS.register("pyroclasm",
            () -> new PyroclasmEffect(MobEffectCategory.HARMFUL, 0xFF4500));

    public static final RegistryObject<MobEffect> HALOTOXIN = EFFECTS.register("halotoxin",
            () -> new HalotoxinEffect(MobEffectCategory.HARMFUL, 0xF5F5F5));

    public static boolean isCryostasis(LivingEntity entity) {
        return entity != null &&
                entity.hasEffect(ModEffects.CRYOSTASIS.get()) &&
                !entity.isSpectator() &&
                !(entity instanceof Player player && player.isCreative());
    }
}

