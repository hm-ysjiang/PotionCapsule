package hmysjiang.potioncapsule.recipe;

import hmysjiang.potioncapsule.Reference;
import hmysjiang.potioncapsule.configs.CommonConfigs;
import hmysjiang.potioncapsule.init.ModItems;
import hmysjiang.potioncapsule.utils.Defaults;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class RecipeWartDust extends SpecialRecipe {
	
	public static final IRecipeSerializer<?> SERIALIZER = new SpecialRecipeSerializer<>(RecipeWartDust::new).setRegistryName(Defaults.modPrefix.apply("wart_dust"));
	private static final ResourceLocation ALLOWED = Defaults.modPrefix.apply("wart_dust");
	
	public RecipeWartDust(ResourceLocation idIn) {
		super(idIn);
		if (!idIn.equals(ALLOWED))
			throw new IllegalArgumentException("The recipe type potioncapsule:wart_dust should not be used except for item_wart_dust itself!");
	}

	@Override
	public boolean matches(CraftingInventory inv, World worldIn) {
		if (!CommonConfigs.recipe_enableWartDust.get())
			return false;
		boolean wart = false;
		for (int i = 0 ; i<inv.getSizeInventory() ; i++) {
			if (!inv.getStackInSlot(i).isEmpty()) {
				if (wart)
					return false;
				if (inv.getStackInSlot(i).getItem() == Items.NETHER_WART)
					wart = true;
				else
					return false;
			}
		}
		return wart;
	}

	@Override
	public ItemStack getCraftingResult(CraftingInventory inv) {
		getRemainingItems(inv);
		return getRecipeOutput();
	}
	
	@Override
	public ItemStack getRecipeOutput() {
		return new ItemStack(ModItems.WART_DUST, 9);
	}

	@Override
	public boolean canFit(int width, int height) {
		return width * height > 0;
	}

	@Override
	public IRecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}
	
	@Override
	public String getGroup() {
		return Reference.CAPSULE_CRAFTING_GROUP;
	}
	
}
