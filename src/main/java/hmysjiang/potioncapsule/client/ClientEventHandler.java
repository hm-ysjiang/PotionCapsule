package hmysjiang.potioncapsule.client;

import hmysjiang.potioncapsule.Reference;
import hmysjiang.potioncapsule.configs.ClientConfigs;
import hmysjiang.potioncapsule.utils.text.CapsuleUsedTextComponent;
import hmysjiang.potioncapsule.utils.text.OverdriveTextComponent;
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
		if (ClientConfigs.pendant_muteOverdrive.get()) {
			if (event.getMessage() instanceof OverdriveTextComponent)
				event.setCanceled(true);
		}
	}
	
}
