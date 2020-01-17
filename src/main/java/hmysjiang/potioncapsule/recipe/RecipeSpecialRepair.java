package hmysjiang.potioncapsule.recipe;

import com.google.gson.JsonObject;

import hmysjiang.potioncapsule.items.ItemSpecialCapsule;
import hmysjiang.potioncapsule.items.ItemSpecialCapsule.EnumSpecialType;
import hmysjiang.potioncapsule.utils.Defaults;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class RecipeSpecialRepair extends SpecialRecipe {
	public static final IRecipeSerializer<?> SERIALIZER = new Serializer().setRegistryName(Defaults.modPrefix.apply("special_repair"));
	
	public final EnumSpecialType type;
	private ItemStack toRepair = ItemStack.EMPTY;
	private ItemStack repaired = ItemStack.EMPTY;
	private ResourceLocation repairs;
	
	public RecipeSpecialRepair(ResourceLocation loc, String typeName) {
		this(loc, EnumSpecialType.valueOf(typeName));
	}
	
	public RecipeSpecialRepair(ResourceLocation loc, EnumSpecialType type) {
		super(loc);
		this.type = type;
		this.toRepair = new ItemStack(ItemSpecialCapsule.getCapsuleInstance(type));
		this.repaired = new ItemStack(ItemSpecialCapsule.getCapsuleInstance(type));
		this.repairs = type.getRepairTag();
		toRepair.setDamage(toRepair.getMaxDamage());
		repaired.setDamage(repaired.getMaxDamage() - repaired.getMaxDamage() / 4);
	}
	
	public ResourceLocation getRepairs() {
		return repairs;
	}
	
	public ItemStack getToRepair() {
		return toRepair;
	}
	
	public ItemStack getRepaired() {
		return repaired;
	}

	@Override
	public boolean matches(CraftingInventory inv, World worldIn) {
		return false;
	}

	@Override
	public ItemStack getCraftingResult(CraftingInventory inv) {
		return getRepaired();
	}

	@Override
	public boolean canFit(int width, int height) {
		return false;
	}

	@Override
	public IRecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}
	
	public static class Serializer extends net.minecraftforge.registries.ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<RecipeSpecialRepair> {

		@Override
		public RecipeSpecialRepair read(ResourceLocation recipeId, JsonObject json) {
			return new RecipeSpecialRepair(recipeId, JSONUtils.getString(json, "specialtype"));
		}

		@Override
		public RecipeSpecialRepair read(ResourceLocation recipeId, PacketBuffer buffer) {
			return new RecipeSpecialRepair(recipeId, EnumSpecialType.valueOf(buffer.readString()));
		}

		@Override
		public void write(PacketBuffer buffer, RecipeSpecialRepair recipe) {
			buffer.writeString(recipe.type.name());
		}
		
	}
	
}
