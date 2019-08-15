package hmysjiang.potioncapsule.container;

import hmysjiang.potioncapsule.items.ItemCapsule;
import hmysjiang.potioncapsule.utils.Defaults;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerPendant extends Container {
	
	@SuppressWarnings("unchecked")
	public static final ContainerType<ContainerPendant> TYPE = (ContainerType<ContainerPendant>) IForgeContainerType
			.create((winId, inv, data) -> (new ContainerPendant(winId, inv, data.readBoolean() ? Hand.MAIN_HAND : Hand.OFF_HAND)))
			.setRegistryName(Defaults.modPrefix.apply("container_pendant"));
	private ItemStack stack;
	private ItemStackHandler handler = null;
	private PlayerInventory inv;
	
	public ContainerPendant(int id, PlayerInventory invIn, Hand hand) {
		super(TYPE, id);
		stack = invIn.player.getHeldItem(hand);
		inv = invIn;
		
		stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(handler -> {
			this.handler = (ItemStackHandler) handler;
			addSlot(new SlotCapsule(handler, 0, 80, 18));
			addSlot(new SlotCapsule(handler, 1, 40, 34));
			addSlot(new SlotCapsule(handler, 2, 120, 34));
			addSlot(new SlotCapsule(handler, 3, 20, 69));
			addSlot(new SlotCapsule(handler, 4, 140, 69));
			addSlot(new SlotCapsule(handler, 5, 40, 104));
			addSlot(new SlotCapsule(handler, 6, 120, 104));
			addSlot(new SlotCapsule(handler, 7, 80, 120));
		});
		
		int xPos = 8;
		int yPos = 146;
		
		for (int y = 0; y < 3; ++y) {
			for (int x = 0; x < 9; ++x) {
				this.addSlot(new Slot(inv, x + y * 9 + 9, xPos + x * 18, yPos + y * 18));
			}
		}
		
		for (int x = 0; x < 9; ++x) {
			if (inv.getStackInSlot(x) == stack)
				this.addSlot(new Slot(inv, x, xPos + x * 18, yPos + 58) {
					@Override public boolean canTakeStack(PlayerEntity playerIn) { return false; }
					@Override public boolean isItemValid(ItemStack stack) { return false; }
				});
			else
				this.addSlot(new Slot(inv, x, xPos + x * 18, yPos + 58));
		}
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
			if (index < 8) {
				if (!this.mergeItemStack(itemstack1, 8, this.inventorySlots.size(), true)) {
					return ItemStack.EMPTY;
				}
			} 
			else if (!this.mergeItemStack(itemstack1, 0, 8, false)) {
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
	
	public ItemStackHandler getHandler() {
		return handler;
	}
	
	@Override
	public void onContainerClosed(PlayerEntity playerIn) {
		super.onContainerClosed(playerIn);
		stack.getOrCreateTag().putBoolean("opened", false);
	}
	
	private static class SlotCapsule extends SlotItemHandler {

		public SlotCapsule(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
			super(itemHandler, index, xPosition, yPosition);
		}
		
		@Override
		public boolean isItemValid(ItemStack stack) {
			return super.isItemValid(stack) && stack.getItem() instanceof ItemCapsule;
		}
		
	}

}
