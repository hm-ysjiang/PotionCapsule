package hmysjiang.potioncapsule.configs;

import net.minecraftforge.common.ForgeConfigSpec;

public class ClientConfigs {

	public static ForgeConfigSpec.IntValue capsule_effectRenderCount;
	public static ForgeConfigSpec.BooleanValue capsule_glowWithEffect;
	
	public static ForgeConfigSpec.BooleanValue pendant_muteCapsuleUsage;
	
	public static ForgeConfigSpec.BooleanValue misc_renderNVNFHUD;
	
	public static void init(ForgeConfigSpec.Builder clientBuilder) {
		// capsule
		capsule_effectRenderCount = clientBuilder.comment(" This number decides that how many effect names are render behind the capsule name")
												 .defineInRange("capsule.effectRenderCount", 3, 0, 5);
		capsule_glowWithEffect = clientBuilder.comment(" Set this to true will give filled capsule the enchanted glowing render effect")
											  .define("capsule.glowWithEffect", false);
		
		// pendant
		pendant_muteCapsuleUsage = clientBuilder.comment(" Set this to true will mute the game status which pops up while capsule being used from pendant")
												.define("pendant.muteCapsuleUsage", false);
		
		// misc
		misc_renderNVNFHUD = clientBuilder.comment(" Set this to true will render the icon in the ingame HUD. This is false by default")
												 .define("misc.renderNVNFHUD", false);
	}
	
}
