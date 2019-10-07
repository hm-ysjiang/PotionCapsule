package hmysjiang.potioncapsule.utils.helper;

import hmysjiang.potioncapsule.PotionCapsule;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;

public class InventoryHelper {
	
	public static ItemStack findStackFromPlayerInventory(PlayerInventory inv, ItemStack sample) {
		for (int i = 0 ; i<inv.getSizeInventory() ; i++) {
			if (inv.getStackInSlot(i).isItemEqual(sample))
				return inv.getStackInSlot(i);
		}
		return PotionCapsule.curioProxy.findCurio(sample, inv.player);
	}
	
}
