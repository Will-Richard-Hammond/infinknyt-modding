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
import net.minecraft.util.math.Vec3d;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class FrostwoodThrowingKnifeRenderer extends EntityRenderer<FrostwoodThrowingKnifeEntity> {
    private static final float SPIN_SPEED = 55.0F;

    /*
     * Your item texture is drawn diagonally in the item PNG.
     * This rotates the sprite so the blade's length lines up with the renderer's local Y axis.
     * The local Y axis is then pointed directly along the knife's velocity.
     */
    private static final float TEXTURE_DIRECTION_CORRECTION = 90.0F;

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

        Vec3d velocity = entity.getVelocity();

        if (velocity.lengthSquared() > 0.0001D) {
            Vector3f direction = new Vector3f(
                    (float) velocity.x,
                    (float) velocity.y,
                    (float) velocity.z
            ).normalize();

            /*
             * Point the knife using its real travel direction instead of doing yaw and pitch
             * as separate rotations. The old renderer used pitch as a Z rotation, which also
             * affected the roll/spin axis. That is why the knife became more face-on the more
             * you looked up or down.
             */
            matrices.multiply(new Quaternionf().rotationTo(
                    new Vector3f(0.0F, -1.0F, 0.0F),
                    direction
            ));
        } else {
            float entityYaw = MathHelper.lerp(tickDelta, entity.prevYaw, entity.getYaw());
            float entityPitch = MathHelper.lerp(tickDelta, entity.prevPitch, entity.getPitch());

            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-entityYaw));
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(entityPitch));
        }

        /*
         * Spin around the knife's own forward axis after it has been aimed.
         * Because the local Y axis is aligned to velocity above, this stays correct at every pitch.
         */
        float spin = (entity.age + tickDelta) * SPIN_SPEED;
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(spin));

        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(TEXTURE_DIRECTION_CORRECTION));
        matrices.scale(0.9F, 0.9F, 0.9F);

        itemRenderer.renderItem(
                stack,
                ModelTransformationMode.NONE,
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
