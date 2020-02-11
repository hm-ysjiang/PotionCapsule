package hmysjiang.potioncapsule.network;

import hmysjiang.potioncapsule.network.packets.CPacketClearBrewerOutput;
import hmysjiang.potioncapsule.network.packets.CPacketKeyBinding;
import hmysjiang.potioncapsule.network.packets.CPacketUpdateBrewerMemory;
import hmysjiang.potioncapsule.network.packets.CPacketUpdateBrewerPartition;
import hmysjiang.potioncapsule.network.packets.CPacketUpdatePendantStatus;
import hmysjiang.potioncapsule.network.packets.SPacketCustomSyncTile;
import hmysjiang.potioncapsule.network.packets.SPacketPlayerSound;
import hmysjiang.potioncapsule.network.packets.SPacketResponseBrewerMemory;
import hmysjiang.potioncapsule.network.packets.SPacketVisualExplosion;
import hmysjiang.potioncapsule.utils.Defaults;
import hmysjiang.potioncapsule.utils.ITileCustomSync;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.tileentity.TileEntity;
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
		INSTANCE.registerMessage(disc++, CPacketUpdatePendantStatus.class, CPacketUpdatePendantStatus::encode, CPacketUpdatePendantStatus::decode, CPacketUpdatePendantStatus::handle);
		INSTANCE.registerMessage(disc++, CPacketUpdateBrewerMemory.class, CPacketUpdateBrewerMemory::encode, CPacketUpdateBrewerMemory::decode, CPacketUpdateBrewerMemory::handle);
		INSTANCE.registerMessage(disc++, CPacketClearBrewerOutput.class, CPacketClearBrewerOutput::encode, CPacketClearBrewerOutput::decode, CPacketClearBrewerOutput::handle);
		INSTANCE.registerMessage(disc++, CPacketUpdateBrewerPartition.class, CPacketUpdateBrewerPartition::encode, CPacketUpdateBrewerPartition::decode, CPacketUpdateBrewerPartition::handle);
		
		INSTANCE.registerMessage(disc++, SPacketVisualExplosion.class, SPacketVisualExplosion::encode, SPacketVisualExplosion::decode, SPacketVisualExplosion::handle);
		INSTANCE.registerMessage(disc++, SPacketPlayerSound.class, SPacketPlayerSound::encode, SPacketPlayerSound::decode, SPacketPlayerSound::handle);
		INSTANCE.registerMessage(disc++, SPacketResponseBrewerMemory.class, SPacketResponseBrewerMemory::encode, SPacketResponseBrewerMemory::decode, SPacketResponseBrewerMemory::handle);
		INSTANCE.registerMessage(disc++, SPacketCustomSyncTile.class, SPacketCustomSyncTile::encode, SPacketCustomSyncTile::decode, SPacketCustomSyncTile::handle);
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
	
	public static <T extends TileEntity & ITileCustomSync> void sendTile(T tile) {
		SPacketCustomSyncTile<T> msg = SPacketCustomSyncTile.<T>of(tile);
		for (PlayerEntity player: tile.getWorld().getPlayers())
			if (player instanceof ServerPlayerEntity)
				toPlayer(msg, (ServerPlayerEntity) player);
	}
	
}
