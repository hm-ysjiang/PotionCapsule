package hmysjiang.potioncapsule.blocks.auto_brewer;

import hmysjiang.potioncapsule.init.ModBlocks;
import hmysjiang.potioncapsule.init.ModItems;
import hmysjiang.potioncapsule.utils.Defaults;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tileentity.BrewingStandTileEntity;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityAutoBrewer extends TileEntity implements ITickableTileEntity, ICapabilityProvider, INamedContainerProvider {

	public static final TileEntityType<?> TYPE = TileEntityType.Builder.create(TileEntityAutoBrewer::new, ModBlocks.AUTO_BREWER).build(null).setRegistryName(Defaults.modPrefix.apply("tile_auto_brewer"));

	private static final int FUEL_MAX = 64 * 20;
	private static final int SLOT_FUEL = 0;
	// 1-6 is ingredient slot
	private static final int SLOT_INPUT = 7;
	private static final int SLOT_CATALYST = 8;
	private static final int SLOT_OUTPUT = 9;
	
	private ItemStackHandler inventory, memory;
	private boolean memMode;
	private int fuel;
	
	public TileEntityAutoBrewer() {
		super(TYPE);
		// BrewingStandTileEntity
		memMode = false;
		memory = new ItemStackHandler(6);
		inventory = new ItemStackHandler(10) {
			@Override
			public boolean isItemValid(int slot, ItemStack stack) {
				switch (slot) {
				case SLOT_FUEL:
					return stack.getItem() == Items.BLAZE_POWDER;
				case SLOT_CATALYST:
					return stack.getItem() == ModItems.CATALYST;
				case SLOT_INPUT:
					return stack.getItem() == ModItems.CAPSULE || stack.getItem() == ModItems.CAPSULE_INSTANT;
				}
				return super.isItemValid(slot, stack);
			}
		};
	}

	@Override
	public void tick() {
		if (FUEL_MAX - fuel >= 20) {
			
		}
	}

	@Override
	public Container createMenu(int p_createMenu_1_, PlayerInventory p_createMenu_2_, PlayerEntity p_createMenu_3_) {
		return null;
	}

	@Override
	public ITextComponent getDisplayName() {
		return ModItems.BLOCK_AUTO_BREWER.getDisplayName(new ItemStack(ModItems.BLOCK_AUTO_BREWER));
	}

}
