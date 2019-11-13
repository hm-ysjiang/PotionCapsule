package hmysjiang.potioncapsule.recipe;

import hmysjiang.potioncapsule.Reference;
import hmysjiang.potioncapsule.init.ModItems;
import hmysjiang.potioncapsule.items.ItemCapsule;
import hmysjiang.potioncapsule.utils.Defaults;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class RecipeCapsuleCreativeAttach extends SpecialRecipe {
	public static final IRecipeSerializer<?> SERIALIZER = new SpecialRecipeSerializer<>(RecipeCapsuleCreativeAttach::new).setRegistryName(Defaults.modPrefix.apply("capsule_creative"));
	
	public RecipeCapsuleCreativeAttach(ResourceLocation location) {
		super(location);
	}

	@Override
	public boolean matches(CraftingInventory inv, World worldIn) {
		int caps = 0, cc = 0;
		for (int i = 0 ; i<inv.getSizeInventory() ; i++) {
			if (ItemCapsule.isItemCapsule(inv.getStackInSlot(i))) {
				if (PotionUtils.getEffectsFromStack(inv.getStackInSlot(i)).size() == 0)
					return false;
				caps++;
			}
			else if (inv.getStackInSlot(i).getItem() == ModItems.CREATIVE_CATALYST) {
				cc++;
			}
		}
		return cc == 1 && caps == 1;
	}

	@Override
	public ItemStack getCraftingResult(CraftingInventory inv) {
		ItemStack capsule = ItemStack.EMPTY;
		for (int i = 0 ; i<inv.getSizeInventory() ; i++) {
			if (ItemCapsule.isItemCapsule(inv.getStackInSlot(i))) {
				capsule = inv.getStackInSlot(i).copy();
			}
		}
		capsule.getOrCreateTag().putBoolean("CapsuleCreative", true);
		return capsule;
	}
	
	@Override
	public String getGroup() {
		return Reference.CAPSULE_CRAFTING_GROUP;
	}

	@Override
	public IRecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}

	@Override
	public boolean canFit(int width, int height) {
		return width * height >= 2;
	}
	
	@Override
	public boolean isDynamic() {
		return true;
	}
	
}
