package hmysjiang.potioncapsule.recipe;

import com.google.gson.JsonObject;

import hmysjiang.potioncapsule.PotionCapsule;
import hmysjiang.potioncapsule.Reference;
import hmysjiang.potioncapsule.blocks.gelatin_extractor.InventoryGelatinExtractor;
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

public class RecipeGelatinExtractor implements IRecipe<InventoryGelatinExtractor> {
	
	public static final IRecipeType<RecipeGelatinExtractor> TYPE = new IRecipeType<RecipeGelatinExtractor>() {
		@Override public String toString() { return Defaults.modPrefix.apply("recipetype_gelatin_ex").toString(); }
	};
	public static final IRecipeSerializer<?> SERIALIZER = new Serializer().setRegistryName(Defaults.modPrefix.apply("gelatin_ex"));
	
	private ResourceLocation id;
	private ItemStack input;
	private int outputCount;
	private int tickCost;
	public boolean active = true;
	
	public RecipeGelatinExtractor(ResourceLocation idIn, ItemStack inputIn, int outputCountIn, int tickCostIn, boolean setActive) {
		PotionCapsule.Logger.info("Loading recipe of type gelatin_ex, id: " + idIn);
		id = idIn;
		input = inputIn;
		outputCount = outputCountIn;
		tickCost = tickCostIn;
	}

	@Override
	public boolean matches(InventoryGelatinExtractor inv, World worldIn) {
		if (!active)
			return false;
		return (inv.getStackInSlot(0).isItemEqual(input) && inv.getStackInSlot(0).getCount() >= input.getCount());
	}

	@Override
	public ItemStack getCraftingResult(InventoryGelatinExtractor inv) {
		inv.getStackInSlot(0).shrink(input.getCount());
		return new ItemStack(ModItems.GELATIN_POWDER, outputCount);
	}

	@Override
	public ItemStack getRecipeOutput() {
		return new ItemStack(ModItems.GELATIN_POWDER, outputCount);
	}

	@Override
	public boolean canFit(int width, int height) {
		return true;
	}

	@Override
	public ResourceLocation getId() {
		return id;
	}

	public ItemStack getInputCopy() {
		return input.copy();
	}

	public int getInputUsed() {
		return input.getCount();
	}

	public int getOutputCount() {
		return outputCount;
	}

	public int getTickCost() {
		return tickCost;
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
	public String getGroup() {
		return Reference.CAPSULE_CRAFTING_GROUP;
	}
	
	private static class Serializer extends net.minecraftforge.registries.ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<RecipeGelatinExtractor> {

		@Override
		public RecipeGelatinExtractor read(ResourceLocation recipeId, JsonObject json) {
			int outputCount, tick;
			outputCount = JSONUtils.getInt(json, "output_count", -1);
			tick = JSONUtils.getInt(json, "tick_cost", -1);
			return new RecipeGelatinExtractor(recipeId, ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(json, "item")), outputCount, tick, true);
		}

		@Override
		public RecipeGelatinExtractor read(ResourceLocation recipeId, PacketBuffer buffer) {
			return new RecipeGelatinExtractor(recipeId, buffer.readItemStack(), buffer.readInt(), buffer.readInt(), buffer.readBoolean());
		}

		@Override
		public void write(PacketBuffer buffer, RecipeGelatinExtractor recipe) {
			buffer.writeItemStack(recipe.getInputCopy());
			buffer.writeInt(recipe.getOutputCount());
			buffer.writeInt(recipe.getTickCost());
			buffer.writeBoolean(recipe.active);
		}
		
	}

}
