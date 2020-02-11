package hmysjiang.potioncapsule.blocks.auto_brewer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import hmysjiang.potioncapsule.configs.CommonConfigs;
import hmysjiang.potioncapsule.container.ContainerAutoBrewer;
import hmysjiang.potioncapsule.init.ModBlocks;
import hmysjiang.potioncapsule.init.ModItems;
import hmysjiang.potioncapsule.items.ItemCapsule;
import hmysjiang.potioncapsule.items.ItemCapsule.EnumCapsuleType;
import hmysjiang.potioncapsule.network.PacketHandler;
import hmysjiang.potioncapsule.potions.effects.EffectNightVisionNF;
import hmysjiang.potioncapsule.utils.Defaults;
import hmysjiang.potioncapsule.utils.ITileCustomSync;
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
import net.minecraft.potion.Effects;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityAutoBrewer extends TileEntity implements ITickableTileEntity, ICapabilityProvider, INamedContainerProvider, ITileCustomSync {

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
	
	private Inventory inventory;
	private ItemStackHandler memory;
	private FluidTank water;
	private boolean memMode;
	private int fuel;
	private int gunpowder;
	private int breath;
	private int catalyst;
	private List<EffectInstance> potion;
	private int brewtime;
	private int brewsteps;
	private boolean brewing = false;
	private ItemStack output = ItemStack.EMPTY;
	private int partition = 5;
	
	public TileEntityAutoBrewer() {
		super(TYPE);
		// BrewingStandTileEntity
		memMode = false;
		memory = new ItemStackHandler(6);
		inventory = new Inventory(16) {
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
		if (world.isRemote)
			return;
		
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
			int toadd = Math.min(CommonConfigs.recipe_instantCatalystAllowed.get() - catalyst, inventory.getStackInSlot(SLOT_CATALYST_INSTANT).getCount());
			inventory.getStackInSlot(SLOT_CATALYST_INSTANT).shrink(toadd);
			catalyst += toadd;
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
			if (dirty) {
				markDirty();
			}
		}
		
		// Brewing
		if (memMode) {
			// Start brew
			if (!brewing && brewtime == 0) {
				brewing = true;
				// Check if has ingredients 
				for (int i = 1 ; i<=brewsteps ; i++) {
					if (inventory.getStackInSlot(i).isEmpty()){
						brewing = false;
						break;
					}
				}
				if (fuel <= 0 || water.getFluidAmount() < 1000 || !checkOutputSpace())
					brewing = false;
				
				if (brewing) {
					for (int i = 1; i <= brewsteps; i++) {
						inventory.getStackInSlot(i).shrink(1);
					}
					brewtime = 600;
					water.drain(1000, FluidAction.EXECUTE);
					fuel--;
					markDirty();
				}
			}
			else {
				brewtime--;
				markDirty();
			}
			
			// Check done
			if (brewing && brewtime == 0) {
				brewing = false;
				mergeOutput(true);
				markDirty();
			}
		}
		
		// Fill the potion into the outputs
		if (potion.size() > 0) {
			boolean shouldWork = true;
			if (shouldWork && !inventory.getStackInSlot(SLOT_INPUT_CAPSULE).isEmpty()) {
				int toApply = -1;
				for (int i = 0 ; i<potion.size() ; i++) {
					if (ItemCapsule.canApplyEffectOnCapsule(inventory.getStackInSlot(SLOT_INPUT_CAPSULE), potion.get(i).getPotion()) && (ItemCapsule.getCapsuleType(inventory.getStackInSlot(SLOT_INPUT_CAPSULE).getItem()) == EnumCapsuleType.INSTANT || potion.get(i).getDuration() >= partition * 20)) {
						toApply = i;
						break;
					}
				}
				if (toApply >= 0) {
					EnumCapsuleType type = ItemCapsule.getCapsuleType(inventory.getStackInSlot(SLOT_INPUT_CAPSULE).getItem());
					if (type == EnumCapsuleType.INSTANT) {
						ItemStack result = inventory.extractItem(SLOT_INPUT_CAPSULE, catalyst + 1, true).copy();
						PotionUtils.appendEffects(result, Arrays.asList(potion.get(toApply)));
						ItemStack remain = inventory.insertSuper(SLOT_OUTPUT_CAPSULE, result, false);
						int inserted = result.getCount() - remain.getCount();
						if (inserted > 0) {
							inventory.getStackInSlot(SLOT_INPUT_CAPSULE).shrink(inserted);
							catalyst -= (inserted - 1);
							potion.remove(toApply);
							markDirty();
							shouldWork = false;
						}
					}
					else {
						ItemStack result = inventory.extractItem(SLOT_INPUT_CAPSULE, 1, true).copy();
						EffectInstance app = new EffectInstance(potion.get(toApply));
						app.duration = partition * 20;
						PotionUtils.appendEffects(result, Arrays.asList(app));
						if (inventory.insertSuper(SLOT_OUTPUT_CAPSULE, result, false).isEmpty()) {
							inventory.getStackInSlot(SLOT_INPUT_CAPSULE).shrink(1);
							potion.get(toApply).duration -= partition * 20;
							if (potion.get(toApply).getDuration() <= 0)
								potion.remove(toApply);
							markDirty();
							shouldWork = false;
						}
					}
				}
			}
			
			if (shouldWork && !inventory.getStackInSlot(SLOT_INPUT_BOTTLE).isEmpty()) {
				if (breath > 0) {
					ItemStack linger = new ItemStack(Items.LINGERING_POTION);
					EffectInstance app = new EffectInstance(potion.get(0));
					if (!app.getPotion().isInstant())
						app.duration = partition * 20;
					PotionUtils.appendEffects(linger, Arrays.asList(app));
					linger.getOrCreateTag().putInt("CustomPotionColor", PotionUtils.getPotionColorFromEffectList(PotionUtils.getEffectsFromStack(linger)));
					linger.setDisplayName(new TranslationTextComponent("potioncapsule.misc.custom_potion_display_name.linger"));
					if (inventory.insertSuper(SLOT_OUTPUT_LINGER, linger, false).isEmpty()) {
						inventory.getStackInSlot(SLOT_INPUT_BOTTLE).shrink(1);
						breath--;
						potion.get(0).duration -= partition * 20;
						if (potion.get(0).getDuration() <= 0)
							potion.remove(0);
						markDirty();
						shouldWork = false;
					}
				}
				else if (gunpowder > 0) {
					ItemStack splash = new ItemStack(Items.SPLASH_POTION);
					EffectInstance app = new EffectInstance(potion.get(0));
					if (!app.getPotion().isInstant())
						app.duration = partition * 20;
					PotionUtils.appendEffects(splash, Arrays.asList(app));
					splash.getOrCreateTag().putInt("CustomPotionColor", PotionUtils.getPotionColorFromEffectList(PotionUtils.getEffectsFromStack(splash)));
					splash.setDisplayName(new TranslationTextComponent("potioncapsule.misc.custom_potion_display_name.splash"));
					if (inventory.insertSuper(SLOT_OUTPUT_SPLASH, splash, false).isEmpty()) {
						inventory.getStackInSlot(SLOT_INPUT_BOTTLE).shrink(1);
						gunpowder--;
						potion.get(0).duration -= partition * 20;
						if (potion.get(0).getDuration() <= 0)
							potion.remove(0);
						markDirty();
						shouldWork = false;
					}
				}
				else {
					ItemStack _potion = new ItemStack(Items.POTION);
					EffectInstance app = new EffectInstance(potion.get(0));
					if (!app.getPotion().isInstant())
						app.duration = partition * 20;
					PotionUtils.appendEffects(_potion, Arrays.asList(app));
					_potion.getOrCreateTag().putInt("CustomPotionColor", PotionUtils.getPotionColorFromEffectList(PotionUtils.getEffectsFromStack(_potion)));
					_potion.setDisplayName(new TranslationTextComponent("potioncapsule.misc.custom_potion_display_name"));
					if (inventory.insertSuper(SLOT_OUTPUT_NORMAL, _potion, false).isEmpty()) {
						inventory.getStackInSlot(SLOT_INPUT_BOTTLE).shrink(1);
						potion.get(0).duration -= partition * 20;
						if (potion.get(0).getDuration() <= 0)
							potion.remove(0);
						markDirty();
						shouldWork = false;
					}
				}
			}
		}
		
		// In the end, sync the ITileCustomSync if needed
		sync();
	}
	
	private void sync() {
		PacketHandler.sendTile(this);
	}
	
	public boolean checkAndSetRecipe(){
		Tuple<ItemStack, Integer> brew = getBrew();
		if (!brew.getA().isEmpty()) {
			if (PotionUtils.getEffectsFromStack(brew.getA()).isEmpty())
				return false;
			for (int i = 0 ; i<6 ; i++) {
				memory.setStackInSlot(i, inventory.getStackInSlot(i + 1).copy());
			}
			memMode = true;
			output = brew.getA().copy();
			brewsteps = brew.getB();
			markDirty();
			return true;
		}
		return false;
	}
	
	private Tuple<ItemStack, Integer> getBrew() {
		int recipeSize = 6;
		for (int i = 1 ; i<=6 ; i++) {
			if (inventory.getStackInSlot(i).isEmpty()) {
				recipeSize = i - 1;
				break;
			}
		}
		if (recipeSize == 0)
			return new Tuple<ItemStack, Integer>(ItemStack.EMPTY, 0);
		ItemStack output = PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), Potions.WATER);
		for (int i = 0 ; i<recipeSize ; i++) {
			output = BrewingRecipeRegistry.getOutput(output, inventory.getStackInSlot(i + 1));
			if (output.isEmpty())
				return new Tuple<ItemStack, Integer>(ItemStack.EMPTY, 0);
		}
		return new Tuple<ItemStack, Integer>(output, recipeSize);
	}
	
	public boolean clearMemory(){
		memMode = false;
		for (int i = 0 ; i<6 ; i++) {
			memory.setStackInSlot(i, ItemStack.EMPTY);
		}
		brewing = false;
		brewtime = 0;
		markDirty();
		return true;
	}
	
	protected boolean checkOutputSpace() {
		return mergeOutput(false).size() <= 5;
	}
	
	protected List<EffectInstance> mergeOutput(boolean act) {
		List<EffectInstance> effects = new ArrayList<>();
		for (EffectInstance effect: PotionUtils.getEffectsFromStack(output)) {
			EffectInstance copy;
			if (effect.getPotion() == Effects.NIGHT_VISION) {
				copy = new EffectInstance(EffectNightVisionNF.INSTANCE, effect.getDuration(), effect.getAmplifier(), effect.isAmbient(), effect.isShowIcon());
			}
			else {
				copy = new EffectInstance(effect);
			}
			if (!effect.getPotion().isInstant())
				copy.duration *= 3;
			effects.add(copy);
			if (effect.getPotion().isInstant()) {
				effects.add(copy);
				effects.add(copy);
			}
		}
		List<EffectInstance> simulate = potion.stream().map(effect -> { return new EffectInstance(effect); }).collect(Collectors.toList());
		for (EffectInstance outer: effects) {
			for (EffectInstance effect: simulate) {
				if (effect.getPotion().isInstant())
					continue;
				if (effect.getPotion() == outer.getPotion() && effect.getAmplifier() == outer.getAmplifier() && effect.getDuration() < 3600 * 20) {
					int left = outer.getDuration() - (3600 * 20 - effect.getDuration());
					effect.duration = MathHelper.clamp(effect.getDuration() + outer.getDuration(), 0, 3600 * 20);
					outer.duration = left;
				}
			}
			if (outer.getDuration() > 0)
				simulate.add(outer);
		}
		if (act)
			potion = simulate;
		return simulate;
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
		brewing = false;
		brewtime = 0;
		markDirty();
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
	
	public boolean isBrewing() {
		return brewing;
	}
	
	public int getBrewtime() {
		return brewtime;
	}
	
	public void clearPotion() {
		potion.clear();
		markDirty();
	}
	
	public int getPartition() {
		return partition;
	}
	
	public void updatePartition(int amount) {
		partition  = MathHelper.clamp(partition + amount, CommonConfigs.capsule_capacity.get() / 20, 3600);
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
		compound.putInt("Time", brewtime);
		compound.put("Output", output.write(new CompoundNBT()));
		compound.putInt("Step", brewsteps);
		compound.putBoolean("Brewing", brewing);
		compound.putInt("Partition", partition);
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
		brewtime = compound.getInt("Time");
		output = ItemStack.read(compound.getCompound("Output"));
		brewsteps = compound.getInt("Step");
		brewing = compound.getBoolean("Brewing");
		partition = compound.getInt("Partition");
	}
	
	public CompoundNBT getCustomUpdate() {
		CompoundNBT compound = ITileCustomSync.super.getCustomUpdate();
		compound.put("Inventory", CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.writeNBT(inventory, null));
		compound.put("Memory", CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.writeNBT(memory, null));
		compound.putInt("WaterAmount", water.getFluidAmount());
		compound.put("Potion", writePotion(new CompoundNBT()));
		compound.putInt("Fuel", fuel);
		compound.putByte("Gunpowder", (byte) gunpowder);
		compound.putByte("Breath", (byte) breath);
		compound.putInt("Catalyst", catalyst);
		compound.putBoolean("MemMode", memMode);
		compound.putInt("Time", brewtime);
		compound.putBoolean("Brewing", brewing);
		return compound;
	}
	
	public void readCustomUpdate(CompoundNBT compound) {
		CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.readNBT(inventory, null, compound.get("Inventory"));
		CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.readNBT(memory, null, compound.get("Memory"));
		water.setFluid(new FluidStack(Fluids.WATER, compound.getInt("WaterAmount")));
		potion = readPotion((CompoundNBT) compound.get("Potion"));
		fuel = compound.getInt("Fuel");
		gunpowder = compound.getByte("Gunpowder");
		breath = compound.getByte("Breath");
		catalyst = compound.getInt("Catalyst");
		memMode = compound.getBoolean("MemMode");
		brewtime = compound.getInt("Time");
		brewing = compound.getBoolean("Brewing");
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
	
	static class Inventory extends ItemStackHandler {
		public Inventory() { super(); }
		public Inventory(int size) { super(size); }
		public Inventory(NonNullList<ItemStack> stacks) { super(stacks); }

		public ItemStack insertSuper(int slot, @Nonnull ItemStack stack, boolean simulate) {
			return super.insertItem(slot, stack, simulate);
		}
	}

}
