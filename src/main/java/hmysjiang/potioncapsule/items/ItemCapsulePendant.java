package hmysjiang.potioncapsule.items;

import hmysjiang.potioncapsule.PotionCapsule;
import hmysjiang.potioncapsule.Reference;
import hmysjiang.potioncapsule.container.ContainerPendant;
import hmysjiang.potioncapsule.init.ModItems;
import hmysjiang.potioncapsule.utils.Defaults;
import hmysjiang.potioncapsule.utils.helper.InventoryHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

@EventBusSubscriber(modid = Reference.MOD_ID)
public class ItemCapsulePendant extends Item {
	private static INamedContainerProvider containerProv = null;

	public ItemCapsulePendant() {
		super(Defaults.itemProp.get());
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		if (!worldIn.isRemote) {
			if (containerProv == null)
				containerProv = new Provider();
			NetworkHooks.openGui((ServerPlayerEntity) playerIn, containerProv, buf->{
				buf.writeBoolean(handIn == Hand.MAIN_HAND);
			});
		}
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}
	
	@Override
	public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if (!worldIn.isRemote) {
			if (entityIn instanceof PlayerEntity) {
				onTick(stack, (PlayerEntity) entityIn, worldIn);
			}
		}
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT nbt) {
		return new ICapabilitySerializable<INBT>() {
			private final IItemHandler handler = new ItemStackHandler(8) {
				@Override public boolean isItemValid(int slot, ItemStack stack) { return stack.getItem() instanceof ItemCapsule; };
				@Override public int getSlotLimit(int slot) { return 128; }
				@Override protected int getStackLimit(int slot, ItemStack stack) { return 128; }
			};
			
			@Override
			public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
				return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.orEmpty(cap, LazyOptional.of(() -> handler));
			}

			@Override
			public INBT serializeNBT() {
				return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.writeNBT(handler, null);
			}

			@Override
			public void deserializeNBT(INBT nbt) {
				CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.readNBT(handler, null, nbt);
			}
		};
	}
	
	@Override
	public CompoundNBT getShareTag(ItemStack stack) {
		CompoundNBT compound = stack.getOrCreateTag();
		stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(handler -> {
			compound.put("cap_sync", CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.writeNBT(handler, null));
		});
		return compound;
	}
	
	@Override
	public void readShareTag(ItemStack stack, CompoundNBT nbt) {
		PotionCapsule.Logger.info(nbt);
		super.readShareTag(stack, nbt);
		stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(handler -> {
			CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.readNBT(handler, null, nbt.get("cap_sync"));
		});
	}

	public static enum CapsuleSlots {
		ATTACK(0),
		DAMAGED(1),
		FIRE(2),
		WATER(3),
		SPRINT(4),
		FALLING(5),
		NIGHT(6),
		KEYBIND(7);
		final int index;
		private CapsuleSlots(int idx) {
			index = idx;
		}
		public int getIndex() {
			return index;
		}
	}
	
	/***
	 * Event Handlers part
	 */
	
	@SubscribeEvent
	public static void onPlayerAbout2Attacc(AttackEntityEvent event) {
		PlayerEntity player = event.getEntityPlayer();
		ItemStack pendant = InventoryHelper.findStackFromInventory(player.inventory, ModItems.PENDANT.getDefaultInstance());
		if (!pendant.isEmpty()) {
			pendant.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(handler -> {
				((ItemStackHandler) handler).setStackInSlot(0, ((ItemCapsule) ModItems.CAPSULE).onItemUseFinishRegardsActiveEffects(handler.getStackInSlot(CapsuleSlots.ATTACK.index), player.world, player));
			});
		}
	}
	
	@SubscribeEvent
	public static void onPlayerGetDamaged(LivingHurtEvent event) {
		PlayerEntity player = event.getEntityLiving() instanceof PlayerEntity ? (PlayerEntity) event.getEntityLiving() : null;
		if (player == null || event.getSource().isFireDamage())
			return;
		ItemStack pendant = InventoryHelper.findStackFromInventory(player.inventory, ModItems.PENDANT.getDefaultInstance());
		if (!pendant.isEmpty()) {
			pendant.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(handler -> {
				((ItemStackHandler) handler).setStackInSlot(1, ((ItemCapsule) ModItems.CAPSULE).onItemUseFinishRegardsActiveEffects(handler.getStackInSlot(CapsuleSlots.DAMAGED.index), player.world, player));
			});
		}
	}

	private void onTick(ItemStack pendant, PlayerEntity player, World world) {
		// FIRE
		if (player.isBurning() || player.isInLava())
			pendant.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(handler -> {
				((ItemStackHandler) handler).setStackInSlot(2,
						((ItemCapsule) ModItems.CAPSULE).onItemUseFinishRegardsActiveEffects(
								handler.getStackInSlot(CapsuleSlots.FIRE.index), world, player));
			});
	
		// WATER
		if (player.isInWater())
			pendant.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(handler -> {
				((ItemStackHandler) handler).setStackInSlot(3,
						((ItemCapsule) ModItems.CAPSULE).onItemUseFinishRegardsActiveEffects(
								handler.getStackInSlot(CapsuleSlots.WATER.index), world, player));
			});
		
		// SPRINT
		if (player.isSprinting())
			pendant.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(handler -> {
				((ItemStackHandler) handler).setStackInSlot(4,
						((ItemCapsule) ModItems.CAPSULE).onItemUseFinishRegardsActiveEffects(
								handler.getStackInSlot(CapsuleSlots.SPRINT.index), world, player));
			});
		
		// FALLING
		if (player.fallDistance > 3.0F)
			pendant.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(handler -> {
				((ItemStackHandler) handler).setStackInSlot(5,
						((ItemCapsule) ModItems.CAPSULE).onItemUseFinishRegardsActiveEffects(
								handler.getStackInSlot(CapsuleSlots.FALLING.index), world, player));
			});
		
		// NIGHT
		if (!world.isDaytime())
			pendant.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(handler -> {
				((ItemStackHandler) handler).setStackInSlot(6,
						((ItemCapsule) ModItems.CAPSULE).onItemUseFinishRegardsActiveEffects(
								handler.getStackInSlot(CapsuleSlots.NIGHT.index), world, player));
			});
		
	}
	
	public static void onKeyBindPressed(PlayerEntity player) {
		if (player.world.isRemote)
			return;
		ItemStack pendant = InventoryHelper.findStackFromInventory(player.inventory, ModItems.PENDANT.getDefaultInstance());
		if (!pendant.isEmpty()) {
			pendant.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(handler -> {
				((ItemStackHandler) handler).setStackInSlot(7, ((ItemCapsule) ModItems.CAPSULE).onItemUseFinishRegardsActiveEffects(handler.getStackInSlot(CapsuleSlots.KEYBIND.index), player.world, player));
			});
		}
	}
	
	private static class Provider implements INamedContainerProvider {
		public static final ITextComponent displayName = ModItems.PENDANT.getDisplayName(ModItems.PENDANT.getDefaultInstance());

		@Override
		public Container createMenu(int winId, PlayerInventory inv, PlayerEntity player) {
			return new ContainerPendant(winId, inv, player.getActiveHand());
		}
		
		@Override
		public ITextComponent getDisplayName() {
			return displayName;
		}
		
	}

}
