package hmysjiang.potioncapsule.network.packets;

import java.util.function.Supplier;

import hmysjiang.potioncapsule.blocks.auto_brewer.TileEntityAutoBrewer;
import hmysjiang.potioncapsule.container.ContainerAutoBrewer;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public class CPacketClearBrewerOutput {
	
	public CPacketClearBrewerOutput() {
	}

	public static void encode(CPacketClearBrewerOutput message, PacketBuffer buf) {
	}

	public static CPacketClearBrewerOutput decode(PacketBuffer buf) {
		return new CPacketClearBrewerOutput();
	}

	public static void handle(CPacketClearBrewerOutput message, Supplier<Context> ctx) {
		if (ctx.get().getSender() != null && ctx.get().getSender().openContainer instanceof ContainerAutoBrewer) {
			TileEntityAutoBrewer brewer = ((ContainerAutoBrewer) ctx.get().getSender().openContainer).brewer;
			brewer.clearPotion();
		}
		ctx.get().setPacketHandled(true);
	}
	
}
