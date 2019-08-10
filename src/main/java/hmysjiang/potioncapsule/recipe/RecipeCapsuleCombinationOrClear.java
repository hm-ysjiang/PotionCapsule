package hmysjiang.potioncapsule.recipe;

import java.util.Set;
import java.util.TreeSet;

import hmysjiang.potioncapsule.Reference;
import hmysjiang.potioncapsule.init.ModItems;
import hmysjiang.potioncapsule.utils.Defaults;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraft.potion.EffectInstance;
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
		for (int i = 0 ; i<inv.getSizeInventory() ; i++) {
			if (!inv.getStackInSlot(i).isEmpty()) {
				if (inv.getStackInSlot(i).getItem() == ModItems.CAPSULE) {
					if (!PotionUtils.getEffectsFromStack(inv.getStackInSlot(i)).isEmpty())
						match = true;
				}
				else
					return false;
			}
		}
		return match;
	}

	@Override
	public ItemStack getCraftingResult(CraftingInventory inv) {
		int capsule_count = 0;
		Set<EffectInstance> effects = new TreeSet<>();
		for (int i = 0 ; i<inv.getSizeInventory() ; i++) {
			if (!PotionUtils.getEffectsFromStack(inv.getStackInSlot(i)).isEmpty()) {
				FOR_ITER:
				for (EffectInstance effect: PotionUtils.getEffectsFromStack(inv.getStackInSlot(i))) {
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
			return ModItems.CAPSULE.getDefaultInstance();
		return PotionUtils.appendEffects(ModItems.CAPSULE.getDefaultInstance(), effects);
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
