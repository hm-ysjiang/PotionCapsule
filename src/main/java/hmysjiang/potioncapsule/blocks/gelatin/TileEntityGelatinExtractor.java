package hmysjiang.potioncapsule.blocks.gelatin;

import hmysjiang.potioncapsule.init.ModBlocks;
import hmysjiang.potioncapsule.utils.Defaults;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityGelatinExtractor extends TileEntity implements ITickableTileEntity, ICapabilityProvider {
	
	public static final TileEntityType<?> TYPE = TileEntityType.Builder.create(TileEntityGelatinExtractor::new, ModBlocks.GELATIN_EXTRACTOR).build(null).setRegistryName(Defaults.modPrefix.apply("tile_gelatin_extractor"));
	private ItemStackHandler handler;
	
	public TileEntityGelatinExtractor() {
		super(TYPE);
		
		handler = new ItemStackHandler(1) {
			@Override
			public boolean isItemValid(int slot, ItemStack stack) {
				return stack.getItem() == Items.BONE || stack.getItem() == Items.LEATHER; 
			}
		};
	}

	@Override
	public void tick() {
		
	}
	
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && ((BlockGelatinExtractor)getBlockState().getBlock()).isOutputSide(getBlockState(), side))
			return LazyOptional.of(() -> handler).cast();
		return super.getCapability(cap, side);
	}
	
	@Override
	public CompoundNBT write(CompoundNBT compound) {
		compound.put("item_cap", CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.writeNBT(handler, null));
		return super.write(compound);
	}
	
	@Override
	public void read(CompoundNBT compound) {
		super.read(compound);
		CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.readNBT(handler, null, compound.get("item_cap"));
	}
	
}
