package hmysjiang.potioncapsule.recipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import hmysjiang.potioncapsule.PotionCapsule;
import hmysjiang.potioncapsule.Reference;
import hmysjiang.potioncapsule.init.ModItems;
import hmysjiang.potioncapsule.utils.Defaults;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class RecipeCapsuleAttachment extends SpecialRecipe {
	public static final IRecipeSerializer<?> SERIALIZER = new SpecialRecipeSerializer<>(RecipeCapsuleAttachment::new).setRegistryName(Defaults.modPrefix.apply("capsule_attachment"));
	private static final ItemStack RESULT = PotionUtils.addPotionToItemStack(new ItemStack(ModItems.CAPSULE), Potions.AWKWARD);
	
	public RecipeCapsuleAttachment(ResourceLocation location) {
		super(location);
		PotionCapsule.Logger.info("Registering recipe " + location);
	}

	@Override
	public boolean matches(CraftingInventory inv, World worldIn) {
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
			else if (inv.getStackInSlot(i).getItem() == Items.POTION) {
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
			if (inv.getStackInSlot(potionPos).getItem() == Items.POTION) {
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
			if (inv.getStackInSlot(potionPos).getItem() == Items.POTION) 
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
				
//				Add a config option here
//				remainEffects.add(effects.get(i));
			}
			else {
				if (!tax) {
					tax = true;
					effects.get(i).duration -= 100;
				}
				remainEffects.add(effects.get(i));
			}
		}
		if (remainEffects.size() > 0)
			remain.set(potionPos, PotionUtils.appendEffects(new ItemStack(Items.POTION), remainEffects));
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
	
}
