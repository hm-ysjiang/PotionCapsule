package hmysjiang.potioncapsule.network;

import hmysjiang.potioncapsule.network.packets.CPacketKeyBinding;
import hmysjiang.potioncapsule.network.packets.SPacketVisualExplosion;
import hmysjiang.potioncapsule.utils.Defaults;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class PacketHandler {
	
	private static final String VERSION = "1";
	private static final SimpleChannel INSTANCE = NetworkRegistry.ChannelBuilder
			.named(Defaults.modPrefix.apply("packet_channel"))
			.networkProtocolVersion(() -> VERSION)
			.clientAcceptedVersions(VERSION::equals)
			.serverAcceptedVersions(VERSION::equals)
			.simpleChannel();
	
	public static void register() {
		int disc = 0;
		
		INSTANCE.registerMessage(disc++, CPacketKeyBinding.class, CPacketKeyBinding::encode, CPacketKeyBinding::decode, CPacketKeyBinding::handle);
		INSTANCE.registerMessage(disc++, SPacketVisualExplosion.class, SPacketVisualExplosion::encode, SPacketVisualExplosion::decode, SPacketVisualExplosion::handle);
	}
	
	public static SimpleChannel getInstacne() {
		return INSTANCE;
	}
	
	public static <MSG> void toServer(MSG msg) {
		INSTANCE.sendToServer(msg);
	}
	
	public static <MSG> void toPlayer(MSG msg, ServerPlayerEntity player) {
		INSTANCE.sendTo(msg, player.connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
	}
	
}
