package hmysjiang.potioncapsule.network.packets;

import java.util.function.Supplier;

import hmysjiang.potioncapsule.PotionCapsule;
import hmysjiang.potioncapsule.blocks.auto_brewer.TileEntityAutoBrewer;
import hmysjiang.potioncapsule.container.ContainerAutoBrewer;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent.Context;
import net.minecraftforge.items.ItemStackHandler;

public class SPacketResponseBrewerMemory {
	
	public final boolean mem;
	public final ItemStackHandler memory;
	
	public SPacketResponseBrewerMemory(boolean mem, ItemStackHandler memory) {
		this.mem = mem;
		this.memory = memory;
	}

	public static void encode(SPacketResponseBrewerMemory message, PacketBuffer buf) {
		buf.writeBoolean(message.mem);
		for (int i = 0 ; i<6 ; i++)
			buf.writeItemStack(message.memory.getStackInSlot(i));
	}

	public static SPacketResponseBrewerMemory decode(PacketBuffer buf) {
		boolean mem = buf.readBoolean();
		ItemStackHandler memory = new ItemStackHandler(6);
		for (int i = 0 ; i<6 ; i++)
			memory.setStackInSlot(i, buf.readItemStack());
		return new SPacketResponseBrewerMemory(mem, memory);
	}

	public static void handle(SPacketResponseBrewerMemory message, Supplier<Context> ctx) {
		if (PotionCapsule.proxy.getPlayer() != null && PotionCapsule.proxy.getPlayer().openContainer instanceof ContainerAutoBrewer) {
			TileEntityAutoBrewer brewer = ((ContainerAutoBrewer) PotionCapsule.proxy.getPlayer().openContainer).brewer;
			brewer.setMemory(message.mem, message.memory);
		}
		ctx.get().setPacketHandled(true);
	}
	
}
