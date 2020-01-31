package hmysjiang.potioncapsule.network.packets;

import java.util.function.Supplier;

import hmysjiang.potioncapsule.blocks.auto_brewer.TileEntityAutoBrewer;
import hmysjiang.potioncapsule.container.ContainerAutoBrewer;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public class CPacketUpdateBrewerPartition {
	
	public final int amount;
	
	public CPacketUpdateBrewerPartition(int amount) {
		this.amount = amount;
	}

	public static void encode(CPacketUpdateBrewerPartition message, PacketBuffer buf) {
		buf.writeVarInt(message.amount);
	}

	public static CPacketUpdateBrewerPartition decode(PacketBuffer buf) {
		return new CPacketUpdateBrewerPartition(buf.readVarInt());
	}

	public static void handle(CPacketUpdateBrewerPartition message, Supplier<Context> ctx) {
		if (ctx.get().getSender().openContainer instanceof ContainerAutoBrewer) {
			TileEntityAutoBrewer brewer = ((ContainerAutoBrewer) ctx.get().getSender().openContainer).brewer;
			brewer.updatePartition(message.amount);
		}
		ctx.get().setPacketHandled(true);
	}
	
}
