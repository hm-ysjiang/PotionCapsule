package hmysjiang.potioncapsule.recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.google.gson.JsonObject;

import hmysjiang.potioncapsule.items.ItemSpecialCapsule;
import hmysjiang.potioncapsule.items.ItemSpecialCapsule.EnumSpecialType;
import hmysjiang.potioncapsule.utils.Defaults;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class RecipeSpecialRepair extends SpecialRecipe {
	public static final IRecipeSerializer<?> SERIALIZER = new Serializer().setRegistryName(Defaults.modPrefix.apply("special_repair"));
	
	public final EnumSpecialType type;
	private ItemStack toRepair = ItemStack.EMPTY;
	private ItemStack repaired = ItemStack.EMPTY;
	private List<ItemStack> repairers;
	
	public RecipeSpecialRepair(ResourceLocation loc, String typeName, List<ItemStack> repairers) {
		this(loc, EnumSpecialType.valueOf(typeName), repairers);
	}
	
	public RecipeSpecialRepair(ResourceLocation loc, EnumSpecialType type, List<ItemStack> repairers) {
		super(loc);
		this.type = type;
		this.toRepair = new ItemStack(ItemSpecialCapsule.getCapsuleInstance(type));
		this.repaired = new ItemStack(ItemSpecialCapsule.getCapsuleInstance(type));
		this.repairers = repairers;
		toRepair.setDamage(toRepair.getMaxDamage());
		repaired.setDamage(repaired.getMaxDamage() - repaired.getMaxDamage() / 4);
	}
	
	public List<ItemStack> getRepairers() {
		return repairers;
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
			EnumSpecialType type = EnumSpecialType.valueOf(JSONUtils.getString(json, "specialtype"));
			return new RecipeSpecialRepair(recipeId, type, ItemTags.getCollection().get(type.getRepairTag()).getAllElements().stream().map(item -> new ItemStack(item)).collect(Collectors.toList()));
		}

		@Override
		public RecipeSpecialRepair read(ResourceLocation recipeId, PacketBuffer buffer) {
			EnumSpecialType type = EnumSpecialType.valueOf(buffer.readString());
			List<ItemStack> repairers = new ArrayList<>();
			int size = buffer.readVarInt();
			for (int i = 0 ; i < size; i++) {
				repairers.add(buffer.readItemStack());
			}
			return new RecipeSpecialRepair(recipeId, type, repairers);
		}

		@Override
		public void write(PacketBuffer buffer, RecipeSpecialRepair recipe) {
			List<ItemStack> repairers = recipe.repairers;
			buffer.writeString(recipe.type.name());
			buffer.writeVarInt(repairers.size());
			for (ItemStack stack: repairers)
				buffer.writeItemStack(stack);
		}
		
	}
	
}
