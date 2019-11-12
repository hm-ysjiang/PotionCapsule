package hmysjiang.potioncapsule.recipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.JsonObject;

import hmysjiang.potioncapsule.PotionCapsule;
import hmysjiang.potioncapsule.Reference;
import hmysjiang.potioncapsule.configs.CommonConfigs;
import hmysjiang.potioncapsule.effects.EffectNightVisionNF;
import hmysjiang.potioncapsule.init.ModItems;
import hmysjiang.potioncapsule.utils.Defaults;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class RecipeCapsuleAttachment extends SpecialRecipe {
	public static final IRecipeSerializer<?> SERIALIZER = new Serializer().setRegistryName(Defaults.modPrefix.apply("capsule_attachment"));

	private final ItemStack potion_item;
	private final ItemStack retrive_item;
	public boolean active = true;
	public boolean doColor;
	
	public RecipeCapsuleAttachment(ResourceLocation location, ItemStack potionIn, ItemStack retriveIn, boolean doColor) {
		super(location);
		PotionCapsule.Logger.info("Registering recipe " + location);
		potion_item = potionIn.copy();
		retrive_item = retriveIn.copy();
		this.doColor = doColor;
	}

	@Override
	public boolean matches(CraftingInventory inv, World worldIn) {
		if (!active)
			return false;
		boolean cap = false, potion = false;
		
		FOR_SREACH:
		for (int i = 0 ; i<inv.getSizeInventory() ; i++) {
			if (inv.getStackInSlot(i).getItem() == ModItems.CAPSULE) {
				if (cap)
					return false;
				if (PotionUtils.getEffectsFromStack(inv.getStackInSlot(i)).size() > 0)
					return false;
				cap = true;
			}
			else if (inv.getStackInSlot(i).isItemEqual(potion_item)) {
				if (potion)
					return false;
				for (EffectInstance ins: PotionUtils.getEffectsFromStack(inv.getStackInSlot(i))) {
					if (ins.duration >= CommonConfigs.capsule_capacity.get()) {
						potion = true;
						continue FOR_SREACH;
					}
				}
				return false;
			}
		}
		return cap && potion;
	}

	@Override
	public ItemStack getCraftingResult(CraftingInventory inv) {
		List<EffectInstance> effects = new ArrayList<>();
		EffectInstance effect2Apply = null;
		int potionPos;
		for (potionPos = 0 ; potionPos<inv.getSizeInventory() ; potionPos++) {
			if (inv.getStackInSlot(potionPos).isItemEqual(potion_item)) {
				effects = PotionUtils.getEffectsFromStack(inv.getStackInSlot(potionPos));
				break;
			}
		}
		for (int i = 0 ; i<effects.size() ; i++) {
			if (effects.get(i).duration >= CommonConfigs.capsule_capacity.get()) {
				effect2Apply = new EffectInstance((CommonConfigs.misc_replaceNvWithNvnf.get() && effects.get(i).getPotion() == Effects.NIGHT_VISION) ? new EffectInstance(EffectNightVisionNF.INSTANCE) : effects.get(i));
				break;
			}
		}
		
		/***
		 * This should not happen
		 */
		if (effect2Apply == null) {
			try {
				PotionCapsule.Logger.error("Error occured while attaching effect on capsule, please report this to the issue tracker");
				PotionCapsule.Logger.error("Potion pos: " + potionPos + ", Content: " + inv.getStackInSlot(potionPos));
				for (int i = 0 ; i<inv.getSizeInventory() ; i++) {
					PotionCapsule.Logger.error("	Pos " + i + ", Size " + i + ", StackContent: " + inv.getStackInSlot(i));
				}
			} catch(IndexOutOfBoundsException exp) { Arrays.asList(exp.getStackTrace()).forEach(PotionCapsule.Logger::error); }
			return ItemStack.EMPTY;
		}
		
		effect2Apply.duration = CommonConfigs.capsule_capacity.get();
		return PotionUtils.appendEffects(new ItemStack(ModItems.CAPSULE), Arrays.asList(effect2Apply));
	}
	
	@Override
	public NonNullList<ItemStack> getRemainingItems(CraftingInventory inv) {
		int potionPos;
		boolean tax = false;
		for (potionPos = 0 ; potionPos<inv.getSizeInventory() ; potionPos++) 
			if (inv.getStackInSlot(potionPos).isItemEqual(potion_item)) 
				break;
		List<EffectInstance> effects = new ArrayList<>(), remainEffects = new ArrayList<>();
		for (EffectInstance effect: PotionUtils.getEffectsFromStack(inv.getStackInSlot(potionPos)))
			effects.add(new EffectInstance(effect));
		NonNullList<ItemStack> remain = super.getRemainingItems(inv);
		for (int i = 0 ; i<effects.size() ; i++) {
			if (effects.get(i).duration < CommonConfigs.capsule_capacity.get()) {
				if (!CommonConfigs.recipe_removeExcessDuration.get())
					remainEffects.add(effects.get(i));
			}
			else if (effects.get(i).duration == CommonConfigs.capsule_capacity.get()) {
				if (!tax) {
					tax = true;
					effects.get(i).duration -= CommonConfigs.capsule_capacity.get();
				}
			}
			else {
				if (!tax) {
					tax = true;
					effects.get(i).duration -= CommonConfigs.capsule_capacity.get();
				}
				if (effects.get(i).duration < CommonConfigs.capsule_capacity.get()) {
					if (!CommonConfigs.recipe_removeExcessDuration.get())
						remainEffects.add(effects.get(i));
				}
				else
					remainEffects.add(effects.get(i));
				
				// Might add a config for remove useless effect
			}
		}
		if (remainEffects.size() > 0) {
			ItemStack newstack = PotionUtils.appendEffects(potion_item.copy(), remainEffects);
			newstack.setDisplayName(new TranslationTextComponent("potioncapsule.misc.left_potion_display_name"));
			if (doColor) {
				newstack.getOrCreateTag().putInt("CustomPotionColor", PotionUtils.getPotionColorFromEffectList(remainEffects));
			}
			remain.set(potionPos, newstack);
		}
		else if (!retrive_item.isEmpty()) {
			remain.set(potionPos, retrive_item.copy());
		}
		return remain;
	}
	
	@Override
	public boolean isDynamic() {
		return true;
	}
	
	@Override
	public String getGroup() {
		return Reference.CAPSULE_CRAFTING_GROUP;
	}

	@Override
	public IRecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}
	
	private static class Serializer extends net.minecraftforge.registries.ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<RecipeCapsuleAttachment> {

		@Override
		public RecipeCapsuleAttachment read(ResourceLocation recipeId, JsonObject json) {
			JsonObject retriveJson = JSONUtils.getJsonObject(json, "retriveitem");
			ItemStack toRetrive = ItemStack.EMPTY;
			if (JSONUtils.getBoolean(retriveJson, "hasRetriveItem", false))
				toRetrive = ShapedRecipe.deserializeItem(retriveJson);
			return new RecipeCapsuleAttachment(recipeId, ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(json, "potionitem")), 
														 toRetrive,
														 JSONUtils.getBoolean(json, "rerender_color", false));
		}

		@Override
		public RecipeCapsuleAttachment read(ResourceLocation recipeId, PacketBuffer buffer) {
			return new RecipeCapsuleAttachment(recipeId, buffer.readItemStack(), buffer.readItemStack(), buffer.readBoolean());
		}

		@Override
		public void write(PacketBuffer buffer, RecipeCapsuleAttachment recipe) {
			buffer.writeItemStack(recipe.potion_item);
			buffer.writeItemStack(recipe.retrive_item);
			buffer.writeBoolean(recipe.doColor);
		}
		
	}

	@Override
	public boolean canFit(int width, int height) {
		return width * height >= 2;
	}
	
}
