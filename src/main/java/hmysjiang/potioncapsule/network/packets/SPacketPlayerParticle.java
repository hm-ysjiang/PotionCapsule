package hmysjiang.potioncapsule.network.packets;

import java.util.function.Supplier;

import hmysjiang.potioncapsule.PotionCapsule;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public class SPacketPlayerParticle {
	
	public final BasicParticleType particle;
	public final double posX;
	public final double posY;
	public final double posZ;
	public final double velX;
	public final double velY;
	public final double velZ;
	
	public SPacketPlayerParticle(BasicParticleType particleIn, double posXIn, double posYIn, double posZIn, double velXIn, double velYIn, double velZIn) {
		particle = particleIn;
		posX = posXIn;
		posY = posYIn;
		posZ = posZIn;
		velX = velXIn;
		velY = velYIn;
		velZ = velZIn;
	}
	
	@SuppressWarnings("deprecation")
	private SPacketPlayerParticle(String particleRegistryName, double posXIn, double posYIn, double posZIn, double velXIn, double velYIn, double velZIn) {
		this((BasicParticleType) Registry.PARTICLE_TYPE.getValue(new ResourceLocation(particleRegistryName)).orElse(null), posXIn, posYIn, posZIn, velXIn, velYIn, velZIn);
	}

	public static void encode(SPacketPlayerParticle message, PacketBuffer buf) {
		buf.writeString(message.particle.getRegistryName().toString());
		buf.writeDouble(message.posX);
		buf.writeDouble(message.posY);
		buf.writeDouble(message.posZ);
		buf.writeDouble(message.velX);
		buf.writeDouble(message.velY);
		buf.writeDouble(message.velZ);
	}

	public static SPacketPlayerParticle decode(PacketBuffer buf) {
		return new SPacketPlayerParticle(buf.readString(), buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readDouble());
	}

	public static void handle(SPacketPlayerParticle message, Supplier<Context> ctx) {
		PlayerEntity player = PotionCapsule.proxy.getPlayer();
		if (player != null && message.particle != null) {
			player.world.addParticle(message.particle, message.posX, message.posY, message.posZ, message.velX, message.velY, message.velZ);
		}
		ctx.get().setPacketHandled(true);
	}
	
}
