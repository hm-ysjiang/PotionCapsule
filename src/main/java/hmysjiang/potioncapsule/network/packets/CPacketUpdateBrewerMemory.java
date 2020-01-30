package hmysjiang.potioncapsule.network.packets;

import java.util.function.Supplier;

import hmysjiang.potioncapsule.blocks.auto_brewer.TileEntityAutoBrewer;
import hmysjiang.potioncapsule.container.ContainerAutoBrewer;
import hmysjiang.potioncapsule.network.PacketHandler;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public class CPacketUpdateBrewerMemory {
	
	public final boolean set;
	
	public CPacketUpdateBrewerMemory(boolean set) {
		this.set = set;
	}

	public static void encode(CPacketUpdateBrewerMemory message, PacketBuffer buf) {
		buf.writeBoolean(message.set);
	}

	public static CPacketUpdateBrewerMemory decode(PacketBuffer buf) {
		return new CPacketUpdateBrewerMemory(buf.readBoolean());
	}

	public static void handle(CPacketUpdateBrewerMemory message, Supplier<Context> ctx) {
		if (ctx.get().getSender().openContainer instanceof ContainerAutoBrewer) {
			TileEntityAutoBrewer brewer = ((ContainerAutoBrewer) ctx.get().getSender().openContainer).brewer;
			if (message.set) {
				if (brewer.checkAndSetRecipe())
					PacketHandler.toPlayer(new SPacketResponseBrewerMemory(true, brewer.getMemory()), ctx.get().getSender());
			}
			else {
				if (brewer.clearMemory())
					PacketHandler.toPlayer(new SPacketResponseBrewerMemory(false, brewer.getMemory()), ctx.get().getSender());
			}
		}
		ctx.get().setPacketHandled(true);
	}
	
}
