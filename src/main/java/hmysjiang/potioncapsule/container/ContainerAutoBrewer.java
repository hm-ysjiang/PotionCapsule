package hmysjiang.potioncapsule.container;

import hmysjiang.potioncapsule.blocks.auto_brewer.TileEntityAutoBrewer;
import hmysjiang.potioncapsule.utils.Defaults;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerAutoBrewer extends BaseContainer {

	@SuppressWarnings("unchecked")
	public static final ContainerType<ContainerAutoBrewer> TYPE = (ContainerType<ContainerAutoBrewer>) IForgeContainerType
			.create((winId, inv, data) -> (new ContainerAutoBrewer(winId, inv, data.readBlockPos())))
			.setRegistryName(Defaults.modPrefix.apply("container_auto_brewer"));
	
	public final TileEntityAutoBrewer brewer;
	public final ItemStackHandler inventory;
	
	public ContainerAutoBrewer(int id, PlayerInventory playerInv, BlockPos posIn) {
		super(TYPE, id, 15);
		
		brewer = (TileEntityAutoBrewer) playerInv.player.world.getTileEntity(posIn);
		inventory = brewer.getInventory();

		addSlot(new SlotItemHandler(inventory, 0, 8, 95));
		for (int i = 1 ; i<=6 ; i++)
			addSlot(new SlotItemHandler(inventory, i, 80, 2 + 18 * i));
		addSlot(new SlotItemHandler(inventory, 7, 126, 20));
		addSlot(new SlotItemHandler(inventory, 8, 152, 20));
		addSlot(new SlotItemHandler(inventory, 9, 105, 92));
		addSlot(new SlotItemHandler(inventory, 10, 105, 110));
		addSlot(new SlotItemHandler(inventory, 11, 152, 92));
		addSlot(new SlotItemHandler(inventory, 12, 126, 74));
		addSlot(new SlotItemHandler(inventory, 13, 126, 92));
		addSlot(new SlotItemHandler(inventory, 14, 126, 110));
		addSlot(new SlotItemHandler(inventory, 15, 152, 74));
		
		int xPos = 8;
		int yPos = 142;
		
		for (int y = 0; y < 3; ++y) {
			for (int x = 0; x < 9; ++x) {
				this.addSlot(new Slot(playerInv, x + y * 9 + 9, xPos + x * 18, yPos + y * 18));
			}
		}
		
		for (int x = 0; x < 9; ++x) {
			this.addSlot(new Slot(playerInv, x, xPos + x * 18, yPos + 58));
		}
	}

}
