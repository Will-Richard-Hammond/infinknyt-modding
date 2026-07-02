package com.rogueknyt.infinknyt.entity.client;

import com.rogueknyt.infinknyt.entity.custom.FrostwoodThrowingKnifeEntity;
import com.rogueknyt.infinknyt.item.ModItems;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

public class FrostwoodThrowingKnifeRenderer extends EntityRenderer<FrostwoodThrowingKnifeEntity> {
    private final ItemRenderer itemRenderer;

    public FrostwoodThrowingKnifeRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(
            FrostwoodThrowingKnifeEntity entity,
            float yaw,
            float tickDelta,
            MatrixStack matrices,
            VertexConsumerProvider vertexConsumers,
            int light
    ) {
        matrices.push();

        ItemStack stack = entity.getStack();

        if (stack.isEmpty()) {
            stack = new ItemStack(ModItems.FROSTWOOD_THROWING_KNIFE);
        }

        /*
         * Direction of travel.
         * The projectile already updates its yaw and pitch while flying,
         * so we use those values to face the same direction as the throw.
         */
        float entityYaw = MathHelper.lerp(tickDelta, entity.prevYaw, entity.getYaw());
        float entityPitch = MathHelper.lerp(tickDelta, entity.prevPitch, entity.getPitch());

        /*
         * Face the direction of travel.
         */
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(entityYaw - 90.0F));
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(entityPitch));

        /*
         * Spin around the forward axis.
         * Increase 35.0F for faster spin, decrease it for slower spin.
         */
        float spin = (entity.age + tickDelta) * 35.0F;
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(spin));

        /*
         * Turns the 2D item so it appears more like it is spinning edge-first.
         * You may need to tweak this depending on your texture direction.
         */
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(90.0F));

        /*
         * Size of the thrown knife.
         * Lower this if it looks too big.
         */
        matrices.scale(0.9F, 0.9F, 0.9F);

        itemRenderer.renderItem(
                stack,
                ModelTransformationMode.GROUND,
                light,
                OverlayTexture.DEFAULT_UV,
                matrices,
                vertexConsumers,
                entity.getWorld(),
                entity.getId()
        );

        matrices.pop();

        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
    }

    @Override
    public Identifier getTexture(FrostwoodThrowingKnifeEntity entity) {
        return SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE;
    }
}