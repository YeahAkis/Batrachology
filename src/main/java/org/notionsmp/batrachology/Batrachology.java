package org.notionsmp.batrachology;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.notionsmp.batrachology.effect.ModEffects;
import org.notionsmp.batrachology.entity.PoisonDartFrogEntity;
import org.notionsmp.batrachology.item.ModItems;

@Mod(Batrachology.MODID)
public class Batrachology {
    public static final String MODID = "batrachology";

    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MODID);

    public static final RegistryObject<EntityType<PoisonDartFrogEntity>> POISON_DART_FROG =
            ENTITIES.register("poison_dart_frog",
                    () -> EntityType.Builder.of(PoisonDartFrogEntity::new, MobCategory.CREATURE)
                            .sized(0.6f, 0.4f)
                            .clientTrackingRange(10)
                            .build("poison_dart_frog"));

    public Batrachology(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();

        ModItems.init(modEventBus);
        ModEffects.EFFECTS.register(modEventBus);
        ENTITIES.register(modEventBus);

        modEventBus.addListener(this::registerAttributes);
    }

    private void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(POISON_DART_FROG.get(), PoisonDartFrogEntity.createAttributes().build());
    }
}