package com.rogueknyt.infinknyt;

import com.rogueknyt.infinknyt.block.ModBlocks;
import com.rogueknyt.infinknyt.block.entity.ModBoats;
import com.rogueknyt.infinknyt.client.render.TranslucentTerraformBoatEntityRenderer;
import com.terraformersmc.terraform.boat.api.client.TerraformBoatClientHelper;
import com.terraformersmc.terraform.boat.impl.TerraformBoatInitializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.RenderLayer;

public class InfinKnytClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.FROSTWOOD_PLANK, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.FROSTWOOD_LOG, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.STRIPPED_FROSTWOOD_LOG, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.FROSTWOOD_CRAFTING_TABLE, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.STRIPPED_FROSTWOOD_WOOD, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.FROSTWOOD_WOOD, RenderLayer.getTranslucent());

        TerraformBoatClientHelper.registerModelLayers(ModBoats.FROSTWOOD_BOAT_ID, false);

        EntityRendererRegistry.register(TerraformBoatInitializer.BOAT, context ->
                new TranslucentTerraformBoatEntityRenderer(context, false));

        EntityRendererRegistry.register(TerraformBoatInitializer.CHEST_BOAT, context ->
                new TranslucentTerraformBoatEntityRenderer(context, true));
    }
}