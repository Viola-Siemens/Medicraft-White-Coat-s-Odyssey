package com.hexagram2021.medicraft.mixin;

import com.hexagram2021.medicraft.client.gui.ElectrocardiogramRenderer;
import com.hexagram2021.medicraft.client.renderer.IElectrocardiogramRendererGetter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.server.packs.resources.ResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin implements IElectrocardiogramRendererGetter {
	@Unique
	private ElectrocardiogramRenderer medicraft$electrocardiogramRenderer;

	@Inject(method = "<init>", at = @At(value = "TAIL"))
	public void medicraft$constructor(Minecraft minecraft, ItemInHandRenderer itemInHandRenderer, ResourceManager resourceManager, RenderBuffers renderBuffers, CallbackInfo ci) {
		this.medicraft$electrocardiogramRenderer = new ElectrocardiogramRenderer(minecraft.getTextureManager());
	}

	@Inject(method = "resetData", at = @At(value = "TAIL"))
	public void medicraft$resetData(CallbackInfo ci) {
		this.medicraft$electrocardiogramRenderer.resetData();
	}

	@Inject(method = "close", at = @At(value = "TAIL"))
	public void medicraft$close(CallbackInfo ci) {
		this.medicraft$electrocardiogramRenderer.close();
	}

	@Override
	public ElectrocardiogramRenderer medicraft$electrocardiogramRenderer() {
		return this.medicraft$electrocardiogramRenderer;
	}
}
