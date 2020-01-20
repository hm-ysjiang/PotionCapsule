package hmysjiang.potioncapsule.recipe;

import java.util.Set;
import java.util.TreeSet;

import hmysjiang.potioncapsule.Reference;
import hmysjiang.potioncapsule.configs.CommonConfigs;
import hmysjiang.potioncapsule.items.ItemCapsule;
import hmysjiang.potioncapsule.items.ItemCapsule.EnumCapsuleType;
import hmysjiang.potioncapsule.potions.effects.EffectNightVisionNF;
import hmysjiang.potioncapsule.utils.Defaults;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class RecipeCapsuleCombinationOrClear extends SpecialRecipe {
	public static final IRecipeSerializer<?> SERIALIZER = new SpecialRecipeSerializer<>(RecipeCapsuleCombinationOrClear::new).setRegistryName(Defaults.modPrefix.apply("capsule_combination"));
	
	public RecipeCapsuleCombinationOrClear(ResourceLocation location) {
		super(location);
	}

	@Override
	public boolean matches(CraftingInventory inv, World worldIn) {
		boolean match = false;
		int capsules = 0;
		EnumCapsuleType domain = null;
		for (int i = 0 ; i<inv.getSizeInventory() ; i++) {
			if (!inv.getStackInSlot(i).isEmpty()) {
				if (ItemCapsule.isItemCapsule(inv.getStackInSlot(i))) {
					if (!PotionUtils.getEffectsFromStack(inv.getStackInSlot(i)).isEmpty()) {
						EnumCapsuleType type = ItemCapsule.getCapsuleType(inv.getStackInSlot(i).getItem());
						if (domain == null)
							domain = type;
						else if (domain != type) 
							return false;
						match = true;
						capsules ++;
					}
				}
				else
					return false;
			}
		}
		return match && (CommonConfigs.recipe_allowCapsuleCombine.get() || capsules == 1);
	}

	@Override
	public ItemStack getCraftingResult(CraftingInventory inv) {
		int capsule_count = 0;
		Set<EffectInstance> effects = new TreeSet<>(ItemCapsule.EFFECT_CMP);
		EnumCapsuleType type = null;
		for (int i = 0 ; i<inv.getSizeInventory() ; i++) {
			if (!PotionUtils.getEffectsFromStack(inv.getStackInSlot(i)).isEmpty()) {
				if (type == null)
					type = ItemCapsule.getCapsuleType(inv.getStackInSlot(i).getItem());
				FOR_ITER:
				for (EffectInstance effect: PotionUtils.getEffectsFromStack(inv.getStackInSlot(i))) {
					if (CommonConfigs.misc_replaceNvWithNvnf.get() && effect.getPotion() == Effects.NIGHT_VISION) {
						effect = new EffectInstance(EffectNightVisionNF.INSTANCE, effect.getDuration(), effect.getAmplifier(), effect.isAmbient(), effect.doesShowParticles(), effect.isShowIcon());
					}
					for (EffectInstance stored: effects) {
						if (effect.getPotion().equals(stored.getPotion())) {
							stored.combine(effect);
							continue FOR_ITER;
						}
					}
					effects.add(effect);
				}
				capsule_count++;
			}
		}
		if (capsule_count == 1)
			return ItemCapsule.getDefaultInstance(type);
		return PotionUtils.appendEffects(ItemCapsule.getDefaultInstance(type), effects);
	}
	
	@Override
	public String getGroup() {
		return Reference.CAPSULE_CRAFTING_GROUP;
	}

	@Override
	public IRecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}

	@Override
	public boolean canFit(int width, int height) {
		return width * height >= 1;
	}
	
	@Override
	public boolean isDynamic() {
		return true;
	}
	
}
