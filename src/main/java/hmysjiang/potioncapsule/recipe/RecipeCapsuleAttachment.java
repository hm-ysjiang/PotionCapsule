package hmysjiang.potioncapsule.recipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

import com.google.gson.JsonObject;

import hmysjiang.potioncapsule.PotionCapsule;
import hmysjiang.potioncapsule.Reference;
import hmysjiang.potioncapsule.init.ModItems;
import hmysjiang.potioncapsule.utils.Defaults;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class RecipeCapsuleAttachment extends SpecialRecipe {
	private static final ItemStack RESULT = PotionUtils.addPotionToItemStack(new ItemStack(ModItems.CAPSULE), Potions.AWKWARD);
	public static final IRecipeSerializer<?> SERIALIZER = new Serializer().setRegistryName(Defaults.modPrefix.apply("capsule_attachment"));
//	public static final IRecipeSerializer<?> SERIALIZER = new SpecialRecipeSerializer<>(RecipeCapsuleAttachment::new).setRegistryName(Defaults.modPrefix.apply("capsule_attachment"));

	@Nullable
	private final Item potion_item;
	@Nullable 
	private final Item retrive_item;
	public boolean active = true;
	public boolean doColor;
	
	@SuppressWarnings("deprecation")
	public RecipeCapsuleAttachment(ResourceLocation location, String potionItemName, String retriveItemName, boolean doColor) {
		super(location);
		PotionCapsule.Logger.info("Registering recipe " + location);
		potion_item = Registry.ITEM.getValue(new ResourceLocation(potionItemName)).orElseGet(() -> {
			PotionCapsule.Logger.error("	Recipe " + location + " has an invalid potionItem " + potionItemName + ",this recipe will be disabled");
			active = false;
			return null;
		});
		retrive_item = Registry.ITEM.getValue(new ResourceLocation(retriveItemName)).orElseGet(() -> {
			PotionCapsule.Logger.error("Recipe " + location + " has an invalid or empty retriveItem " + retriveItemName + ",this recipe will clear the slot of the potionItem that has been used");
			return null;
		});
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
			else if (inv.getStackInSlot(i).getItem().equals(potion_item)) {
				if (potion)
					return false;
				for (EffectInstance ins: PotionUtils.getEffectsFromStack(inv.getStackInSlot(i))) {
					if (ins.duration >= 100) {
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
			if (inv.getStackInSlot(potionPos).getItem().equals(potion_item)) {
				effects = PotionUtils.getEffectsFromStack(inv.getStackInSlot(potionPos));
				break;
			}
		}
		for (int i = 0 ; i<effects.size() ; i++) {
			if (effects.get(i).duration >= 100) {
//				PotionCapsule.Logger.info(i + ", effect: " + effects.get(i).getEffectName() + ", dur: " + effects.get(i).duration);
				effect2Apply = new EffectInstance(effects.get(i));
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
			return RESULT.copy();
		}
		
		effect2Apply.duration = 100;
		return PotionUtils.appendEffects(new ItemStack(ModItems.CAPSULE), Arrays.asList(effect2Apply));
	}
	
	@Override
	public NonNullList<ItemStack> getRemainingItems(CraftingInventory inv) {
		int potionPos;
		boolean tax = false;
		for (potionPos = 0 ; potionPos<inv.getSizeInventory() ; potionPos++) 
			if (inv.getStackInSlot(potionPos).getItem().equals(potion_item)) 
				break;
		List<EffectInstance> effects = new ArrayList<>(), remainEffects = new ArrayList<>();
		for (EffectInstance effect: PotionUtils.getEffectsFromStack(inv.getStackInSlot(potionPos)))
			effects.add(new EffectInstance(effect));
		NonNullList<ItemStack> remain = super.getRemainingItems(inv);
		for (int i = 0 ; i<effects.size() ; i++) {
			if (effects.get(i).duration < 100) {
				remainEffects.add(effects.get(i));
			}
			else if (effects.get(i).duration == 100) {
				if (!tax) {
					tax = true;
					effects.get(i).duration -= 100;
				}
			}
			else {
				if (!tax) {
					tax = true;
					effects.get(i).duration -= 100;
				}
				remainEffects.add(effects.get(i));
				
				// Might add a config for remove useless effect
			}
		}
		if (remainEffects.size() > 0) {
			ItemStack newstack = PotionUtils.appendEffects(new ItemStack(potion_item), remainEffects);
			newstack.setDisplayName(new TranslationTextComponent("potioncapsule.misc.left_potion_display_name"));
			if (doColor) {
				newstack.getOrCreateTag().putInt("CustomPotionColor", PotionUtils.getPotionColorFromEffectList(remainEffects));
			}
			remain.set(potionPos, newstack);
		}
		else if (retrive_item != null) {
			remain.set(potionPos, new ItemStack(retrive_item));
		}
		return remain;
	}
	
	@Override
	public ItemStack getRecipeOutput() {
		return RESULT;
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
			String potionItemName = JSONUtils.getString(json, "potionitem", "");
			if (potionItemName == "") 
				PotionCapsule.Logger.error("Found an invalid recipe while deserializing " + recipeId + ",missing field: potionitem");
			
			String retriveItemName = JSONUtils.getString(json, "retriveitem", "");
			boolean doColor = JSONUtils.getBoolean(json, "rerender_color", false);
			return new RecipeCapsuleAttachment(recipeId, potionItemName, retriveItemName, doColor);
		}

		@Override
		public RecipeCapsuleAttachment read(ResourceLocation recipeId, PacketBuffer buffer) {
			String potionItem = buffer.readString();
			if (potionItem.equals("")) 
				PotionCapsule.Logger.error("Found an invalid recipe while deserializing " + recipeId + " from the packet");

			String retriveItemName = buffer.readString();
			boolean doColor = buffer.readBoolean();
			return new RecipeCapsuleAttachment(recipeId, potionItem, retriveItemName, doColor);
		}

		@Override
		public void write(PacketBuffer buffer, RecipeCapsuleAttachment recipe) {
			buffer.writeString(recipe.potion_item.getRegistryName().toString());
			buffer.writeString(recipe.retrive_item.getRegistryName().toString());
			buffer.writeBoolean(recipe.doColor);
		}
		
	}
	
}
