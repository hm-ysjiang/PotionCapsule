package hmysjiang.potioncapsule.utils.helper;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.util.IItemProvider;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;

public class PotionHelper {
	
	public static Ingredient awkward() {
		return Ingredient.fromStacks(PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), Potions.AWKWARD));
	}
	
	public static Ingredient glowstone() {
		return Ingredient.fromItems(Items.GLOWSTONE_DUST);
	}
	
	public static Ingredient redstone() {
		return Ingredient.fromItems(Items.REDSTONE);
	}
	
	public static Ingredient gunpowder() {
		return Ingredient.fromItems(Items.GUNPOWDER);
	}
	
	public static Ingredient breath() {
		return Ingredient.fromItems(Items.DRAGON_BREATH);
	}
	
	public static ItemStack potion(Potion p) {
		return PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), p);
	}
	
	public static Ingredient ipotion(Potion p) {
		return Ingredient.fromStacks(potion(p));
	}
	
	public static ItemStack potionSplash(Potion p) {
		return PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), p);
	}
	
	public static Ingredient ipotionSplash(Potion p) {
		return Ingredient.fromStacks(potionSplash(p));
	}
	
	public static ItemStack potionLinger(Potion p) {
		return PotionUtils.addPotionToItemStack(new ItemStack(Items.LINGERING_POTION), p);
	}
	
	public static Ingredient ipotionLinger(Potion p) {
		return Ingredient.fromStacks(potionLinger(p));
	}
	
	public static <T extends Potion> void register5StageRecipe(IItemProvider initMaterial, T NORMAL, T STRONG, T VERY_STRONG, T LONG, T STRONG_LONG) {
		register5StageRecipe(Potions.AWKWARD, initMaterial, NORMAL, STRONG, VERY_STRONG, LONG, STRONG_LONG);
	}
	
	public static <T extends Potion> void register5StageRecipe(Potion origin, IItemProvider initMaterial, T NORMAL, T STRONG, T VERY_STRONG, T LONG, T STRONG_LONG) {
		BrewingRecipeRegistry.addRecipe(ipotion(NORMAL),		gunpowder(), potionSplash(NORMAL));
		BrewingRecipeRegistry.addRecipe(ipotion(STRONG),		gunpowder(), potionSplash(STRONG));
		BrewingRecipeRegistry.addRecipe(ipotion(VERY_STRONG),	gunpowder(), potionSplash(VERY_STRONG));
		BrewingRecipeRegistry.addRecipe(ipotion(LONG),			gunpowder(), potionSplash(LONG));
		BrewingRecipeRegistry.addRecipe(ipotion(STRONG_LONG),	gunpowder(), potionSplash(STRONG_LONG));

		BrewingRecipeRegistry.addRecipe(ipotion(NORMAL),		breath(), potionLinger(NORMAL));
		BrewingRecipeRegistry.addRecipe(ipotion(STRONG),		breath(), potionLinger(STRONG));
		BrewingRecipeRegistry.addRecipe(ipotion(VERY_STRONG),	breath(), potionLinger(VERY_STRONG));
		BrewingRecipeRegistry.addRecipe(ipotion(LONG),			breath(), potionLinger(LONG));
		BrewingRecipeRegistry.addRecipe(ipotion(STRONG_LONG),	breath(), potionLinger(STRONG_LONG));
		
		BrewingRecipeRegistry.addRecipe(ipotion(origin),	Ingredient.fromItems(initMaterial), potion(NORMAL));
		BrewingRecipeRegistry.addRecipe(ipotion(NORMAL),	glowstone(),						potion(STRONG));
		BrewingRecipeRegistry.addRecipe(ipotion(STRONG),	glowstone(),						potion(VERY_STRONG));
		BrewingRecipeRegistry.addRecipe(ipotion(NORMAL),	redstone(),							potion(LONG));
		BrewingRecipeRegistry.addRecipe(ipotion(STRONG),	redstone(),							potion(STRONG_LONG));
		BrewingRecipeRegistry.addRecipe(ipotion(LONG),		glowstone(),						potion(STRONG_LONG));
		
		BrewingRecipeRegistry.addRecipe(ipotionSplash(origin),	Ingredient.fromItems(initMaterial), potionSplash(NORMAL));
		BrewingRecipeRegistry.addRecipe(ipotionSplash(NORMAL),	glowstone(),						potionSplash(STRONG));
		BrewingRecipeRegistry.addRecipe(ipotionSplash(STRONG),	glowstone(),						potionSplash(VERY_STRONG));
		BrewingRecipeRegistry.addRecipe(ipotionSplash(NORMAL),	redstone(),							potionSplash(LONG));
		BrewingRecipeRegistry.addRecipe(ipotionSplash(STRONG),	redstone(),							potionSplash(STRONG_LONG));
		BrewingRecipeRegistry.addRecipe(ipotionSplash(LONG),	glowstone(),						potionSplash(STRONG_LONG));
		
		BrewingRecipeRegistry.addRecipe(ipotionLinger(origin),	Ingredient.fromItems(initMaterial), potionLinger(NORMAL));
		BrewingRecipeRegistry.addRecipe(ipotionLinger(NORMAL),	glowstone(),						potionLinger(STRONG));
		BrewingRecipeRegistry.addRecipe(ipotionLinger(STRONG),	glowstone(),						potionLinger(VERY_STRONG));
		BrewingRecipeRegistry.addRecipe(ipotionLinger(NORMAL),	redstone(),							potionLinger(LONG));
		BrewingRecipeRegistry.addRecipe(ipotionLinger(STRONG),	redstone(),							potionLinger(STRONG_LONG));
		BrewingRecipeRegistry.addRecipe(ipotionLinger(LONG),	glowstone(),						potionLinger(STRONG_LONG));
	}
	
}
