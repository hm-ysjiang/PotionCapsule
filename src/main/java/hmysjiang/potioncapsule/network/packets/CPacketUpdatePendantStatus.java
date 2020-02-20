package hmysjiang.potioncapsule.network.packets;

import java.util.function.Supplier;

import hmysjiang.potioncapsule.container.ContainerPendant;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public class CPacketUpdatePendantStatus {
	
	public final int mask;
	
	public CPacketUpdatePendantStatus(int mask) {
		this.mask = mask;
	}
	
	public static void encode(CPacketUpdatePendantStatus message, PacketBuffer buf) {
		buf.writeInt(message.mask);
	}

	public static CPacketUpdatePendantStatus decode(PacketBuffer buf) {
		return new CPacketUpdatePendantStatus(buf.readInt());
	}

	public static void handle(CPacketUpdatePendantStatus message, Supplier<Context> ctx) {
		if (ctx.get().getSender() != null && ctx.get().getSender().openContainer instanceof ContainerPendant) {
			CompoundNBT tag = ((ContainerPendant) ctx.get().getSender().openContainer).getStack().getOrCreateTag();
			tag.putInt("StatusMask", tag.getInt("StatusMask") ^ message.mask);
		}
		ctx.get().setPacketHandled(true);
	}
	
}
