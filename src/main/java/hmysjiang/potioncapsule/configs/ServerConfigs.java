package hmysjiang.potioncapsule.configs;

import net.minecraftforge.common.ForgeConfigSpec;

public class ServerConfigs {
	
	public static ForgeConfigSpec.IntValue capsule_capacity;
	public static ForgeConfigSpec.IntValue capsule_stackSize;
	
	public static ForgeConfigSpec.BooleanValue recipe_allowCapsuleCombine;
	public static ForgeConfigSpec.BooleanValue recipe_removeExcessDuration;
	public static ForgeConfigSpec.BooleanValue recipe_enableWartDust;
	
	public static ForgeConfigSpec.BooleanValue misc_replaceNvWithNvnf;
	
	public static void init(ForgeConfigSpec.Builder serverBuilder) {
		// capsule
		capsule_capacity = serverBuilder.comment(" This number decides how long is the effect in a Capsule and the amount extracts from the potion while crafting")
										.defineInRange("capsule.capacity", 100, 40, 400);
		capsule_stackSize = serverBuilder.comment(" This number represents the stack limit of capsule inside the pendant")
										.defineInRange("capsule.stackSize", 128, 1, 4096);
		
		// recipe
		recipe_allowCapsuleCombine = serverBuilder.comment(" Set this to true will allow Potion Capsules to be combined in crafting grids")
												  .define("recipe.allowCombine", true);
		recipe_removeExcessDuration = serverBuilder.comment(" Set this to true will remove a certain effect from a potion item if its duration is less than required in recipe")
												   .define("recipe.removeExcessDuration", false);
		recipe_enableWartDust = serverBuilder.comment(" Set this to true will enable the crafting recipe of Nether Wart Dust")
											 .define("recipe.enableWartDust", false);
		
		// misc
		misc_replaceNvWithNvnf = serverBuilder.comment(" Set this to true will automatically replace any NightVision with NightVisionNoFlicker, a wrapper effect just to solve the vanilla flickering NightVision")
											  .define("misc.replaceNvWithNvnf", true);
	}
	
}
