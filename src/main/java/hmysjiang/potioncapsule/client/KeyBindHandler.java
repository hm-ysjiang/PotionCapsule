package hmysjiang.potioncapsule.client;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import hmysjiang.potioncapsule.Reference;
import hmysjiang.potioncapsule.network.PacketHandler;
import hmysjiang.potioncapsule.network.packets.CPacketKeyBinding;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent.KeyInputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = Reference.MOD_ID, value = Dist.CLIENT)
public class KeyBindHandler {
	public static List<KeyBinding> keybindings;
	
	public static void init() {
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
			if (event.getKey() == keybindings.get(i).getKey().getKeyCode()) {
				PacketHandler.toServer(new CPacketKeyBinding(i));
			}
	}
	
}
