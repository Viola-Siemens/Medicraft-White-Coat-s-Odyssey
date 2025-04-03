package com.hexagram2021.medicraft.client.renderer.block;

import com.hexagram2021.medicraft.common.block.entity.AEDBlockEntity;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.world.phys.Vec3;

public class AEDBlockEntityRenderer implements BlockEntityRenderer<AEDBlockEntity> {
	@Override
	public void render(AEDBlockEntity blockEntity, float partialTick, PoseStack transform, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
		Minecraft mc = Minecraft.getInstance();
		if(mc.player != null && mc.player.distanceToSqr(Vec3.atCenterOf(blockEntity.getBlockPos())) < 64) {

		}
	}
}
