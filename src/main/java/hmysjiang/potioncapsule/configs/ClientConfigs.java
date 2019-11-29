package hmysjiang.potioncapsule.configs;

import net.minecraftforge.common.ForgeConfigSpec;

public class ClientConfigs {

	public static ForgeConfigSpec.IntValue capsule_effectRenderCount;

	public static ForgeConfigSpec.BooleanValue pendant_muteCapsuleUsage;
	public static ForgeConfigSpec.BooleanValue pendant_muteOverdrive;
	
	public static ForgeConfigSpec.BooleanValue misc_renderNVNFHUD;
	
	public static void init(ForgeConfigSpec.Builder clientBuilder) {
		// capsule
		capsule_effectRenderCount = clientBuilder.comment(" This number decides that how many effect names are render behind the capsule name")
												 .defineInRange("capsule.effectRenderCount", 3, 0, 5);
		
		// pendant
		pendant_muteCapsuleUsage = clientBuilder.comment(" Set this to true will mute the game status which pops up while capsule being used from pendant")
				.define("pendant.muteCapsuleUsage", false);
		pendant_muteOverdrive = clientBuilder.comment(" Set this to true will mute the pop up status of Sunlight Yellow Overdrive Capsule")
				.define("pendant.muteOverdriveUsage", true);
		
		
		// misc
		misc_renderNVNFHUD = clientBuilder.comment(" Set this to true will render the icon in the ingame HUD. This is false by default")
												 .define("misc.renderNVNFHUD", false);
	}
	
}
