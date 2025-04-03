package com.hexagram2021.medicraft.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
import it.unimi.dsi.fastutil.doubles.DoubleCollection;
import it.unimi.dsi.fastutil.doubles.DoubleList;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.joml.Matrix4f;

@OnlyIn(Dist.CLIENT)
public record ElectrocardiogramRenderer(TextureManager textureManager, Int2ObjectMap<ElectrocardiogramInstance> electrocardiograms) implements AutoCloseable {
	private static final int WIDTH = 256;	// 3.2 seconds
	private static final int HEIGHT = 128;

	public ElectrocardiogramRenderer(TextureManager textureManager) {
		this(textureManager, new Int2ObjectOpenHashMap<>());
	}

	public void update(int id, DoubleCollection valuesToUpdate) {
		this.getOrCreateElectrocardiogramInstance(id).update(valuesToUpdate);
	}

	public void render(PoseStack transform, MultiBufferSource buffer, int id, int packedLight) {
		this.getOrCreateElectrocardiogramInstance(id).draw(transform, buffer, packedLight);
	}

	private ElectrocardiogramInstance getOrCreateElectrocardiogramInstance(int id) {
		return this.electrocardiograms.computeIfAbsent(id, ElectrocardiogramInstance::new);
	}

	public void resetData() {
		for (ElectrocardiogramInstance instance : this.electrocardiograms.values()) {
			instance.close();
		}

		this.electrocardiograms.clear();
	}

	@Override
	public void close() {
		this.resetData();
	}

	@OnlyIn(Dist.CLIENT)
	class ElectrocardiogramInstance implements AutoCloseable {
		private static final int DRAW_LINE_EVERY = 16;
		private static final double GRAM_R = 0.4D;
		private static final double GRAM_G = 0.85D;
		private static final double GRAM_B = 0.3D;
		private static final double LINE_COLOR = 0.25D;
		private static final double BACKGROUND_COLOR = 0.15D;
		private static final double LINE_WIDTH = 2.0D;
		private static final double LINE_SOLID_WIDTH = 1.0D;

		private final DynamicTexture texture;
		private final RenderType renderType;
		private int widthSplit = 0;

		private double prev = 0.0D;
		private final DoubleList values = new DoubleArrayList();	// THREAD UNSAFE, LOCK BEFORE EDITING!!!
		private boolean requiresUpload = false;

		ElectrocardiogramInstance(int id) {
			this.texture = new DynamicTexture(WIDTH, HEIGHT, true);
			this.fillDefault();
			ResourceLocation resourcelocation = ElectrocardiogramRenderer.this.textureManager.register("electrocardiogram/" + id, this.texture);
			this.renderType = RenderType.text(resourcelocation);
		}

		private void update(DoubleCollection valuesToUpdate) {
			synchronized (this.values) {
				this.values.addAll(valuesToUpdate);
			}
		}

		public void forceUpload() {
			this.requiresUpload = true;
		}

		private static double convertToY(int h) {
			return (double)(h - HEIGHT / 2) * 1.25D / HEIGHT;
		}
		private static double mix(double pixel, double rate, double background) {
			return 1.0D - (1.0D - pixel * rate) * (1.0D - background);
		}

		@SuppressWarnings("DataFlowIssue")
		private void fillDefault() {
			for (int j = 0; j < WIDTH; j++) {
				int backgroundColor = (int)((j % DRAW_LINE_EVERY == 0 ? LINE_COLOR : BACKGROUND_COLOR) * 256.0D);
				int color = 0xFF000000 | (backgroundColor << 16) | (backgroundColor << 8) | backgroundColor;
				for (int i = 0; i < HEIGHT; i++) {
					this.texture.getPixels().setPixelRGBA(j, i, color);
				}
			}
		}

		@SuppressWarnings({"DataFlowIssue", "PointlessArithmeticExpression"})
		private static void _updateTexture(double prev, double next, int w, DynamicTexture texture) {
			double backgroundColor = w % DRAW_LINE_EVERY == 0 ? LINE_COLOR : BACKGROUND_COLOR;
			double c = Math.sqrt(Math.pow(next - prev, 2) + 1);
			for (int i = 0; i < HEIGHT; i++) {
				double y = convertToY(i);
				double dist = Math.abs(y - prev) / c;
				int color;
				if(dist < LINE_WIDTH) {
					double rate = dist < LINE_SOLID_WIDTH ? 1.0D : (LINE_WIDTH - dist) / (LINE_WIDTH - LINE_SOLID_WIDTH);
					int r = (int)(mix(GRAM_R, rate, backgroundColor) * 256.0D);
					int g = (int)(mix(GRAM_G, rate, backgroundColor) * 256.0D);
					int b = (int)(mix(GRAM_B, rate, backgroundColor) * 256.0D);
					color = 0xFF000000 | (b << 16) | (g << 8) | r;
				} else {
					int rgb = (int)(backgroundColor * 256.0D);
					color = 0xFF000000 | (rgb << 16) | (rgb << 8) | rgb;
				}
				texture.getPixels().setPixelRGBA(w, i, color);
			}
		}

		void updateTexture() {
			synchronized (this.values) {
				_updateTexture(this.prev, this.values.getDouble(0), this.widthSplit % WIDTH, this.texture);
				for (int j = 1; j < this.values.size(); j++) {
					_updateTexture(this.values.getDouble(j - 1), this.values.getDouble(j), (this.widthSplit + j) % WIDTH, this.texture);
				}
				this.widthSplit += this.values.size();
				this.prev = this.values.getDouble(this.values.size() - 1);
				this.values.clear();
			}

			this.texture.upload();
		}

		void draw(PoseStack transform, MultiBufferSource bufferSource, int packedLight) {
			if (this.requiresUpload) {
				this.updateTexture();
				this.requiresUpload = false;
			}
			Matrix4f lastPose = transform.last().pose();
			VertexConsumer vertexConsumer = bufferSource.getBuffer(this.renderType);
			float splitU = (float)this.widthSplit / WIDTH;
			vertexConsumer.addVertex(lastPose, this.widthSplit, HEIGHT, -0.01F).setColor(0xffffffff).setUv(splitU, 1.0F).setLight(packedLight);
			vertexConsumer.addVertex(lastPose, WIDTH, HEIGHT, -0.01F).setColor(0xffffffff).setUv(1.0F, 1.0F).setLight(packedLight);
			vertexConsumer.addVertex(lastPose, WIDTH, 0, -0.01F).setColor(0xffffffff).setUv(1.0F, 0.0F).setLight(packedLight);
			vertexConsumer.addVertex(lastPose, this.widthSplit, 0, -0.01F).setColor(0xffffffff).setUv(splitU, 0.0F).setLight(packedLight);

			vertexConsumer.addVertex(lastPose, 0, HEIGHT, -0.01F).setColor(0xffffffff).setUv(0.0F, 1.0F).setLight(packedLight);
			vertexConsumer.addVertex(lastPose, this.widthSplit, HEIGHT, -0.01F).setColor(0xffffffff).setUv(splitU, 1.0F).setLight(packedLight);
			vertexConsumer.addVertex(lastPose, this.widthSplit, 0, -0.01F).setColor(0xffffffff).setUv(splitU, 0.0F).setLight(packedLight);
			vertexConsumer.addVertex(lastPose, 0, 0, -0.01F).setColor(0xffffffff).setUv(0.0F, 0.0F).setLight(packedLight);
		}

		@Override
		public void close() {
			this.texture.close();
		}
	}
}
