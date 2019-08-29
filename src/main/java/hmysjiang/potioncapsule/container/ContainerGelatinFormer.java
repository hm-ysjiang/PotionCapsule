package hmysjiang.potioncapsule.container;

import hmysjiang.potioncapsule.blocks.gelatin_former.TileEntityGelatinFormer;
import hmysjiang.potioncapsule.utils.Defaults;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerGelatinFormer extends BaseContainer {
	
	@SuppressWarnings("unchecked")
	public static final ContainerType<ContainerGelatinFormer> TYPE = (ContainerType<ContainerGelatinFormer>) IForgeContainerType
			.create((winId, inv, data) -> (new ContainerGelatinFormer(winId, inv, data.readBlockPos())))
			.setRegistryName(Defaults.modPrefix.apply("container_gelatin_form"));
	private TileEntityGelatinFormer tile = null;
	private PlayerInventory inv;
	
	public ContainerGelatinFormer(int id, PlayerInventory invIn, BlockPos pos) {
		super(TYPE, id, 3);
		
		tile = (TileEntityGelatinFormer) invIn.player.world.getTileEntity(pos);
		
		tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(handler -> {
			addSlot(new SlotItemHandler(handler, 0, 24, 35));
			addSlot(new SlotItemHandler(handler, 1, 46, 35));
			addSlot(new SlotItemHandler(handler, 2, 125, 35));
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
	
	public TileEntityGelatinFormer getTile() {
		return tile;
	}

}
