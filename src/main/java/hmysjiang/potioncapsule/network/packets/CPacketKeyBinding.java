package hmysjiang.potioncapsule.network.packets;

import java.util.function.Supplier;

import hmysjiang.potioncapsule.items.ItemCapsulePendant;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public class CPacketKeyBinding {
	
	public final int keyIndex;
	
	public CPacketKeyBinding(int keyIdx) {
		keyIndex = keyIdx;
		
	}

	public static void encode(CPacketKeyBinding message, PacketBuffer buf) {
		buf.writeInt(message.keyIndex);
	}

	public static CPacketKeyBinding decode(PacketBuffer buf) {
		return new CPacketKeyBinding(buf.readInt());
	}

	public static void handle(CPacketKeyBinding message, Supplier<Context> ctx) {
		int code = message.keyIndex;
		if (code == 0) {
			ItemCapsulePendant.onKeyBindPressed(ctx.get().getSender());
		}
		ctx.get().setPacketHandled(true);
	}
	
}
