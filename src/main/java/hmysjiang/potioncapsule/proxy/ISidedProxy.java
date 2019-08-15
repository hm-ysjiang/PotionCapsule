package hmysjiang.potioncapsule.proxy;

import hmysjiang.potioncapsule.network.PacketHandler;

public interface ISidedProxy {
	
	default void init() {
		PacketHandler.register();
	}
	
}
