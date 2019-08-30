package hmysjiang.potioncapsule.blocks.gelatin_former;

import hmysjiang.potioncapsule.container.ContainerGelatinFormer;
import hmysjiang.potioncapsule.init.ModBlocks;
import hmysjiang.potioncapsule.init.ModItems;
import hmysjiang.potioncapsule.recipe.RecipeGelatinFormer;
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
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityGelatinFormer extends TileEntity implements ITickableTileEntity, ICapabilityProvider, INamedContainerProvider {
	
	public static final TileEntityType<?> TYPE = TileEntityType.Builder.create(TileEntityGelatinFormer::new, ModBlocks.GELATIN_FORMER).build(null).setRegistryName(Defaults.modPrefix.apply("tile_gelatin_former"));
	private static final ResourceLocation NO_RECIPE_FOUND = Defaults.modPrefix.apply("recipe_not_found");
	
	private ItemStackHandler handler;
	private InventoryGelatinFormer invWrapper;
	private int full_countdown = -1;
	private int working_countdown;
	private ResourceLocation curRecipe;
	
	public TileEntityGelatinFormer() {
		super(TYPE);
		
		handler = new ItemStackHandler(3) {
			@Override
			public boolean isItemValid(int slot, ItemStack stack) {
				if (slot == 0)
					return stack.getItem() == ModItems.GELATIN_POWDER;
				if (slot == 1)
					return stack.getItem() != ModItems.GELATIN_POWDER;
				return true;
			}
			@Override
			public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
				return slot == 2 ? stack : super.insertItem(slot, stack, simulate);
			}
		};
		invWrapper = new InventoryGelatinFormer(handler);
		working_countdown = -1;
		curRecipe = NO_RECIPE_FOUND;
	}

	@Override
	public void tick() {
		if (working_countdown == -1) {
			world.getRecipeManager().getRecipe(RecipeGelatinFormer.TYPE, invWrapper, world).ifPresent(r -> {
				if (handler.getStackInSlot(2).isEmpty() || 
						(handler.getStackInSlot(2).isItemEqual(r.getResult()) && handler.getStackInSlot(2).getCount() + r.getResult().getCount() <= 64)) {
					full_countdown = r.getTickCost();
					working_countdown = full_countdown;
					r.getCraftingResult(invWrapper);
					curRecipe = r.getId();
					this.markDirty();
				}
			});
		}
		else {
			if (working_countdown-- == 0) {
				if (handler.getStackInSlot(2).getCount() >= 64) {
					working_countdown ++;
				}
				else {
					world.getRecipeManager().getRecipe(curRecipe).ifPresent(r -> {
						if (handler.getStackInSlot(2).isEmpty()) {
							handler.setStackInSlot(2, r.getRecipeOutput());
						}
						else if (r.getRecipeOutput().isItemEqual(handler.getStackInSlot(2))) {
							handler.getStackInSlot(2).grow(r.getRecipeOutput().getCount());
						}
					});
					
					world.getRecipeManager().getRecipe(RecipeGelatinFormer.TYPE, invWrapper, world).ifPresent(r -> {
						if (handler.getStackInSlot(2).isEmpty() || 
								(handler.getStackInSlot(2).isItemEqual(r.getResult()) && handler.getStackInSlot(2).getCount() + r.getResult().getCount() <= 64)) {
							full_countdown = r.getTickCost();
							working_countdown = full_countdown;
							r.getCraftingResult(invWrapper);
							curRecipe = r.getId();
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
		compound.putString("curRecipe", curRecipe.toString());
		return super.write(compound);
	}
	
	@Override
	public void read(CompoundNBT compound) {
		super.read(compound);
		CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.readNBT(handler, null, compound.get("item_cap"));
		working_countdown = compound.getInt("countdown");
		full_countdown = compound.getInt("full");
		curRecipe = new ResourceLocation(compound.getString("curRecipe"));
	}
	
	@Override
	public CompoundNBT getUpdateTag() {
		return this.write(super.getUpdateTag());
	}

	@Override
	public Container createMenu(int winId, PlayerInventory inv, PlayerEntity player) {
		return new ContainerGelatinFormer(winId, inv, pos);
	}

	@Override
	public ITextComponent getDisplayName() {
		return ModItems.BLOCK_GELATIN_FORMER.getDisplayName(ModItems.BLOCK_GELATIN_FORMER.getDefaultInstance());
	}

}
