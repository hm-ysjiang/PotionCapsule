package hmysjiang.potioncapsule.container;

import java.util.Optional;

import hmysjiang.potioncapsule.blocks.special_capsule_repairer.InventorySpecialCapsuleRepairer;
import hmysjiang.potioncapsule.items.ItemSpecialCapsule;
import hmysjiang.potioncapsule.recipe.ISpecialRepairerRecipe;
import hmysjiang.potioncapsule.utils.Defaults;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerSpecialCapsuleRepairer extends BaseContainer {

	@SuppressWarnings("unchecked")
	public static final ContainerType<ContainerSpecialCapsuleRepairer> TYPE = (ContainerType<ContainerSpecialCapsuleRepairer>) IForgeContainerType
			.create((winId, inv, data) -> (new ContainerSpecialCapsuleRepairer(winId, inv, data.readBlockPos())))
			.setRegistryName(Defaults.modPrefix.apply("container_special_cap_repair"));
	
	private PlayerEntity player;
	private World world;
	private BlockPos pos;
	private ItemStackHandler inv, output;
	private ISpecialRepairerRecipe buf;
	private boolean match;
	
	public ContainerSpecialCapsuleRepairer(int id, PlayerInventory playerInv, BlockPos posIn) {
		super(TYPE, id, 3);
		
		match = false;
		player = playerInv.player;
		world = playerInv.player.world;
		pos = posIn;
		inv = new ItemStackHandler(2);
		output = new ItemStackHandler(1) {
			@Override public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) { return stack; }
		};
		
		addSlot(new SlotItemHandler(inv, 0, 27, 38) {
			@Override
			public boolean isItemValid(ItemStack stack) {
				return super.isItemValid(stack) && stack.getItem() instanceof ItemSpecialCapsule;
			}
			@Override
			public void onSlotChanged() {
				super.onSlotChanged();
				ContainerSpecialCapsuleRepairer.this.updateRecipe();
			}
		});
		addSlot(new SlotItemHandler(inv, 1, 88, 38) {
			@Override
			public void onSlotChanged() {
				super.onSlotChanged();
				ContainerSpecialCapsuleRepairer.this.updateRecipe();
			}
		});
		addSlot(new SlotItemHandler(output, 0, 134, 38) {
			@Override
			public boolean isItemValid(ItemStack stack) {
				return super.isItemValid(stack) && stack.getItem() instanceof ItemSpecialCapsule;
			}
			@Override
			public void onSlotChanged() {
				super.onSlotChanged();
				if (match) {
					NonNullList<ItemStack> remain = ContainerSpecialCapsuleRepairer.this.buf.getRemainingItems(new InventorySpecialCapsuleRepairer(inv));
					ContainerSpecialCapsuleRepairer.this.inv.setStackInSlot(0, remain.get(0));
					ContainerSpecialCapsuleRepairer.this.inv.setStackInSlot(1, remain.get(1));
					ContainerSpecialCapsuleRepairer.this.updateRecipe();
					ContainerSpecialCapsuleRepairer.this.world.playSound(player, pos, SoundEvents.BLOCK_ANVIL_USE, SoundCategory.BLOCKS, 1.0F, world.rand.nextFloat() * 0.1F + 0.9F);
				}
			}
		});
		
		int xPos = 8;
		int yPos = 76;
		
		for (int y = 0; y < 3; ++y) {
			for (int x = 0; x < 9; ++x) {
				this.addSlot(new Slot(playerInv, x + y * 9 + 9, xPos + x * 18, yPos + y * 18));
			}
		}
		
		for (int x = 0; x < 9; ++x) {
			this.addSlot(new Slot(playerInv, x, xPos + x * 18, yPos + 58));
		}
	}
	
	private void updateRecipe(){
		Optional<ISpecialRepairerRecipe> opt = world.getRecipeManager().getRecipe(ISpecialRepairerRecipe.TYPE, new InventorySpecialCapsuleRepairer(inv), world);
		match = opt.isPresent();
		opt.ifPresent(recipe -> {
			buf = recipe;
			output.setStackInSlot(0, recipe.getCraftingResult(new InventorySpecialCapsuleRepairer(inv)));
		});
	}
	
	@Override
	public void onContainerClosed(PlayerEntity playerIn) {
		super.onContainerClosed(playerIn);
		for (int i = 0 ; i<inv.getSlots() ; i++)
			if (!inv.getStackInSlot(i).isEmpty())
				playerIn.inventory.placeItemBackInInventory(playerIn.world, inv.getStackInSlot(i));
	}
	
	public boolean match() {
		return match;
	}

}
