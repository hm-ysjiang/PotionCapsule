package hmysjiang.potioncapsule.network.packets;

import java.util.function.Supplier;

import hmysjiang.potioncapsule.items.ItemCapsulePendant;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public class PacketKeyBinding {
	
	public final int keyIndex;
	
	public PacketKeyBinding(int keyIdx) {
		keyIndex = keyIdx;
		
	}

	public static void encode(PacketKeyBinding message, PacketBuffer buf) {
		buf.writeInt(message.keyIndex);
	}

	public static PacketKeyBinding decode(PacketBuffer buf) {
		return new PacketKeyBinding(buf.readInt());
	}

	public static void handle(PacketKeyBinding message, Supplier<Context> ctx) {
		int code = message.keyIndex;
		if (code == 0) {
			ItemCapsulePendant.onKeyBindPressed(ctx.get().getSender());
		}
	}
	
}
