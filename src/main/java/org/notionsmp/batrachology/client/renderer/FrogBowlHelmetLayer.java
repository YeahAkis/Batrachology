package org.notionsmp.batrachology.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.notionsmp.batrachology.client.model.PoisonDartFrogModel;
import org.notionsmp.batrachology.entity.FrogType;
import org.notionsmp.batrachology.item.custom.FrogBowlItem;

public class FrogBowlHelmetLayer<T extends LivingEntity, M extends HumanoidModel<T>> extends RenderLayer<T, M> {
    private final PoisonDartFrogModel<T> frogModel;

    public FrogBowlHelmetLayer(RenderLayerParent<T, M> renderer, PoisonDartFrogModel<T> frogModel) {
        super(renderer);
        this.frogModel = frogModel;
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, T entity,
                       float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks,
                       float netHeadYaw, float headPitch) {

        ItemStack helmetStack = entity.getItemBySlot(EquipmentSlot.HEAD);
        if (!helmetStack.isEmpty() && helmetStack.getItem() instanceof FrogBowlItem frogBowl) {
            poseStack.pushPose();

            ModelPart head = this.getParentModel().head;
            head.translateAndRotate(poseStack);

            poseStack.scale(1.1F, 1.1F, 1.1F);
            poseStack.translate(0.0D, -1.95D, 0.0D);

            FrogType frogType = frogBowl.getFrogType();
            ResourceLocation texture = frogType.getTexture();

            VertexConsumer vertexConsumer =
                    buffer.getBuffer(this.frogModel.renderType(texture));


            this.frogModel.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
            this.frogModel.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY,
                    1.0F, 1.0F, 1.0F, 1.0F);

            poseStack.popPose();
        }
    }
}