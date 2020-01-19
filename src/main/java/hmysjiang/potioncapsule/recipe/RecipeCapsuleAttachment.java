package hmysjiang.potioncapsule.recipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.JsonObject;

import hmysjiang.potioncapsule.PotionCapsule;
import hmysjiang.potioncapsule.Reference;
import hmysjiang.potioncapsule.configs.CommonConfigs;
import hmysjiang.potioncapsule.init.ModItems;
import hmysjiang.potioncapsule.items.ItemCapsule;
import hmysjiang.potioncapsule.items.ItemCapsule.EnumCapsuleType;
import hmysjiang.potioncapsule.potions.effects.EffectNightVisionNF;
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
	public boolean doColor;
	
	public RecipeCapsuleAttachment(ResourceLocation location, ItemStack potionIn, ItemStack retriveIn, boolean doColor) {
		super(location);
		//PotionCapsule.Logger.info("Registering recipe " + location);
		potion_item = potionIn.copy();
		retrive_item = retriveIn.copy();
		this.doColor = doColor;
	}

	@Override
	public boolean matches(CraftingInventory inv, World worldIn) {
		boolean cap = false, potion = false;
		int capPos = 0, amps = 0;
		EnumCapsuleType type = EnumCapsuleType.NORMAL;
		
		for (int i = 0 ; i<inv.getSizeInventory() ; i++) {
			if (ItemCapsule.isItemCapsule(inv.getStackInSlot(i))) {
				if (cap)
					return false;
				if (PotionUtils.getEffectsFromStack(inv.getStackInSlot(i)).size() > 0)
					return false;
				cap = true;
				type = ItemCapsule.getCapsuleType(inv.getStackInSlot(i).getItem());
				capPos = i;
			}
		}
		
		FOR_POTION:
		for (int i = 0 ; i<inv.getSizeInventory() ; i++) {
			if (i == capPos)
				continue;
			if (inv.getStackInSlot(i).isItemEqual(potion_item)) {
				if (potion)
					return false;
				for (EffectInstance ins: PotionUtils.getEffectsFromStack(inv.getStackInSlot(i))) {
					if (ins.getPotion().isInstant()) {
						if (type == EnumCapsuleType.INSTANT) {
							potion = true;
							continue FOR_POTION;
						}
					}
					else if (type == EnumCapsuleType.NORMAL && ins.duration >= CommonConfigs.capsule_capacity.get()) {
						potion = true;
						continue FOR_POTION;
					}
				}
				return false;
			}
			else if (!inv.getStackInSlot(i).isEmpty() && !(type == EnumCapsuleType.INSTANT && inv.getStackInSlot(i).getItem() == ModItems.CATALYST)) {
				return false;
			}
		}
		for (int i = 0 ; i<inv.getSizeInventory() ; i++) {
			if (inv.getStackInSlot(i).getItem() == ModItems.CATALYST)
				if (++amps > inv.getStackInSlot(capPos).getCount() - 1)
					return false;
		}
		return cap && potion;
	}

	@Override
	public ItemStack getCraftingResult(CraftingInventory inv) {
		List<EffectInstance> effects = new ArrayList<>();
		
		EnumCapsuleType type = EnumCapsuleType.NORMAL;
		int capsuleCount = 0;
		for (int i = 0 ; i<inv.getSizeInventory() ; i++) {
			if (ItemCapsule.isItemCapsule(inv.getStackInSlot(i))) {
				type = ItemCapsule.getCapsuleType(inv.getStackInSlot(i).getItem());
				capsuleCount = inv.getStackInSlot(i).getCount();
				break;
			}
		}
		
		EffectInstance effect2Apply = null;
		int potionPos;
		for (potionPos = 0 ; potionPos<inv.getSizeInventory() ; potionPos++) {
			if (inv.getStackInSlot(potionPos).isItemEqual(potion_item)) {
				effects = PotionUtils.getEffectsFromStack(inv.getStackInSlot(potionPos));
				break;
			}
		}
		for (int i = 0 ; i<effects.size() ; i++) {
			if (type == EnumCapsuleType.INSTANT) {
				if (effects.get(i).getPotion().isInstant()) {
					effect2Apply = new EffectInstance(effects.get(i));
				}
			}
			else if (effects.get(i).duration >= CommonConfigs.capsule_capacity.get()) {
				effect2Apply = new EffectInstance((CommonConfigs.misc_replaceNvWithNvnf.get() && effects.get(i).getPotion() == Effects.NIGHT_VISION) ?
													new EffectInstance(EffectNightVisionNF.INSTANCE) : effects.get(i));
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
		
		int instantAmp = 0;
		if (type == EnumCapsuleType.NORMAL)
			effect2Apply.duration = CommonConfigs.capsule_capacity.get();
		else if (CommonConfigs.recipe_instantCatalystAllowed.get() > 0){
			for (int i = 0 ; i<inv.getSizeInventory() ; i++) {
				if (inv.getStackInSlot(i).getItem() == ModItems.CATALYST) {
					if (++instantAmp >= CommonConfigs.recipe_instantCatalystAllowed.get())
						break;
				}
			}
		}
		if (instantAmp > capsuleCount - 1)
			instantAmp = capsuleCount - 1;
		return PotionUtils.appendEffects(ItemCapsule.getDefaultInstance(type, 1 + instantAmp), Arrays.asList(effect2Apply));
	}
	
	@Override
	public NonNullList<ItemStack> getRemainingItems(CraftingInventory inv) {
		int potionPos = 0, amps = 0, capsulePos = 0;
		boolean tax = false;
		EnumCapsuleType type = EnumCapsuleType.NORMAL;
		for (int i  = 0 ; i<inv.getSizeInventory() ; i++) {
			if (inv.getStackInSlot(i).isItemEqual(potion_item)) 
				potionPos = i;
			else if (inv.getStackInSlot(i).getItem() == ModItems.CATALYST) {
				amps++;
			}
			else if (ItemCapsule.isItemCapsule(inv.getStackInSlot(i))) {
				capsulePos = i;
				type = ItemCapsule.getCapsuleType(inv.getStackInSlot(i).getItem());
			}
		}
		List<EffectInstance> effects = new ArrayList<>(), remainEffects = new ArrayList<>();
		for (EffectInstance effect: PotionUtils.getEffectsFromStack(inv.getStackInSlot(potionPos)))
			effects.add(new EffectInstance(effect));
		NonNullList<ItemStack> remain = super.getRemainingItems(inv);
		for (int i = 0 ; i<effects.size() ; i++) {
			if (effects.get(i).duration < CommonConfigs.capsule_capacity.get()) {
				if (effects.get(i).getPotion().isInstant()) {
					if (type == EnumCapsuleType.INSTANT && !tax) {
						tax = true;
					}
					else 
						remainEffects.add(effects.get(i));
				}
				else if (!CommonConfigs.recipe_removeExcessDuration.get())
					remainEffects.add(effects.get(i));
			}
			else if (effects.get(i).duration == CommonConfigs.capsule_capacity.get()) {
				if (!tax) {
					if (effects.get(i).getPotion().isInstant()) {
						if (type == EnumCapsuleType.INSTANT) {
							tax = true;
						}
					}
					else if (type == EnumCapsuleType.NORMAL) {
						tax = true;
					}
				}
			}
			else {
				if (!tax) {
					if (effects.get(i).getPotion().isInstant()) {
						if (type == EnumCapsuleType.INSTANT) {
							tax = true;
							continue;
						}
					}
					else if (type == EnumCapsuleType.NORMAL) {
						tax = true;
						effects.get(i).duration -= CommonConfigs.capsule_capacity.get();
					}
				}
				if (effects.get(i).duration < CommonConfigs.capsule_capacity.get()) {
					if (!CommonConfigs.recipe_removeExcessDuration.get())
						remainEffects.add(effects.get(i));
				}
				else
					remainEffects.add(effects.get(i));
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
		inv.getStackInSlot(capsulePos).shrink(amps);
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
