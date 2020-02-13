package hmysjiang.potioncapsule.recipe;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import hmysjiang.potioncapsule.blocks.special_capsule_repairer.InventorySpecialCapsuleRepairer;
import hmysjiang.potioncapsule.items.ItemSpecialCapsule;
import hmysjiang.potioncapsule.items.ItemSpecialCapsule.EnumSpecialType;
import hmysjiang.potioncapsule.utils.Defaults;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class RecipeSpecialCapsuleRepairer implements ISpecialRepairerRecipe {
	
	public static final IRecipeSerializer<?> SERIALIZER = new Serializer().setRegistryName(Defaults.modPrefix.apply("special_capsule_repair"));
	
	private final EnumSpecialType type;
	private final Map<Ingredient, Integer> materials;
	private final ResourceLocation id;
	public RecipeSpecialCapsuleRepairer(ResourceLocation loc, EnumSpecialType type, Map<Ingredient, Integer> materials) {
		id = loc;
		this.type = type;
		this.materials = materials;
		
		// May be better if recipes are buffered
		// BlockSpecialCapsuleRepairer.addRecipe(type, this);
	}

	@Override
	public boolean matches(InventorySpecialCapsuleRepairer inv, World worldIn) {
		if (!inv.getStackInSlot(0).isEmpty()) {
			ItemStack capsule = inv.getStackInSlot(0);
			if (capsule.getItem() instanceof ItemSpecialCapsule) {
				if (capsule.getDamage() == 0)
					return false;
				EnumSpecialType capsuleType = ((ItemSpecialCapsule) capsule.getItem()).type;
				if (capsuleType == type) {
					for (Ingredient material: materials.keySet()) {
						if (material.test(inv.getStackInSlot(1)))
							return true;
					}
				}
			}
		}
		return false;
	}

	@Override
	public ItemStack getCraftingResult(InventorySpecialCapsuleRepairer inv) {
		ItemStack capsule = inv.getStackInSlot(0).copy();
		for (Entry<Ingredient, Integer> entry: materials.entrySet()) {
			if (entry.getKey().test(inv.getStackInSlot(1))) {
				int amount = entry.getValue() * inv.getStackInSlot(1).getCount();
				capsule.setDamage(MathHelper.clamp(capsule.getDamage() - amount, 0, capsule.getDamage()));
				return capsule;
			}
		}
		return ItemStack.EMPTY;
	}
	
	@Override
	public NonNullList<ItemStack> getRemainingItems(InventorySpecialCapsuleRepairer inv) {
		NonNullList<ItemStack> remain = NonNullList.withSize(2, ItemStack.EMPTY);
		ItemStack capsule = inv.getStackInSlot(0);
		int dmg = capsule.getDamage();
		for (Entry<Ingredient, Integer> entry: materials.entrySet()) {
			if (entry.getKey().test(inv.getStackInSlot(1))) {
				ItemStack material = inv.getStackInSlot(1).copy();
				int repair = entry.getValue();
				int use = MathHelper.ceil(((float) dmg) / ((float) repair));
				
				material.setCount(MathHelper.clamp(material.getCount() - use, 0, material.getCount()));
				remain.set(1, material);
			}
		}
		return remain;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return new ItemStack(ItemSpecialCapsule.getCapsuleInstance(type));
	}

	@Override
	public ResourceLocation getId() {
		return id;
	}

	@Override
	public boolean canFit(int width, int height) {
		return true;
	}

	@Override
	public IRecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}
	
	@Override
	public IRecipeType<?> getType() {
		return ISpecialRepairerRecipe.TYPE;
	}
	
	public EnumSpecialType getCapsuleType() {
		return type;
	}
	
	public Map<Ingredient, Integer> getMaterials() {
		return materials;
	}

	public static class Serializer extends net.minecraftforge.registries.ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<RecipeSpecialCapsuleRepairer> {

		@Override
		public RecipeSpecialCapsuleRepairer read(ResourceLocation recipeId, JsonObject json) {
			EnumSpecialType type = EnumSpecialType.valueOf(JSONUtils.getString(json, "specialtype"));
			Map<Ingredient, Integer> materials = new HashMap<Ingredient, Integer>();
			for (JsonElement js: JSONUtils.getJsonArray(json, "items")) {
				materials.put(Ingredient.deserialize(js), MathHelper.clamp(JSONUtils.getInt((JsonObject) js, "amount", 1), 1, 262144));
			}
			return new RecipeSpecialCapsuleRepairer(recipeId, type, materials);
		}

		@Override
		public RecipeSpecialCapsuleRepairer read(ResourceLocation recipeId, PacketBuffer buffer) {
			EnumSpecialType type = EnumSpecialType.valueOf(buffer.readString());
			Map<Ingredient, Integer> materials = new HashMap<Ingredient, Integer>();
			int size = buffer.readVarInt();
			
			for (int i = 0 ; i < size; i++) {
				materials.put(Ingredient.read(buffer), buffer.readVarInt());
			}
			return new RecipeSpecialCapsuleRepairer(recipeId, type, materials);
		}

		@Override
		public void write(PacketBuffer buffer, RecipeSpecialCapsuleRepairer recipe) {
			buffer.writeString(recipe.type.name());
			buffer.writeVarInt(recipe.materials.size());
			
			for (Map.Entry<Ingredient, Integer> entry: recipe.materials.entrySet()) {
				entry.getKey().write(buffer);
				buffer.writeVarInt(entry.getValue());
			}
		}
		
	}
	
}
