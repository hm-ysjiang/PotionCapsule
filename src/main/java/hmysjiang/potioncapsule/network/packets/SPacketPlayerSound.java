package hmysjiang.potioncapsule.network.packets;

import java.util.function.Supplier;

import hmysjiang.potioncapsule.PotionCapsule;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public class SPacketPlayerSound {
	
	public BlockPos pos;
	public String soundEventName, soundCategoryName;
	public float volume, pitch;
	
	public SPacketPlayerSound(BlockPos pos, String soundEventName, String soundCategoryName, float volume, float pitch) {
		this.pos = pos;
		this.soundEventName = soundEventName;
		this.soundCategoryName = soundCategoryName;
		this.volume = volume;
		this.pitch = pitch;
	}
	
	public static void encode(SPacketPlayerSound message, PacketBuffer buf) {
		buf.writeBlockPos(message.pos);
		buf.writeString(message.soundEventName);
		buf.writeString(message.soundCategoryName);
		buf.writeFloat(message.volume);
		buf.writeFloat(message.pitch);
	}

	public static SPacketPlayerSound decode(PacketBuffer buf) {
		return new SPacketPlayerSound(buf.readBlockPos(), buf.readString(), buf.readString(), buf.readFloat(), buf.readFloat());
	}
	
	@SuppressWarnings("deprecation")
	public static void handle(SPacketPlayerSound message, Supplier<Context> ctx) {
		PlayerEntity player = PotionCapsule.proxy.getPlayer();
		if (player != null) {
			Registry.SOUND_EVENT.getValue(new ResourceLocation(message.soundEventName)).ifPresent(sound -> {
				player.world.playSound(player, message.pos, sound, SoundCategory.valueOf(message.soundCategoryName), message.volume, message.pitch);
			});
		}
		ctx.get().setPacketHandled(true);
	}
	
}
