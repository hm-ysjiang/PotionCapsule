package hmysjiang.potioncapsule.utils.helper;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class InventoryHelper {
	
	public static ItemStack findStackFromInventory(IInventory inv, ItemStack sample) {
		for (int i = 0 ; i<inv.getSizeInventory() ; i++) {
			if (inv.getStackInSlot(i).isItemEqual(sample))
				return inv.getStackInSlot(i);
		}
		return ItemStack.EMPTY;
	}
	
}
