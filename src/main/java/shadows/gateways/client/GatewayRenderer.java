package shadows.gateways.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import shadows.gateways.GatewaysToEternity;
import shadows.gateways.entity.GatewayEntity;

public class GatewayRenderer extends EntityRenderer<GatewayEntity> {

	public static final ResourceLocation TEXTURE = new ResourceLocation(GatewaysToEternity.MODID, "textures/entity/temp.png");

	public GatewayRenderer(EntityRendererManager mgr) {
		super(mgr);
	}

	@Override
	public ResourceLocation getEntityTexture(GatewayEntity entity) {
		return TEXTURE;
	}

	@Override
	public void render(GatewayEntity entity, float unknown, float partialTicks, MatrixStack matrix, IRenderTypeBuffer buf, int packedLight) {
		matrix.push();
		PlayerEntity player = Minecraft.getInstance().player;
		Vec3d playerV = player.getEyePosition(partialTicks);
		Vec3d portal = entity.getPositionVec();

		float scale = 1.5F;
		double yOffset = 1.5;

		matrix.translate(0, yOffset, 0);
		matrix.multiply(new Quaternion(new Vector3f(0, 1, 0), 90, true));
		matrix.multiply(new Quaternion(new Vector3f(0, 1, 0), 180F - (float) angleOf(portal, playerV), true));
		matrix.scale(scale, scale, scale);

		this.renderManager.textureManager.bindTexture(this.getEntityTexture(entity));
		IVertexBuilder builder = buf.getBuffer(RenderType.getEntityCutout(getEntityTexture(entity)));
		builder.vertex(matrix.peek().getModel(), -1, -1, 0).color(255, 255, 255, 255).texture(1, 1).overlay(OverlayTexture.DEFAULT_UV).light(packedLight).normal(matrix.peek().getNormal(), 0, 1, 0).endVertex();
		builder.vertex(matrix.peek().getModel(), -1, 1, 0).color(255, 255, 255, 255).texture(1, 0).overlay(OverlayTexture.DEFAULT_UV).light(packedLight).normal(matrix.peek().getNormal(), 0, 1, 0).endVertex();
		builder.vertex(matrix.peek().getModel(), 1, 1, 0).color(255, 255, 255, 255).texture(0, 0).overlay(OverlayTexture.DEFAULT_UV).light(packedLight).normal(matrix.peek().getNormal(), 0, 1, 0).endVertex();
		builder.vertex(matrix.peek().getModel(), 1, -1, 0).color(255, 255, 255, 255).texture(0, 1).overlay(OverlayTexture.DEFAULT_UV).light(packedLight).normal(matrix.peek().getNormal(), 0, 1, 0).endVertex();

		matrix.pop();
	}

	public static double angleOf(Vec3d p1, Vec3d p2) {
		final double deltaY = (p2.z - p1.z);
		final double deltaX = (p2.x - p1.x);
		final double result = Math.toDegrees(Math.atan2(deltaY, deltaX));
		return (result < 0) ? (360d + result) : result;
	}

}
