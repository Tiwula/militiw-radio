package ua.tiwula.militiwradio.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;

public class RadioCurioRenderer implements ICurioRenderer
{
    @Override
    public <T extends LivingEntity, M extends EntityModel<T>> void render(
            ItemStack stack,
            SlotContext slotContext,
            PoseStack poseStack,
            RenderLayerParent<T, M> renderLayerParent,
            MultiBufferSource buffer,
            int packedLight,
            float limbSwing,
            float limbSwingAmount,
            float partialTicks,
            float ageInTicks,
            float netHeadYaw,
            float headPitch
    ) {
        @SuppressWarnings("unchecked")
        T entity = (T) slotContext.entity();

        if (!(renderLayerParent.getModel() instanceof HumanoidModel<?> humanoidModel)) {
            return;
        }

        poseStack.pushPose();

        // Следуем за телом игрока
        humanoidModel.body.translateAndRotate(poseStack);

        // Положение рации на груди
        poseStack.translate(-0.23D, 0.15D, -0.145D);

        // Повороты
        poseStack.mulPose(Axis.XP.rotationDegrees(180F));
//        poseStack.mulPose(Axis.ZP.rotationDegrees(180F));

        // Масштаб
        poseStack.scale(0.7F, 0.7F, 0.7F);

        Minecraft.getInstance()
                .getItemRenderer()
                .renderStatic(
                        stack,
                        ItemDisplayContext.FIXED,
                        packedLight,
                        OverlayTexture.NO_OVERLAY,
                        poseStack,
                        buffer,
                        entity.level(),
                        entity.getId()
                );

        poseStack.popPose();
    }
}
