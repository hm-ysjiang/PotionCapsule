package hmysjiang.potioncapsule.configs;

import net.minecraftforge.common.ForgeConfigSpec;

public class ServerConfigs {
	
	public static ForgeConfigSpec.IntValue pendant_slotSize;
	
	public static void init(ForgeConfigSpec.Builder serverBuilder) {
		pendant_slotSize = serverBuilder.comment(" This number represents the slot count limit inside the Capsule Pendant")
										.defineInRange("pendant.slotsize", 128, 1, 4096);
	}
	
}
