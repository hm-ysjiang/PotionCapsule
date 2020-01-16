package hmysjiang.potioncapsule.configs;

import net.minecraftforge.common.ForgeConfigSpec;

public class ClientConfigs {

	public static ForgeConfigSpec.IntValue capsule_effectRenderCount;
	
	public static ForgeConfigSpec.BooleanValue misc_renderNVNFHUD;
	
	public static void init(ForgeConfigSpec.Builder clientBuilder) {
		// capsule
		capsule_effectRenderCount = clientBuilder.comment(" This number decides that how many effect names are render behind the capsule name")
												 .defineInRange("capsule.effectRenderCount", 3, 0, 5);
		
		// misc
		misc_renderNVNFHUD = clientBuilder.comment(" Set this to true will render the icon in the ingame HUD. This is false by default")
												 .define("misc.renderNVNFHUD", false);
	}
	
}
