package hmysjiang.potioncapsule.network.packets;

import java.util.function.Supplier;

import hmysjiang.potioncapsule.PotionCapsule;
import hmysjiang.potioncapsule.utils.ITileCustomSync;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public class SPacketCustomSyncTile<T extends ITileCustomSync> {
	
	public CompoundNBT nbt;
	
	public static <T extends TileEntity & ITileCustomSync> SPacketCustomSyncTile<T> of(T tile){
		SPacketCustomSyncTile<T> msg = new SPacketCustomSyncTile<>(tile.getCustomUpdate());
		BlockPos pos = tile.getPos();
		msg.nbt.putIntArray("Pos", new int[] {pos.getX(), pos.getY(), pos.getZ()});
		return msg;
	}
	
	protected SPacketCustomSyncTile(CompoundNBT nbt) {
		this.nbt = nbt;
	}
	
	public static void encode(SPacketCustomSyncTile<?> message, PacketBuffer buf) {
		buf.writeCompoundTag(message.nbt);
	}
	
	public static SPacketCustomSyncTile<?> decode(PacketBuffer buf) {
		return new SPacketCustomSyncTile<>(buf.readCompoundTag());
	}
	
	@SuppressWarnings("deprecation")
	public static void handle(SPacketCustomSyncTile<?> message, Supplier<Context> ctx) {
		if (PotionCapsule.proxy.getWorld() != null) {
			World world = PotionCapsule.proxy.getWorld();
			int[] pos = message.nbt.getIntArray("Pos");
			if (pos.length != 3) {
				PotionCapsule.Logger.error("Received an errored packet when try to sync tile");
				return;
			}
			BlockPos blockpos = new BlockPos(pos[0], pos[1], pos[2]);
			if (world.isBlockLoaded(blockpos)) {
				TileEntity tile = world.getTileEntity(blockpos);
				if (tile != null && tile instanceof ITileCustomSync) {
					((ITileCustomSync) tile).readCustomUpdate(message.nbt);
				}
			}
		}
		ctx.get().setPacketHandled(true);
	}
	
}
