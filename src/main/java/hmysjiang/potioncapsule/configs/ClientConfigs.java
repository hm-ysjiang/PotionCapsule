package hmysjiang.potioncapsule.configs;

import net.minecraftforge.common.ForgeConfigSpec;

public class ClientConfigs {
	
	public static ForgeConfigSpec.IntValue capsule_effectRenderCount;
	
	public static void init(ForgeConfigSpec.Builder clientBuilder) {
		capsule_effectRenderCount = clientBuilder.comment(" This number decides that how many effect names are render behind the capsule name")
												 .defineInRange("capsule.effectRenderCount", 3, 1, 5);
	}
	
}
