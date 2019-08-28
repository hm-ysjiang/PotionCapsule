package hmysjiang.potioncapsule.container;

import hmysjiang.potioncapsule.blocks.gelatin.TileEntityGelatinExtractor;
import hmysjiang.potioncapsule.utils.Defaults;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerGelatinExtractor extends Container {
	
	@SuppressWarnings("unchecked")
	public static final ContainerType<ContainerGelatinExtractor> TYPE = (ContainerType<ContainerGelatinExtractor>) IForgeContainerType
			.create((winId, inv, data) -> (new ContainerGelatinExtractor(winId, inv, data.readBlockPos())))
			.setRegistryName(Defaults.modPrefix.apply("container_gelatin_ex"));
	private TileEntityGelatinExtractor tile = null;
	private PlayerInventory inv;
	
	public ContainerGelatinExtractor(int id, PlayerInventory invIn, BlockPos pos) {
		super(TYPE, id);
		
		tile = (TileEntityGelatinExtractor) invIn.player.world.getTileEntity(pos);
		
		tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(handler -> {
			addSlot(new SlotItemHandler(handler, 0, 57, 35));
			addSlot(new SlotItemHandler(handler, 1, 103, 35));
		});
		
		inv = invIn;
		
		int xPos = 8;
		int yPos = 84;
		
		for (int y = 0; y < 3; ++y) {
			for (int x = 0; x < 9; ++x) {
				this.addSlot(new Slot(inv, x + y * 9 + 9, xPos + x * 18, yPos + y * 18));
			}
		}
		
		for (int x = 0; x < 9; ++x) {
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
			if (index < 2) {
				if (!this.mergeItemStack(itemstack1, 2, this.inventorySlots.size(), true)) {
					return ItemStack.EMPTY;
				}
			} 
			else if (!this.mergeItemStack(itemstack1, 0, 2, false)) {
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
	
	public TileEntityGelatinExtractor getTile() {
		return tile;
	}

}
