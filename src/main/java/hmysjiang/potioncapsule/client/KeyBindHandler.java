package hmysjiang.potioncapsule.client;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import hmysjiang.potioncapsule.Reference;
import hmysjiang.potioncapsule.configs.CommonConfigs;
import hmysjiang.potioncapsule.network.PacketHandler;
import hmysjiang.potioncapsule.network.packets.CPacketKeyBinding;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent.KeyInputEvent;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = Reference.MOD_ID, value = Dist.CLIENT)
public class KeyBindHandler {
	private static int triggerCD;
	public static List<KeyBinding> keybindings;
	
	public static void init() {
		triggerCD = 0;
		
		keybindings = new ArrayList<>();
		
		keybindings.add(new KeyBinding("potioncapsule.keybind.pendant_keybind.descr", KeyEvent.VK_X, "potioncapsule.keybind.category"));
		register();
	}

	private static void register() {
		for (KeyBinding binding: keybindings)
			ClientRegistry.registerKeyBinding(binding);
	}
	
	@SubscribeEvent
	public static void onKeyInput(KeyInputEvent event) {
		if (Minecraft.getInstance().currentScreen != null)
			return;
		for (int i = 0 ; i<keybindings.size() ; i++)
			if (triggerCD == 0 && event.getKey() == keybindings.get(i).getKey().getKeyCode()) {
				PacketHandler.toServer(new CPacketKeyBinding(i));
				triggerCD = CommonConfigs.keybind_delayTicks.get();
			}
	}
	
	@SubscribeEvent
	public static void onClientTick(ClientTickEvent event) {
		if (triggerCD > 0)
			triggerCD--;
	}
	
}
