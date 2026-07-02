package com.rogueknyt.infinknyt.entity.custom;

import com.rogueknyt.infinknyt.entity.ModEntities;
import com.rogueknyt.infinknyt.item.ModItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

public class FrostwoodThrowingKnifeEntity extends ThrownItemEntity {
    private static final float DAMAGE = 3.0F;
    private static final int SLOWNESS_DURATION = 80; // 4 seconds
    private static final int SLOWNESS_AMPLIFIER = 0; // Slowness I

    public FrostwoodThrowingKnifeEntity(EntityType<? extends FrostwoodThrowingKnifeEntity> entityType, World world) {
        super(entityType, world);
    }

    public FrostwoodThrowingKnifeEntity(World world, LivingEntity owner) {
        super(ModEntities.FROSTWOOD_THROWING_KNIFE, owner, world);
    }

    public FrostwoodThrowingKnifeEntity(World world, double x, double y, double z) {
        super(ModEntities.FROSTWOOD_THROWING_KNIFE, x, y, z, world);
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.FROSTWOOD_THROWING_KNIFE;
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);

        if (this.getWorld().isClient()) {
            return;
        }

        Entity hitEntity = entityHitResult.getEntity();
        Entity owner = this.getOwner();

        if (hitEntity == owner) {
            return;
        }

        if (hitEntity instanceof LivingEntity livingEntity) {
            livingEntity.damage(this.getDamageSources().thrown(this, owner), DAMAGE);

            livingEntity.addStatusEffect(new StatusEffectInstance(
                    StatusEffects.SLOWNESS,
                    SLOWNESS_DURATION,
                    SLOWNESS_AMPLIFIER
            ));
        }
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);

        if (!this.getWorld().isClient()) {
            this.discard();
        }
    }
}