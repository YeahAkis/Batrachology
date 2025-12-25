package org.notionsmp.batrachology.client;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.notionsmp.batrachology.Batrachology;
import org.notionsmp.batrachology.client.model.PoisonDartFrogModel;
import org.notionsmp.batrachology.client.renderer.FrogBowlHelmetLayer;
import org.notionsmp.batrachology.client.renderer.PoisonDartFrogRenderer;

@Mod.EventBusSubscriber(modid = Batrachology.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetup {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() ->
                EntityRenderers.register(
                        Batrachology.POISON_DART_FROG.get(),
                        PoisonDartFrogRenderer::new
                )
        );
    }

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(
                PoisonDartFrogModel.LAYER_LOCATION,
                PoisonDartFrogModel::createBodyLayer
        );
    }

    @SubscribeEvent
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void addLayers(EntityRenderersEvent.AddLayers event) {
        PoisonDartFrogModel<LivingEntity> frogModel = new PoisonDartFrogModel<>(
                event.getEntityModels().bakeLayer(PoisonDartFrogModel.LAYER_LOCATION)
        );

        for (String skin : new String[]{"default", "slim"}) {
            PlayerRenderer renderer = event.getSkin(skin);
            if (renderer != null) {
                renderer.addLayer(new FrogBowlHelmetLayer(renderer, frogModel));
            }
        }

        addTo(event, EntityType.ZOMBIE, frogModel);
        addTo(event, EntityType.HUSK, frogModel);
        addTo(event, EntityType.DROWNED, frogModel);
        addTo(event, EntityType.SKELETON, frogModel);
        addTo(event, EntityType.STRAY, frogModel);
        addTo(event, EntityType.WITHER_SKELETON, frogModel);
        addTo(event, EntityType.PIGLIN, frogModel);
        addTo(event, EntityType.PIGLIN_BRUTE, frogModel);
        addTo(event, EntityType.ARMOR_STAND, frogModel);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static void addTo(
            EntityRenderersEvent.AddLayers event,
            EntityType<? extends LivingEntity> type,
            PoisonDartFrogModel<LivingEntity> frogModel
    ) {
        LivingEntityRenderer renderer = (LivingEntityRenderer) event.getRenderer(type);
        if (renderer != null && renderer.getModel() instanceof HumanoidModel) {
            renderer.addLayer(new FrogBowlHelmetLayer(renderer, frogModel));
        }
    }
}