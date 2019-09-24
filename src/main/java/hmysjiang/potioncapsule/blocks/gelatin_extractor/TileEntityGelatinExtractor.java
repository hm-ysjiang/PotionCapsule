package hmysjiang.potioncapsule.blocks.gelatin_extractor;

import hmysjiang.potioncapsule.container.ContainerGelatinExtractor;
import hmysjiang.potioncapsule.init.ModBlocks;
import hmysjiang.potioncapsule.init.ModItems;
import hmysjiang.potioncapsule.recipe.RecipeGelatinExtractor;
import hmysjiang.potioncapsule.utils.Defaults;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityGelatinExtractor extends TileEntity implements ITickableTileEntity, ICapabilityProvider, INamedContainerProvider {
	
	public static final TileEntityType<?> TYPE = TileEntityType.Builder.create(TileEntityGelatinExtractor::new, ModBlocks.GELATIN_EXTRACTOR).build(null).setRegistryName(Defaults.modPrefix.apply("tile_gelatin_extractor"));
	private ItemStackHandler handler;
	private InventoryGelatinExtractor invWrapper;
	private int full_countdown = -1;
	private int working_countdown;
	private int nextOutput = 0;
	
	public TileEntityGelatinExtractor() {
		super(TYPE);
		
		handler = new ItemStackHandler(2) {
			@Override
			public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
				return slot == 1 ? stack : super.insertItem(slot, stack, simulate);
			}
		};
		invWrapper = new InventoryGelatinExtractor(handler);
		working_countdown = -1;
	}

	@Override
	public void tick() {
		if (working_countdown == -1) {
			world.getRecipeManager().getRecipe(RecipeGelatinExtractor.TYPE, invWrapper, world).ifPresent(r -> {
				if (handler.getStackInSlot(1).getCount() + r.getOutputCount() <= 64) {
					full_countdown = r.getTickCost();
					working_countdown = full_countdown;
					nextOutput = r.getCraftingResult(invWrapper).getCount();
					this.markDirty();
				}
			});
		}
		else {
			if (working_countdown-- == 0) {
				if (handler.getStackInSlot(1).getCount() >= 64) {
					working_countdown ++;
				}
				else {
					if (handler.getStackInSlot(1).isEmpty()) {
						handler.setStackInSlot(1, new ItemStack(ModItems.GELATIN_POWDER, nextOutput));
					}
					else {
						handler.getStackInSlot(1).grow(nextOutput);
					}
					
					world.getRecipeManager().getRecipe(RecipeGelatinExtractor.TYPE, invWrapper, world).ifPresent(r -> {
						if (handler.getStackInSlot(1).getCount() + r.getOutputCount() <= 64) {
							full_countdown = r.getTickCost();
							working_countdown = full_countdown;
							nextOutput = r.getCraftingResult(invWrapper).getCount();
						}
					});
					this.markDirty();
				}
			}
		}
	}
	
	public boolean isWorking() {
		return working_countdown >= 0;
	}
	
	public float getWorkedPercnetage() {
		if (!isWorking())
			return 0.0F;
		return ((float) (full_countdown - working_countdown) / (float) full_countdown);
	}
	
	public int getWorkedPercentagePixel() {
		if (!isWorking())
			return 0;
		return (int) (23 * getWorkedPercnetage() + 1);
	}
	
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return LazyOptional.of(() -> handler).cast();
		return super.getCapability(cap, side);
	}
	
	public ItemStackHandler getHandler() {
		return handler;
	}
	
	@Override
	public CompoundNBT write(CompoundNBT compound) {
		compound.put("item_cap", CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.writeNBT(handler, null));
		compound.putInt("countdown", working_countdown);
		compound.putInt("full", full_countdown);
		compound.putInt("nextOutput", nextOutput);
		return super.write(compound);
	}
	
	@Override
	public void read(CompoundNBT compound) {
		super.read(compound);
		CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.readNBT(handler, null, compound.get("item_cap"));
		working_countdown = compound.getInt("countdown");
		full_countdown = compound.getInt("full");
		nextOutput = compound.getInt("nextOutput");
	}
	
	@Override
	public CompoundNBT getUpdateTag() {
		return this.write(super.getUpdateTag());
	}
	
	@Override
	public Container createMenu(int winId, PlayerInventory inv, PlayerEntity player) {
		return new ContainerGelatinExtractor(winId, inv, pos);
	}

	@Override
	public ITextComponent getDisplayName() {
		return ModItems.BLOCK_GELATIN_EXTRACTOR.getDisplayName(new ItemStack(ModItems.BLOCK_GELATIN_EXTRACTOR));
	}
	
}
