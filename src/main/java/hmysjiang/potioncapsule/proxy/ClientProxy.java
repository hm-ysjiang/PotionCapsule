package hmysjiang.potioncapsule.proxy;

import hmysjiang.potioncapsule.client.KeyBindHandler;
import hmysjiang.potioncapsule.client.gui.ScreenGelatinExtractor;
import hmysjiang.potioncapsule.client.gui.ScreenGelatinFormer;
import hmysjiang.potioncapsule.client.gui.ScreenPendant;
import hmysjiang.potioncapsule.container.ContainerGelatinExtractor;
import hmysjiang.potioncapsule.container.ContainerGelatinFormer;
import hmysjiang.potioncapsule.container.ContainerPendant;
import net.minecraft.client.gui.ScreenManager;

public class ClientProxy implements ISidedProxy {
	
	@Override
	public void init() {
		ISidedProxy.super.init();
		KeyBindHandler.init();
		ScreenManager.registerFactory(ContainerPendant.TYPE, ScreenPendant::new);
		ScreenManager.registerFactory(ContainerGelatinExtractor.TYPE, ScreenGelatinExtractor::new);
		ScreenManager.registerFactory(ContainerGelatinFormer.TYPE, ScreenGelatinFormer::new);
	}
	
}
