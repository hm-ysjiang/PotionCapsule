package hmysjiang.potioncapsule.client;

import hmysjiang.potioncapsule.Reference;
import hmysjiang.potioncapsule.configs.ClientConfigs;
import hmysjiang.potioncapsule.utils.text.CapsuleUsedTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = Reference.MOD_ID, value = Dist.CLIENT)
public class ClientEventHandler {
	
	@SubscribeEvent
	public static void onClientChat(ClientChatReceivedEvent event) {
		if (ClientConfigs.pendant_muteCapsuleUsage.get()) {
			if (event.getMessage() instanceof CapsuleUsedTextComponent)
				event.setCanceled(true);
		}
	}
	
}
