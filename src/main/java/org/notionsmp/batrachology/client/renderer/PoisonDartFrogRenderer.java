package org.notionsmp.batrachology.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.notionsmp.batrachology.client.model.PoisonDartFrogModel;
import org.notionsmp.batrachology.entity.PoisonDartFrogEntity;

public class PoisonDartFrogRenderer extends MobRenderer<PoisonDartFrogEntity, PoisonDartFrogModel<PoisonDartFrogEntity>> {

    public PoisonDartFrogRenderer(EntityRendererProvider.Context context) {
        super(context, new PoisonDartFrogModel<>(context.bakeLayer(PoisonDartFrogModel.LAYER_LOCATION)), 0.3f);
    }

    @Override
    public ResourceLocation getTextureLocation(PoisonDartFrogEntity entity) {
        return entity.getFrogType().getTexture();
    }

    @Override
    protected void scale(PoisonDartFrogEntity entity, PoseStack poseStack, float partialTickTime) {
        float scale = 1.5f;
        poseStack.scale(scale, scale, scale);
    }
}