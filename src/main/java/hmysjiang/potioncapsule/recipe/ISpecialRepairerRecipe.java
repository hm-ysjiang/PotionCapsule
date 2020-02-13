package hmysjiang.potioncapsule.recipe;

import hmysjiang.potioncapsule.blocks.special_capsule_repairer.InventorySpecialCapsuleRepairer;
import hmysjiang.potioncapsule.utils.Defaults;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;

public interface ISpecialRepairerRecipe extends IRecipe<InventorySpecialCapsuleRepairer> {
	
	public static final IRecipeType<ISpecialRepairerRecipe> TYPE = new IRecipeType<ISpecialRepairerRecipe>() {
		@Override public String toString() { return Defaults.modPrefix.apply("recipetype_special_repair").toString(); }
	};
	
}
