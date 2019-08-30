package hmysjiang.potioncapsule.recipe;

import com.google.gson.JsonObject;

import hmysjiang.potioncapsule.PotionCapsule;
import hmysjiang.potioncapsule.blocks.gelatin_former.InventoryGelatinFormer;
import hmysjiang.potioncapsule.init.ModItems;
import hmysjiang.potioncapsule.utils.Defaults;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class RecipeGelatinFormer implements IRecipe<InventoryGelatinFormer> {
	
	public static final IRecipeType<RecipeGelatinFormer> TYPE = new IRecipeType<RecipeGelatinFormer>() {
		@Override public String toString() { return Defaults.modPrefix.apply("recipetype_gelatin_form").toString(); }
	};
	public static final IRecipeSerializer<?> SERIALIZER = new Serializer().setRegistryName(Defaults.modPrefix.apply("gelatin_form"));
	
	private final ResourceLocation id;
	private final int gelatin_count;
	private final int tick_cost;
	private final ItemStack catalyst;
	private final ItemStack result;
	
	public RecipeGelatinFormer(ResourceLocation idIn, int gelatin_countIn, int tick_costIn, ItemStack catalystIn, ItemStack resultIn) {
		PotionCapsule.Logger.info("Loading recipe of type gelatin_form, id: " + idIn);
		id = idIn;
		gelatin_count = gelatin_countIn;
		tick_cost = tick_costIn;
		catalyst = catalystIn;
		result = resultIn;
	}

	@Override
	public boolean matches(InventoryGelatinFormer inv, World worldIn) {
		return inv.getStackInSlot(0).getCount() >= gelatin_count && 
				inv.getStackInSlot(1).isItemEqual(catalyst) && inv.getStackInSlot(1).getCount() >= catalyst.getCount();
	}

	@Override
	public ItemStack getCraftingResult(InventoryGelatinFormer inv) {
		inv.getStackInSlot(0).shrink(gelatin_count);
		inv.getStackInSlot(1).shrink(catalyst.getCount());
		return result.copy();
	}

	@Override
	public ItemStack getRecipeOutput() {
		// Why TF are capsules getting a non-null nbt tag on client side ?
		ItemStack out = result.copy();
		if (out.getItem() == ModItems.CAPSULE)
			out.getOrCreateTag();
		return out;
	}

	@Override
	public boolean canFit(int width, int height) {
		return true;
	}

	@Override
	public ResourceLocation getId() {
		return id;
	}

	public int getGelatinCount() {
		return gelatin_count;
	}

	public int getTickCost() {
		return tick_cost;
	}

	public ItemStack getCatalyst() {
		return catalyst;
	}

	public ItemStack getResult() {
		return result;
	}

	@Override
	public IRecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}

	@Override
	public IRecipeType<?> getType() {
		return TYPE;
	}
	
	@Override
	public String toString() {
		String ret = "Recipe GelatinForm" + "\n" + 
					 "--------" + "\n" + 
					 "gelatin: " + gelatin_count + "\n" + 
					 "input: " + catalyst + "\n" + 
					 "resule: " + result + "\n" + 
					 "--------";
		return ret;
	}
	
	private static class Serializer extends net.minecraftforge.registries.ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<RecipeGelatinFormer> {

		@Override
		public RecipeGelatinFormer read(ResourceLocation recipeId, JsonObject json) {
			int gelatin_count = JSONUtils.getInt(json, "gelatin_count", 1);
			int tick_cost = JSONUtils.getInt(json, "tick_cost", 100);
			ItemStack input = ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(json, "catalyst"));
			ItemStack result = ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(json, "result"));
			return new RecipeGelatinFormer(recipeId, gelatin_count, tick_cost, input, result);
		}

		@Override
		public RecipeGelatinFormer read(ResourceLocation recipeId, PacketBuffer buffer) {
			int gelatin_count = buffer.readInt();
			int tick_cost = buffer.readInt();
			ItemStack input = buffer.readItemStack();
			ItemStack result = buffer.readItemStack();
			return new RecipeGelatinFormer(recipeId, gelatin_count, tick_cost, input, result);
		}

		@Override
		public void write(PacketBuffer buffer, RecipeGelatinFormer recipe) {
			buffer.writeInt(recipe.gelatin_count);
			buffer.writeInt(recipe.tick_cost);
			buffer.writeItemStack(recipe.catalyst);
			buffer.writeItemStack(recipe.result);
		}
		
	}

}
