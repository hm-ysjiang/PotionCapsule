package hmysjiang.potioncapsule.utils.helper;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.util.IItemProvider;
import net.minecraftforge.common.brewing.BrewingRecipe;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;

public class PotionHelper {
	
	public static ItemStack glowstone() {
		return new ItemStack(Items.GLOWSTONE_DUST);
	}
	
	public static ItemStack redstone() {
		return new ItemStack(Items.REDSTONE);
	}
	
	public static void registerRecipe(Potion origin, IItemProvider initMaterial, Potion output) {
		BrewingRecipeRegistry.addRecipe(new StrictBrewingRecipe(new ItemStack(Items.POTION), origin, new ItemStack(initMaterial), output));
		BrewingRecipeRegistry.addRecipe(new StrictBrewingRecipe(new ItemStack(Items.SPLASH_POTION), origin, new ItemStack(initMaterial), output));
		BrewingRecipeRegistry.addRecipe(new StrictBrewingRecipe(new ItemStack(Items.LINGERING_POTION), origin, new ItemStack(initMaterial), output));
	}
	
	public static <T extends Potion> void register2StageRecipe(IItemProvider initMaterial, T NORMAL, T LONG) {
		register2StageRecipe(Potions.AWKWARD, initMaterial, NORMAL, LONG);
	}
	
	public static <T extends Potion> void register2StageRecipe(Potion origin, IItemProvider initMaterial, T NORMAL, T LONG) {
		registerSplashLinger(NORMAL);
		registerSplashLinger(LONG);
		
		BrewingRecipeRegistry.addRecipe(new StrictBrewingRecipe(new ItemStack(Items.POTION), origin, new ItemStack(initMaterial), NORMAL));
		BrewingRecipeRegistry.addRecipe(new StrictBrewingRecipe(new ItemStack(Items.POTION), NORMAL, redstone(), LONG));
		
		BrewingRecipeRegistry.addRecipe(new StrictBrewingRecipe(new ItemStack(Items.SPLASH_POTION), origin, new ItemStack(initMaterial), NORMAL));
		BrewingRecipeRegistry.addRecipe(new StrictBrewingRecipe(new ItemStack(Items.SPLASH_POTION), NORMAL, redstone(), LONG));
		
		BrewingRecipeRegistry.addRecipe(new StrictBrewingRecipe(new ItemStack(Items.LINGERING_POTION), origin, new ItemStack(initMaterial), NORMAL));
		BrewingRecipeRegistry.addRecipe(new StrictBrewingRecipe(new ItemStack(Items.LINGERING_POTION), NORMAL, redstone(), LONG));
	}
	
	public static <T extends Potion> void register3StageRecipe(IItemProvider initMaterial, T NORMAL, T LONG, T VERY_LONG) {
		register3StageRecipe(Potions.AWKWARD, initMaterial, NORMAL, LONG, VERY_LONG);
	}
	
	public static <T extends Potion> void register3StageRecipe(Potion origin, IItemProvider initMaterial, T NORMAL, T LONG, T VERY_LONG) {
		registerSplashLinger(NORMAL);
		registerSplashLinger(LONG);
		registerSplashLinger(VERY_LONG);
		
		BrewingRecipeRegistry.addRecipe(new StrictBrewingRecipe(new ItemStack(Items.POTION), origin, new ItemStack(initMaterial), NORMAL));
		BrewingRecipeRegistry.addRecipe(new StrictBrewingRecipe(new ItemStack(Items.POTION), NORMAL, redstone(), LONG));
		BrewingRecipeRegistry.addRecipe(new StrictBrewingRecipe(new ItemStack(Items.POTION), LONG, redstone(), VERY_LONG));

		BrewingRecipeRegistry.addRecipe(new StrictBrewingRecipe(new ItemStack(Items.SPLASH_POTION), origin, new ItemStack(initMaterial), NORMAL));
		BrewingRecipeRegistry.addRecipe(new StrictBrewingRecipe(new ItemStack(Items.SPLASH_POTION), NORMAL, redstone(), LONG));
		BrewingRecipeRegistry.addRecipe(new StrictBrewingRecipe(new ItemStack(Items.SPLASH_POTION), LONG, redstone(), VERY_LONG));

		BrewingRecipeRegistry.addRecipe(new StrictBrewingRecipe(new ItemStack(Items.LINGERING_POTION), origin, new ItemStack(initMaterial), NORMAL));
		BrewingRecipeRegistry.addRecipe(new StrictBrewingRecipe(new ItemStack(Items.LINGERING_POTION), NORMAL, redstone(), LONG));
		BrewingRecipeRegistry.addRecipe(new StrictBrewingRecipe(new ItemStack(Items.LINGERING_POTION), LONG, redstone(), VERY_LONG));
	}
	
	public static <T extends Potion> void register5StageRecipe(IItemProvider initMaterial, T NORMAL, T STRONG, T VERY_STRONG, T LONG, T STRONG_LONG) {
		register5StageRecipe(Potions.AWKWARD, initMaterial, NORMAL, STRONG, VERY_STRONG, LONG, STRONG_LONG);
	}
	
	public static <T extends Potion> void register5StageRecipe(Potion origin, IItemProvider initMaterial, T NORMAL, T STRONG, T VERY_STRONG, T LONG, T STRONG_LONG) {
		registerSplashLinger(NORMAL);
		registerSplashLinger(STRONG);
		registerSplashLinger(VERY_STRONG);
		registerSplashLinger(LONG);
		registerSplashLinger(STRONG_LONG);
		
		BrewingRecipeRegistry.addRecipe(new StrictBrewingRecipe(new ItemStack(Items.POTION), origin, new ItemStack(initMaterial), NORMAL));
		BrewingRecipeRegistry.addRecipe(new StrictBrewingRecipe(new ItemStack(Items.POTION), NORMAL, glowstone(), STRONG));
		BrewingRecipeRegistry.addRecipe(new StrictBrewingRecipe(new ItemStack(Items.POTION), STRONG, glowstone(), VERY_STRONG));
		BrewingRecipeRegistry.addRecipe(new StrictBrewingRecipe(new ItemStack(Items.POTION), NORMAL, redstone(), LONG));
		BrewingRecipeRegistry.addRecipe(new StrictBrewingRecipe(new ItemStack(Items.POTION), STRONG, redstone(), STRONG_LONG));
		BrewingRecipeRegistry.addRecipe(new StrictBrewingRecipe(new ItemStack(Items.POTION), LONG, glowstone(), STRONG_LONG));

		BrewingRecipeRegistry.addRecipe(new StrictBrewingRecipe(new ItemStack(Items.SPLASH_POTION), origin, new ItemStack(initMaterial), NORMAL));
		BrewingRecipeRegistry.addRecipe(new StrictBrewingRecipe(new ItemStack(Items.SPLASH_POTION), NORMAL, glowstone(), STRONG));
		BrewingRecipeRegistry.addRecipe(new StrictBrewingRecipe(new ItemStack(Items.SPLASH_POTION), STRONG, glowstone(), VERY_STRONG));
		BrewingRecipeRegistry.addRecipe(new StrictBrewingRecipe(new ItemStack(Items.SPLASH_POTION), NORMAL, redstone(), LONG));
		BrewingRecipeRegistry.addRecipe(new StrictBrewingRecipe(new ItemStack(Items.SPLASH_POTION), STRONG, redstone(), STRONG_LONG));
		BrewingRecipeRegistry.addRecipe(new StrictBrewingRecipe(new ItemStack(Items.SPLASH_POTION), LONG, glowstone(), STRONG_LONG));

		BrewingRecipeRegistry.addRecipe(new StrictBrewingRecipe(new ItemStack(Items.LINGERING_POTION), origin, new ItemStack(initMaterial), NORMAL));
		BrewingRecipeRegistry.addRecipe(new StrictBrewingRecipe(new ItemStack(Items.LINGERING_POTION), NORMAL, glowstone(), STRONG));
		BrewingRecipeRegistry.addRecipe(new StrictBrewingRecipe(new ItemStack(Items.LINGERING_POTION), STRONG, glowstone(), VERY_STRONG));
		BrewingRecipeRegistry.addRecipe(new StrictBrewingRecipe(new ItemStack(Items.LINGERING_POTION), NORMAL, redstone(), LONG));
		BrewingRecipeRegistry.addRecipe(new StrictBrewingRecipe(new ItemStack(Items.LINGERING_POTION), STRONG, redstone(), STRONG_LONG));
		BrewingRecipeRegistry.addRecipe(new StrictBrewingRecipe(new ItemStack(Items.LINGERING_POTION), LONG, glowstone(), STRONG_LONG));
	}
	
	public static void registerSplashLinger(Potion p) {
		BrewingRecipeRegistry.addRecipe(new StrictBrewingRecipe(new ItemStack(Items.POTION), p, new ItemStack(Items.GUNPOWDER), p, new ItemStack(Items.SPLASH_POTION)));
		BrewingRecipeRegistry.addRecipe(new StrictBrewingRecipe(new ItemStack(Items.POTION), p, new ItemStack(Items.DRAGON_BREATH), p, new ItemStack(Items.LINGERING_POTION)));
	}
	
	protected static class StrictBrewingRecipe extends BrewingRecipe {

		@Nonnull private final ItemStack sContainer;
		@Nonnull private final Potion sInput;
		@Nonnull private final ItemStack sIngredient;
		
		protected StrictBrewingRecipe(ItemStack container, Potion input, ItemStack ingredient, Potion output) {
			this(container.copy(), input, ingredient, output, container.copy());
		}
		
		protected StrictBrewingRecipe(ItemStack container, Potion input, ItemStack ingredient, Potion output, ItemStack containerOut) {
			super(Ingredient.fromStacks(PotionUtils.addPotionToItemStack(container, input)), Ingredient.fromStacks(ingredient), PotionUtils.addPotionToItemStack(containerOut, output));
			sContainer = container;
			sInput = input;
			sIngredient = ingredient;
		}

		@Override
		public boolean isInput(ItemStack stack) {
			return stack.isItemEqual(sContainer) && PotionUtils.getPotionFromItem(stack).equals(sInput);
		}

		@Override
		public boolean isIngredient(ItemStack ingredient) {
			return ingredient.isItemEqual(sIngredient);
		}

	}

}
