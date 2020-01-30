package hmysjiang.potioncapsule.blocks.auto_brewer;

import java.util.ArrayList;
import java.util.List;

import hmysjiang.potioncapsule.configs.CommonConfigs;
import hmysjiang.potioncapsule.container.ContainerAutoBrewer;
import hmysjiang.potioncapsule.init.ModBlocks;
import hmysjiang.potioncapsule.init.ModItems;
import hmysjiang.potioncapsule.items.ItemCapsule;
import hmysjiang.potioncapsule.utils.Defaults;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.PotionUtils;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityAutoBrewer extends TileEntity implements ITickableTileEntity, ICapabilityProvider, INamedContainerProvider {

	public static final TileEntityType<?> TYPE = TileEntityType.Builder.create(TileEntityAutoBrewer::new, ModBlocks.AUTO_BREWER).build(null).setRegistryName(Defaults.modPrefix.apply("tile_auto_brewer"));
	public static final int FUEL_MAX = 32 * 20;
	
	private static final int SLOT_FUEL = 0;
	// 1-6 is ingredient slot
	private static final int SLOT_INPUT_BOTTLE = 7;
	private static final int SLOT_INPUT_CAPSULE = 8;
	private static final int SLOT_CATALYST_GUNPOWDER = 9;
	private static final int SLOT_CATALYST_BREATH = 10;
	private static final int SLOT_CATALYST_INSTANT = 11;
	private static final int SLOT_OUTPUT_NORMAL = 12;
	private static final int SLOT_OUTPUT_SPLASH = 13;
	private static final int SLOT_OUTPUT_LINGER = 14;
	private static final int SLOT_OUTPUT_CAPSULE = 15;
	
	private ItemStackHandler inventory, memory;
	private FluidTank water;
	private boolean memMode;
	private int fuel;
	private int gunpowder;
	private int breath;
	private int catalyst;
	private List<EffectInstance> potion;
	
	public TileEntityAutoBrewer() {
		super(TYPE);
		// BrewingStandTileEntity
		memMode = false;
		memory = new ItemStackHandler(6);
		inventory = new ItemStackHandler(16) {
			@Override
			public boolean isItemValid(int slot, ItemStack stack) {
				switch (slot) {
				case SLOT_FUEL:
					return stack.getItem() == Items.BLAZE_POWDER;
				case SLOT_INPUT_BOTTLE:
					return stack.getItem() == Items.GLASS_BOTTLE;
				case SLOT_INPUT_CAPSULE:
					return ItemCapsule.isItemCapsule(stack) && PotionUtils.getEffectsFromStack(stack).isEmpty();
				case SLOT_CATALYST_GUNPOWDER:
					return stack.getItem() == Items.GUNPOWDER;
				case SLOT_CATALYST_BREATH:
					return stack.getItem() == Items.DRAGON_BREATH;
				case SLOT_CATALYST_INSTANT:
					return stack.getItem() == ModItems.CATALYST;
				case SLOT_OUTPUT_NORMAL:
					return stack.getItem() == Items.POTION;
				case SLOT_OUTPUT_SPLASH:
					return stack.getItem() == Items.SPLASH_POTION;
				case SLOT_OUTPUT_LINGER:
					return stack.getItem() == Items.LINGERING_POTION;
				case SLOT_OUTPUT_CAPSULE:
					return ItemCapsule.isItemCapsule(stack);
				default:
					return !isInputOrCatalysts(stack) && (!memMode || stack.getItem() == memory.getStackInSlot(slot - 1).getItem());
				}
			}
			@Override
			public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
				if (slot >= SLOT_OUTPUT_NORMAL)
					return stack;
				return super.insertItem(slot, stack, simulate);
			}
		};
		water = new FluidTank(4000, fluid -> { return fluid.getFluid() == Fluids.WATER; });
		potion = new ArrayList<EffectInstance>();
	}

	@Override
	public void tick() {
		// Fuel and Catalysts
		if (FUEL_MAX - fuel >= 20 && !inventory.getStackInSlot(SLOT_FUEL).isEmpty()) {
			inventory.getStackInSlot(SLOT_FUEL).shrink(1);
			fuel += 20;
			markDirty();
		}
		if (gunpowder == 0 && !inventory.getStackInSlot(SLOT_CATALYST_GUNPOWDER).isEmpty()) {
			inventory.getStackInSlot(SLOT_CATALYST_GUNPOWDER).shrink(1);
			gunpowder += 3;
			markDirty();
		}
		if (breath == 0 && !inventory.getStackInSlot(SLOT_CATALYST_BREATH).isEmpty()) {
			inventory.getStackInSlot(SLOT_CATALYST_BREATH).shrink(1);
			breath += 3;
			markDirty();
		}
		if (catalyst < CommonConfigs.recipe_instantCatalystAllowed.get() && !inventory.getStackInSlot(SLOT_CATALYST_INSTANT).isEmpty()) {
			inventory.getStackInSlot(SLOT_CATALYST_INSTANT).shrink(1);
			catalyst++;
			markDirty();
		}
		
		// Try to push the ingredients to the front
		if (!memMode) {
			boolean dirty = false;
			for (int i = 1 ; i<=6 ; i++) {
				if (inventory.getStackInSlot(i).isEmpty()) {
					for (int j = i + 1 ; j<=6 ; j++) {
						if (!inventory.getStackInSlot(j).isEmpty()) {
							inventory.insertItem(i, inventory.extractItem(j, 64, false), false);
							dirty = true;
							break;
						}
					}
				}
			}
			if (dirty)
				markDirty();
		}
	}
	
	public boolean checkAndSetRecipe(){
		for (int i = 0 ; i<6 ; i++) {
			memory.setStackInSlot(i, inventory.getStackInSlot(i + 1).copy());
		}
		memMode = true;
		markDirty();
		return true;
	}
	
	public boolean clearMemory(){
		memMode = false;
		for (int i = 0 ; i<6 ; i++) {
			memory.setStackInSlot(i, ItemStack.EMPTY);
		}
		markDirty();
		return true;
	}
	
	public ItemStackHandler getInventory() {
		return inventory;
	}
	
	public ItemStackHandler getMemory() {
		return memory;
	}
	
	public FluidTank getWater() {
		return water;
	}
	
	public boolean isMemMode() {
		return memMode;
	}
	
	public void setMemory(boolean memMode, ItemStackHandler newMem) {
		this.memMode = memMode;
		memory = newMem;
	}
	
	public int getFuel() {
		return fuel;
	}
	
	public int getGunpowder() {
		return gunpowder;
	}
	
	public int getBreath() {
		return breath;
	}
	
	public int getCatalyst() {
		return catalyst;
	}
	
	public List<EffectInstance> getPotion() {
		return potion;
	}
	
	public void clearPotion() {
		potion.clear();
		markDirty();
	}
	
	@Override
	public CompoundNBT write(CompoundNBT compound) {
		compound.put("Inventory", CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.writeNBT(inventory, null));
		compound.put("Memory", CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.writeNBT(memory, null));
		compound.put("Water", CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.writeNBT(water, null));
		compound.putBoolean("MemMode", memMode);
		compound.putInt("Fuel", fuel);
		compound.putInt("Gunpowder", gunpowder);
		compound.putInt("Breath", breath);
		compound.putInt("Catalyst", catalyst);
		compound.put("Potion", writePotion(new CompoundNBT()));
		return super.write(compound);
	}
	
	@Override
	public void read(CompoundNBT compound) {
		super.read(compound);
		CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.readNBT(inventory, null, compound.get("Inventory"));
		CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.readNBT(memory, null, compound.get("Memory"));
		CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.readNBT(water, null, compound.get("Water"));
		memMode = compound.getBoolean("MemMode");
		fuel = compound.getInt("Fuel");
		gunpowder = compound.getInt("Gunpowder");
		breath = compound.getInt("Breath");
		catalyst = compound.getInt("Catalyst");
		potion = readPotion((CompoundNBT) compound.get("Potion"));
	}
	
	protected CompoundNBT writePotion(CompoundNBT nbt) {
		int size = potion.size();
		byte[] id = new byte[size], amp = new byte[size];
		int[] duration = new int[size];
		for (int i = 0 ; i<size ; i++) {
			EffectInstance effect = potion.get(i);
			id[i] = (byte) Effect.getId(effect.getPotion());
			amp[i] = (byte) effect.getAmplifier();
			duration[i] = effect.getDuration();
		}
		nbt.putByteArray("Ids", id);
		nbt.putByteArray("Amp", amp);
		nbt.putIntArray("Duration", duration);
		return nbt;
	}
	
	protected List<EffectInstance> readPotion(CompoundNBT nbt){
		byte[] id = nbt.getByteArray("Ids"), amp = nbt.getByteArray("Amp");
		int[] duration = nbt.getIntArray("Duration");
		List<EffectInstance> potions = new ArrayList<>(id.length);
		for (int i = 0 ; i<id.length ; i++) {
			potions.add(new EffectInstance(Effect.get(id[i]), duration[i], amp[i]));
		}
		return potions;
	}
	
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
			return LazyOptional.of(() -> water).cast();
		if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return LazyOptional.of(() -> inventory).cast();
		return super.getCapability(cap, side);
	}
	
	@Override
	public CompoundNBT getUpdateTag() {
		return this.write(super.getUpdateTag());
	}
	
	@Override
	public void handleUpdateTag(CompoundNBT tag) {
		super.handleUpdateTag(tag);
	}

	@Override
	public Container createMenu(int winId, PlayerInventory playerInv, PlayerEntity player) {
		return new ContainerAutoBrewer(winId, playerInv, pos);
	}

	@Override
	public ITextComponent getDisplayName() {
		return ModItems.BLOCK_AUTO_BREWER.getDisplayName(new ItemStack(ModItems.BLOCK_AUTO_BREWER));
	}
	
	private static boolean isInputOrCatalysts(ItemStack stack) {
		return stack.getItem() == Items.GLASS_BOTTLE || stack.getItem() == Items.GUNPOWDER || stack.getItem() == Items.DRAGON_BREATH || ItemCapsule.isItemCapsule(stack);
	}

}
