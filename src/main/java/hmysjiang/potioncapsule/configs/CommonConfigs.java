package hmysjiang.potioncapsule.configs;

import net.minecraftforge.common.ForgeConfigSpec;

public class CommonConfigs {
	
	public static ForgeConfigSpec.IntValue capsule_capacity;
	public static ForgeConfigSpec.IntValue capsule_stackSize;
	
	public static ForgeConfigSpec.BooleanValue recipe_allowCapsuleCombine;
	public static ForgeConfigSpec.BooleanValue recipe_removeExcessDuration;
	public static ForgeConfigSpec.BooleanValue recipe_enableWartDust;
	public static ForgeConfigSpec.IntValue recipe_instantCatalystAllowed;
	
	public static ForgeConfigSpec.BooleanValue misc_replaceNvWithNvnf;
	
	public static void init(ForgeConfigSpec.Builder builder) {
		// capsule
		capsule_capacity = builder.comment(" This number decides how long is the effect in a Capsule and the amount extracts from the potion while crafting")
										.defineInRange("capsule.capacity", 100, 40, 400);
		capsule_stackSize = builder.comment(" This number represents the stack limit of capsule inside the pendant")
										.defineInRange("capsule.stackSize", 128, 1, 4096);
		
		// recipe
		recipe_allowCapsuleCombine = builder.comment(" Set this to true will allow Potion Capsules to be combined in crafting grids")
												  .define("recipe.allowCombine", true);
		recipe_removeExcessDuration = builder.comment(" Set this to true will remove a certain effect from a potion item if its duration is less than required in recipe")
												   .define("recipe.removeExcessDuration", false);
		recipe_enableWartDust = builder.comment(" Set this to true will enable the crafting recipe of Nether Wart Dust")
											 .define("recipe.enableWartDust", false);
		recipe_instantCatalystAllowed = builder.comment(" This defines the maximum amount of Instant Catalyst the player is allowed to placed in the crafting grid to dup output. Set to 0 to disable this feature")
													 .defineInRange("recipe.instantCatalystAllowed", 7, 0, 63);
		
		// misc
		misc_replaceNvWithNvnf = builder.comment(" Set this to true will automatically replace any NightVision with NightVisionNoFlicker, a wrapper effect just to solve the vanilla flickering NightVision")
											  .define("misc.replaceNvWithNvnf", true);
	}
	
}
