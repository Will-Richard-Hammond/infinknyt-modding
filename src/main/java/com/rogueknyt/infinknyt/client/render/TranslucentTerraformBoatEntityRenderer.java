package com.rogueknyt.infinknyt.client.render;

import com.mojang.datafixers.util.Pair;
import com.rogueknyt.infinknyt.InfinKnyt;
import com.terraformersmc.terraform.boat.impl.client.TerraformBoatEntityRenderer;
import com.terraformersmc.terraform.boat.impl.entity.TerraformBoatHolder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.CompositeEntityModel;
import net.minecraft.client.render.entity.model.ModelWithWaterPatch;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.joml.Quaternionf;

@Environment(EnvType.CLIENT)
public class TranslucentTerraformBoatEntityRenderer extends TerraformBoatEntityRenderer {
    public TranslucentTerraformBoatEntityRenderer(EntityRendererFactory.Context context, boolean chest) {
        super(context, chest);
    }

    @Override
    public void render(BoatEntity boatEntity, float yaw, float tickDelta,
                       MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {

        if (!(boatEntity instanceof TerraformBoatHolder holder)) {
            super.render(boatEntity, yaw, tickDelta, matrices, vertexConsumers, light);
            return;
        }

        matrices.push();
        matrices.translate(0.0F, 0.375F, 0.0F);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F - yaw));

        float wobbleTicks = boatEntity.getDamageWobbleTicks() - tickDelta;
        float wobbleStrength = boatEntity.getDamageWobbleStrength() - tickDelta;

        if (wobbleStrength < 0.0F) {
            wobbleStrength = 0.0F;
        }

        if (wobbleTicks > 0.0F) {
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(
                    MathHelper.sin(wobbleTicks) * wobbleTicks * wobbleStrength / 10.0F * boatEntity.getDamageWobbleSide()
            ));
        }

        float bubbleWobble = boatEntity.interpolateBubbleWobble(tickDelta);

        if (!MathHelper.approximatelyEquals(bubbleWobble, 0.0F)) {
            matrices.multiply(new Quaternionf().setAngleAxis(
                    bubbleWobble * ((float) Math.PI / 180.0F),
                    1.0F, 0.0F, 1.0F
            ));
        }

        Pair<Identifier, CompositeEntityModel<BoatEntity>> pair = this.getTextureAndModel(holder);
        Identifier texture = pair.getFirst();
        CompositeEntityModel<BoatEntity> model = pair.getSecond();

        matrices.scale(-1.0F, -1.0F, 1.0F);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(90.0F));

        model.setAngles(boatEntity, tickDelta, 0.0F, -0.1F, 0.0F, 0.0F);

        boolean isFrostwood = texture.getNamespace().equals(InfinKnyt.MOD_ID)
                && texture.getPath().contains("frostwood_boat");

        RenderLayer renderLayer = isFrostwood
                ? RenderLayer.getEntityTranslucent(texture)
                : model.getLayer(texture);

        float alpha = isFrostwood ? 0.55F : 1.0F;

        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(renderLayer);
        model.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, alpha);

        if (!boatEntity.isSubmergedInWater()) {
            VertexConsumer waterMask = vertexConsumers.getBuffer(RenderLayer.getWaterMask());

            if (model instanceof ModelWithWaterPatch modelWithWaterPatch) {
                modelWithWaterPatch.getWaterPatch().render(matrices, waterMask, light, OverlayTexture.DEFAULT_UV);
            }
        }

        matrices.pop();

        if (this.hasLabel(boatEntity)) {
            this.renderLabelIfPresent(boatEntity, boatEntity.getDisplayName(), matrices, vertexConsumers, light);
        }
    }
}