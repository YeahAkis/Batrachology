package org.notionsmp.batrachology.entity;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import org.notionsmp.batrachology.Batrachology;
import org.notionsmp.batrachology.effect.ModEffects;

public enum FrogType {
    AZURE("azure", 0.15f, () -> new MobEffectInstance(ModEffects.CRYOSTASIS.get(), 600)),
    BUMBLEBEE("bumblebee", 0.15f, () -> new MobEffectInstance(ModEffects.BATRACHOTOXIN.get(), 800)),
    GREEN_BLACK("green_black", 0.15f, () -> new MobEffectInstance(ModEffects.MELANOTOXIN.get(), 600)),
    GOLDEN("golden", 0.08f, () -> new MobEffectInstance(ModEffects.AUREOTOXIN.get(), 400)),
    STRAWBERRY("strawberry", 0.08f, () -> new MobEffectInstance(ModEffects.HEMOGLUCIDE.get(), 800)),
    FIERY("fiery", 0.15f, () -> new MobEffectInstance(ModEffects.PYROCLASM.get(), 600)),
    NOMINAL("nominal", 0.15f, () -> new MobEffectInstance(ModEffects.HALOTOXIN.get(), 1000)),
    AKIS("akis", 0.0f, () -> null);

    private final String id;
    private final float spawnWeight;
    private final EffectSupplier effectSupplier;

    FrogType(String id, float spawnWeight, EffectSupplier effectSupplier) {
        this.id = id;
        this.spawnWeight = spawnWeight;
        this.effectSupplier = effectSupplier;
    }

    public String getId() {
        return id;
    }

    public float getSpawnWeight() {
        return spawnWeight;
    }

    public ResourceLocation getTexture() {
        return ResourceLocation.fromNamespaceAndPath(Batrachology.MODID,
                "textures/entity/" + id + "_poison_dart_frog.png");
    }

    public MobEffectInstance getEffectInstance() {
        return effectSupplier.get();
    }

    public MobEffectInstance getEffectInstance(float durationMultiplier) {
        MobEffectInstance base = getEffectInstance();
        if (base == null) return null;
        return new MobEffectInstance(
                base.getEffect(),
                (int) (base.getDuration() * durationMultiplier),
                base.getAmplifier(),
                base.isAmbient(),
                base.isVisible(),
                base.showIcon()
        );
    }

    public boolean canSpawnNaturally() {
        return this != AKIS;
    }

    public static FrogType getRandomNaturalType(RandomSource random) {
        float total = 0;
        for (FrogType type : values()) {
            if (type.canSpawnNaturally()) {
                total += type.spawnWeight;
            }
        }

        float rand = random.nextFloat() * total;
        float current = 0;

        for (FrogType type : values()) {
            if (type.canSpawnNaturally()) {
                current += type.spawnWeight;
                if (rand < current) {
                    return type;
                }
            }
        }
        return AZURE;
    }

    @FunctionalInterface
    interface EffectSupplier {
        MobEffectInstance get();
    }
}