package hmysjiang.potioncapsule.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public abstract class BaseContainer extends Container {
	private int invSize;

	protected BaseContainer(ContainerType<?> type, int id, int invSize) {
		super(type, id);
		this.invSize = invSize;
	}
	
	@Override
	public boolean canInteractWith(PlayerEntity playerIn) {
		return true;
	}
	
	public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(index);
		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			if (index < invSize) {
				if (!this.mergeItemStack(itemstack1, invSize, this.inventorySlots.size(), true)) {
					return ItemStack.EMPTY;
				}
			}
			else if (!this.mergeItemStack(itemstack1, 0, invSize, false)) {
				return ItemStack.EMPTY;
			}

			if (itemstack1.isEmpty()) {
				slot.putStack(ItemStack.EMPTY);
			} 
			else {
				slot.onSlotChanged();
			}
		}
		return itemstack;
	}

}
