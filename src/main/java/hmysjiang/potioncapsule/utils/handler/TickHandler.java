package hmysjiang.potioncapsule.utils.handler;

import hmysjiang.potioncapsule.items.ItemSpecialCapsule;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.WorldTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(bus=EventBusSubscriber.Bus.FORGE)
public class TickHandler {
	
	@SubscribeEvent
	public static void onServerWorldTick(WorldTickEvent event) {
		if (!event.world.isRemote) {
			if (event.phase == Phase.START) {
				ServerWorld world = (ServerWorld) event.world;
				world.getServer().getPlayerList().getPlayers().forEach(ItemSpecialCapsule::serverWorldTick);
			}
		}
	}
	
}
