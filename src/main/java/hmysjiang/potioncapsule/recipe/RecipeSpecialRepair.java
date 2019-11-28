package hmysjiang.potioncapsule.recipe;

import hmysjiang.potioncapsule.items.ItemSpecialCapsule;
import hmysjiang.potioncapsule.items.ItemSpecialCapsule.EnumSpecialType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class RecipeSpecialRepair {
	
	private ItemStack toRepair = ItemStack.EMPTY;
	private ItemStack repaired = ItemStack.EMPTY;
	private ResourceLocation repairs;
	
	public RecipeSpecialRepair(EnumSpecialType type) {
		this.toRepair = new ItemStack(ItemSpecialCapsule.getCapsuleInstance(type));
		this.repaired = new ItemStack(ItemSpecialCapsule.getCapsuleInstance(type));
		this.repairs = type.getRepairTag();
		toRepair.setDamage(toRepair.getMaxDamage());
		repaired.setDamage(repaired.getMaxDamage() - 1);
	}
	
	public ResourceLocation getRepairs() {
		return repairs;
	}
	
	public ItemStack getToRepair() {
		return toRepair;
	}
	
	public ItemStack getRepaired() {
		return repaired;
	}
	
}
