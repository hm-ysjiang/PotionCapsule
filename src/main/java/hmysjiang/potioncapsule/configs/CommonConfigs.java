package hmysjiang.potioncapsule.configs;

import net.minecraftforge.common.ForgeConfigSpec;

public class CommonConfigs {
	
	public static ForgeConfigSpec.IntValue capsule_capacity;
	public static ForgeConfigSpec.IntValue capsule_stackSize;
	public static ForgeConfigSpec.IntValue capsule_preUsage;

	public static ForgeConfigSpec.BooleanValue special_clearOnCraft;
	public static ForgeConfigSpec.BooleanValue special_bzd_enable;
	public static ForgeConfigSpec.IntValue special_bzd_uses;
	public static ForgeConfigSpec.BooleanValue special_xmas_enable;
	public static ForgeConfigSpec.IntValue special_xmas_uses;
	public static ForgeConfigSpec.BooleanValue special_overdrive_enable;
	public static ForgeConfigSpec.IntValue special_overdrive_uses;
	public static ForgeConfigSpec.BooleanValue special_cakelie_enable;
	public static ForgeConfigSpec.IntValue special_cakelie_uses;
	
	public static ForgeConfigSpec.BooleanValue recipe_allowCapsuleCombine;
	public static ForgeConfigSpec.BooleanValue recipe_removeExcessDuration;
	public static ForgeConfigSpec.BooleanValue recipe_enableWartDust;
	public static ForgeConfigSpec.IntValue recipe_instantCatalystAllowed;
	
	public static ForgeConfigSpec.IntValue worldgen_cactiFragSpawnHeight;
	public static ForgeConfigSpec.IntValue worldgen_cactiFragSpawnRate;
	public static ForgeConfigSpec.IntValue worldgen_fieryLilySpawnRate;
	
	public static ForgeConfigSpec.IntValue keybind_delayTicks;

	public static ForgeConfigSpec.BooleanValue misc_replaceNvWithNvnf;
	public static ForgeConfigSpec.BooleanValue misc_catDropFur;
	
	public static void init(ForgeConfigSpec.Builder builder) {
		// capsule
		capsule_capacity = builder.comment(" This number decides how long is the effect in a Capsule and the amount extracts from the potion while crafting")
										.defineInRange("capsule.capacity", 100, 40, 400);
		capsule_stackSize = builder.comment(" This number represents the stack limit of capsule inside the pendant")
										.defineInRange("capsule.stackSize", 128, 1, 4096);
		capsule_preUsage = builder.comment(" This number tells the pendant how long before a specific effect goes off should it consume a capsule")
				.defineInRange("capsule.preUsage", 2, 0, 99);
		
		// special capsules
		special_clearOnCraft = builder.comment(" Should a special capsule be cleared when crafted?")
				.define("specialcap.clearOnCraft", true);
		special_bzd_enable = builder.comment(" Set this to false to disable the Bite the dust capsule")
				.define("specialcap.bzd.enable", true);
		special_xmas_enable = builder.comment(" Set this to false to disable the Lost Christmas capsule")
				.define("specialcap.xmas.enable", true);
		special_overdrive_enable = builder.comment(" Set this to false to disable the Sunlight Yellow Overdrive capsule")
				.define("specialcap.overdrive.enable", true);
		special_cakelie_enable = builder.comment(" Set this to false to disable the Lie of The Cake capsule")
				.define("specialcap.cakelie.enable", true);
		
		special_bzd_uses = builder.comment(" This specifies the max usages of Bite the dust capsule")
				.defineInRange("specialcap.bzd.uses", 3, 1, 64);
		special_xmas_uses = builder.comment(" This specifies the max usages of Lost Christmas capsule")
				.defineInRange("specialcap.xmas.uses", 3, 1, 64);
		special_overdrive_uses = builder.comment(" This specifies the max usages of Sunlight Yellow Overdrive capsule")
				.defineInRange("specialcap.overdrive.uses", 256, 1, 1024);
		special_cakelie_uses = builder.comment(" This specifies the max usages of Lie of The Cake capsule")
				.defineInRange("specialcap.cakelie.uses", 256, 1, 1024);
		
		// recipe
		recipe_allowCapsuleCombine = builder.comment(" Set this to true will allow Potion Capsules to be combined in crafting grids")
												  .define("recipe.allowCombine", true);
		recipe_removeExcessDuration = builder.comment(" Set this to true will remove a certain effect from a potion item if its duration is less than required in recipe")
												   .define("recipe.removeExcessDuration", false);
		recipe_enableWartDust = builder.comment(" Set this to true will enable the crafting recipe of Nether Wart Dust")
											 .define("recipe.enableWartDust", false);
		recipe_instantCatalystAllowed = builder.comment(" This defines the maximum amount of Instant Catalyst the player is allowed to placed in the crafting grid to dup output. Set to 0 to disable this feature")
													 .defineInRange("recipe.instantCatalystAllowed", 7, 0, 63);
		
		// worldgen
		worldgen_cactiFragSpawnHeight = builder.comment(" This number defines how tall a cactus should be to let a Tiny Cactus spawn on it")
				  .defineInRange("worldgen.cactiFragSpawnHeight", 2, 2, 64);
		worldgen_cactiFragSpawnRate = builder.comment(" This number defines the spawn rate (1/n) of Tiny Cactus. NOTE: a rate of 9 gives about 50% of chance when growing a cactus to the vanilla height limit of 3. Set to 0 to disable the spawn")
				  .defineInRange("worldgen.cactiFragSpawnRate", 8, 0, 64);
		worldgen_fieryLilySpawnRate = builder.comment(" This number defines the spawn rate (1/n) of a group of Fiery Lilypads. Set to 0 to disable the spawn")
				  .defineInRange("worldgen.fieryLilySpawnRate", 96, 0, 512);
		
		// keybind
		keybind_delayTicks = builder.comment(" This number define how long the delay should be between two keybind inputs.")
				.defineInRange("keybind.delayTicks", 10, 0, 200);
		
		// misc
		misc_replaceNvWithNvnf = builder.comment(" Set this to true will automatically replace any NightVision with NightVisionNoFlicker, a wrapper effect just to solve the vanilla flickering NightVision")
											  .define("misc.replaceNvWithNvnf", true);
		misc_catDropFur = builder.comment(" Does cats drop fur?")
											  .define("misc.catDropFur", true);
	}
	
}
