package hmysjiang.potioncapsule.network;

import hmysjiang.potioncapsule.network.packets.PacketKeyBinding;
import hmysjiang.potioncapsule.utils.Defaults;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class PacketHandler {
	
	private static final String VERSION = "1";
	private static final SimpleChannel INSTACNE = NetworkRegistry.ChannelBuilder
			.named(Defaults.modPrefix.apply("packet_channel"))
			.networkProtocolVersion(() -> VERSION)
			.clientAcceptedVersions(VERSION::equals)
			.serverAcceptedVersions(VERSION::equals)
			.simpleChannel();
	
	public static void register() {
		int disc = 0;
		
		INSTACNE.registerMessage(disc++, PacketKeyBinding.class, PacketKeyBinding::encode, PacketKeyBinding::decode, PacketKeyBinding::handle);
	}
	
	public static SimpleChannel getInstacne() {
		return INSTACNE;
	}
	
	public static <MSG> void toServer(MSG msg) {
		INSTACNE.sendToServer(msg);
	}
	
}
