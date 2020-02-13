package hmysjiang.potioncapsule.recipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonObject;

import hmysjiang.potioncapsule.PotionCapsule;
import hmysjiang.potioncapsule.blocks.special_capsule_repairer.InventorySpecialCapsuleRepairer;
import hmysjiang.potioncapsule.items.ItemSpecialCapsule;
import hmysjiang.potioncapsule.items.ItemSpecialCapsule.EnumSpecialType;
import hmysjiang.potioncapsule.utils.Defaults;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class RecipeFoodRestoration implements ISpecialRepairerRecipe {
	
	public static final IRecipeSerializer<?> SERIALIZER = new Serializer().setRegistryName(Defaults.modPrefix.apply("food_restoration"));

	private Map<Integer, List<Item>> val2items;
	private Map<Item, Integer> item2val;
	private final ResourceLocation id;
	
	protected RecipeFoodRestoration(ResourceLocation idIn) {
		id = idIn;
		val2items = new HashMap<>();
		item2val = new HashMap<>();
	}

	@Override
	public boolean matches(InventorySpecialCapsuleRepairer inv, World worldIn) {
		if (inv.getStackInSlot(0).isEmpty() || inv.getStackInSlot(1).isEmpty())
			return false;
		if (inv.getStackInSlot(0).getDamage() == 0)
			return false;
		if (!inv.getStackInSlot(1).getItem().isFood())
			return false;
		return true;
	}

	@Override
	public ItemStack getCraftingResult(InventorySpecialCapsuleRepairer inv) {
		ItemStack cake = inv.getStackInSlot(0).copy();
		ItemStack food = inv.getStackInSlot(1);
		int val = item2val.getOrDefault(food.getItem(), 0);
		if (val > 0) {
			int amount = val * food.getCount();
			cake.setDamage(MathHelper.clamp(cake.getDamage() - amount, 0, cake.getDamage()));
			return cake;
		}
		return ItemStack.EMPTY;
	}
	
	@Override
	public NonNullList<ItemStack> getRemainingItems(InventorySpecialCapsuleRepairer inv) {
		NonNullList<ItemStack> remain = NonNullList.withSize(2, ItemStack.EMPTY);
		ItemStack capsule = inv.getStackInSlot(0);
		ItemStack food = inv.getStackInSlot(1);
		int dmg = capsule.getDamage();
		int val = item2val.getOrDefault(food.getItem(), 0);
		if (val > 0) {
			int use = MathHelper.ceil(((float) dmg) / ((float) val));
			food.setCount(MathHelper.clamp(food.getCount() - use, 0, food.getCount()));
			remain.set(1, food);
		}
		return remain;
	}
	
	public Map<Integer, List<Item>> getVal2Items() {
		return val2items;
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
	public ItemStack getRecipeOutput() {
		return new ItemStack(ItemSpecialCapsule.getCapsuleInstance(EnumSpecialType.LIE_OF_CAKE));
	}

	@Override
	public ResourceLocation getId() {
		return id;
	}

	@Override
	public IRecipeType<?> getType() {
		return ISpecialRepairerRecipe.TYPE;
	}
	
	@SuppressWarnings("deprecation")
	private RecipeFoodRestoration query() {
		PotionCapsule.Logger.info("Start querying foods...");
		for (Item item: Registry.ITEM) {
			if (item.isFood()) {
				Food food = item.getFood();
				
				int val = food.getHealing() + ((int) ((float) food.getHealing() * food.getSaturation() * 2.0F));
				if (val <= 0)
					continue;
				
				item2val.put(item, val);
				
				if (!val2items.containsKey(val))
					val2items.put(val, new ArrayList<>());
				val2items.get(val).add(item);
			}
		}
		PotionCapsule.Logger.info("Food querying complete");
		return this;
	}
	
	static class Serializer extends net.minecraftforge.registries.ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<RecipeFoodRestoration> {

		@Override
		public RecipeFoodRestoration read(ResourceLocation recipeId, JsonObject json) {
			return new RecipeFoodRestoration(recipeId).query();
		}

		@Override
		public RecipeFoodRestoration read(ResourceLocation recipeId, PacketBuffer buffer) {
			return new RecipeFoodRestoration(recipeId).query();
		}

		@Override
		public void write(PacketBuffer buffer, RecipeFoodRestoration recipe) {}
		
	}

}
