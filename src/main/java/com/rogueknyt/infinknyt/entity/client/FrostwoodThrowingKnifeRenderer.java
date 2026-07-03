package com.rogueknyt.infinknyt.entity.client;

import com.rogueknyt.infinknyt.entity.custom.FrostwoodThrowingKnifeEntity;
import com.rogueknyt.infinknyt.item.ModItems;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

public class FrostwoodThrowingKnifeRenderer extends EntityRenderer<FrostwoodThrowingKnifeEntity> {
    private final ItemRenderer itemRenderer;

    /*
     * Use this if the knife points sideways/backwards.
     * Try 0, 90, -90, or 180.
     */
    private static final float TIP_ALIGNMENT = 180;

    /*
     * Arrow-style roll speed.
     * Higher = faster spin.
     */
    private static final float SPIN_SPEED = 35.0F;

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

        float entityYaw = MathHelper.lerp(tickDelta, entity.prevYaw, entity.getYaw());
        float entityPitch = MathHelper.lerp(tickDelta, entity.prevPitch, entity.getPitch());

        /*
         * Arrow-style direction.
         * This points the knife in the direction the projectile is travelling.
         */
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(entityYaw - 90.0F));
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(entityPitch));

        /*
         * Makes the knife texture point forward.
         * If the tip is wrong, only change TIP_ALIGNMENT.
         */
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(TIP_ALIGNMENT));

        /*
         * Simple arrow-style spin.
         * It rolls around its forward direction instead of tumbling end over end.
         */
        float spin = (entity.age + tickDelta) * SPIN_SPEED;
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(spin));

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
        return PlayerScreenHandler.BLOCK_ATLAS_TEXTURE;
    }
}