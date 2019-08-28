package hmysjiang.potioncapsule.recipe;

import com.google.gson.JsonObject;

import hmysjiang.potioncapsule.PotionCapsule;
import hmysjiang.potioncapsule.blocks.gelatin.InventoryGelatinExtractor;
import hmysjiang.potioncapsule.init.ModItems;
import hmysjiang.potioncapsule.utils.Defaults;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class RecipeGelatinExtractor implements IRecipe<InventoryGelatinExtractor> {
	
	public static final IRecipeType<RecipeGelatinExtractor> TYPE = new IRecipeType<RecipeGelatinExtractor>() {
		@Override public String toString() { return Defaults.modPrefix.apply("recipetype_gelatin_ex").toString(); }
	};
	public static final IRecipeSerializer<?> SERIALIZER = new Serializer().setRegistryName(Defaults.modPrefix.apply("gelatin_ex"));
	
	private ResourceLocation id;
	private final Item input;
	private final String inputName;
	private int inputUsed;
	private int outputCount;
	private int tickCost;
	public boolean active = true;
	
	@SuppressWarnings("deprecation")
	public RecipeGelatinExtractor(ResourceLocation idIn, String inputIn, int inputUsedIn, int outputCountIn, int tickCostIn, boolean setActive) {
		PotionCapsule.Logger.info("Loading recipe of type gelatin_ex, id: " + idIn);
		id = idIn;
		
		input = Registry.ITEM.getValue(new ResourceLocation(inputIn)).orElseGet(() -> {
			PotionCapsule.Logger.error("Recipe " + idIn + " of type gelatin_ex has an invalid input " + inputIn + ",this recipe will be disabled");
			active = false;
			return null;
		});
		if (inputUsedIn == -1 || outputCountIn == -1 || tickCostIn == -1 || !setActive)
			active = false;
		
		inputName = inputIn;
		inputUsed = inputUsedIn;
		outputCount = outputCountIn;
		tickCost = tickCostIn;
	}

	@Override
	public boolean matches(InventoryGelatinExtractor inv, World worldIn) {
		if (!active)
			return false;
		return (inv.getStackInSlot(0).getItem() == input && inv.getStackInSlot(0).getCount() >= inputUsed);
	}

	@Override
	public ItemStack getCraftingResult(InventoryGelatinExtractor inv) {
		inv.getStackInSlot(0).shrink(inputUsed);
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

	public Item getInput() {
		return input;
	}

	public int getInputUsed() {
		return inputUsed;
	}

	public int getOutputCount() {
		return outputCount;
	}

	public int getTickCost() {
		return tickCost;
	}

	public String getInputName() {
		return inputName;
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
		return inputName + " " + inputUsed + " " + outputCount + " " + tickCost;
	}
	
	private static class Serializer extends net.minecraftforge.registries.ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<RecipeGelatinExtractor> {

		@Override
		public RecipeGelatinExtractor read(ResourceLocation recipeId, JsonObject json) {
			String itemName = JSONUtils.getString(json, "item", "");
			int inputCount, outputCount, tick;
			inputCount = JSONUtils.getInt(json, "input_count", -1);
			outputCount = JSONUtils.getInt(json, "output_count", -1);
			tick = JSONUtils.getInt(json, "ticks", -1);
			return new RecipeGelatinExtractor(recipeId, itemName, inputCount, outputCount, tick, true);
		}

		@Override
		public RecipeGelatinExtractor read(ResourceLocation recipeId, PacketBuffer buffer) {
			String name = buffer.readString();
			int input, output, tick;
			input = buffer.readInt();
			output = buffer.readInt();
			tick = buffer.readInt();
			boolean active = buffer.readBoolean();
			return new RecipeGelatinExtractor(recipeId, name, input, output, tick, active);
		}

		@Override
		public void write(PacketBuffer buffer, RecipeGelatinExtractor recipe) {
			buffer.writeString(recipe.getInputName());
			buffer.writeInt(recipe.getInputUsed());
			buffer.writeInt(recipe.getOutputCount());
			buffer.writeInt(recipe.getTickCost());
			buffer.writeBoolean(recipe.active);
		}
		
	}

}
