package hmysjiang.potioncapsule.compat.jei.recipe;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

public class RecipeSpecialRepairJei {
	
	public final int amount;
	private ItemStack capsule, output;
	private Ingredient ingredient;
	
	public RecipeSpecialRepairJei(Item capsuleIn, Ingredient ingredientIn, int amountIn) {
		capsule = new ItemStack(capsuleIn);
		capsule.setDamage(capsule.getMaxDamage());
		output = capsule.copy();
		output.setDamage(capsule.getMaxDamage() - amountIn);
		ingredient = ingredientIn;
		amount = amountIn;
	}

	public ItemStack getCapsule() {
		return capsule;
	}

	public Ingredient getIngredient() {
		return ingredient;
	}

	public ItemStack getOutput() {
		return output;
	}
	
}
