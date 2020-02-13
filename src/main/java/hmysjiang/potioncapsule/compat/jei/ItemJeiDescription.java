package hmysjiang.potioncapsule.compat.jei;

import java.util.Arrays;
import java.util.stream.Collectors;

import hmysjiang.potioncapsule.PotionCapsule;
import hmysjiang.potioncapsule.compat.curio.ICurioProxy;
import hmysjiang.potioncapsule.configs.CommonConfigs;
import hmysjiang.potioncapsule.init.ModBlocks;
import hmysjiang.potioncapsule.init.ModItems;
import hmysjiang.potioncapsule.items.ItemSpecialCapsule;
import hmysjiang.potioncapsule.items.ItemSpecialCapsule.EnumSpecialType;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtils;

public class ItemJeiDescription {
	
	public static void registerInfo(IRecipeRegistration registration) {
		registration.addIngredientInfo(new ItemStack(ModItems.APPLE_JELLY), VanillaTypes.ITEM, "potioncapsule.jei_desc.jelly_apple");
		registration.addIngredientInfo(new ItemStack(ModItems.CACTUS_JELLY), VanillaTypes.ITEM, "potioncapsule.jei_desc.jelly_cactus");
		registration.addIngredientInfo(new ItemStack(ModItems.CAPSULE), VanillaTypes.ITEM, "potioncapsule.jei_desc.capsule",
				  																		   "potioncapsule.jei_desc.emptyline",
																						   "potioncapsule.jei_desc.capsule.transfer");
		registration.addIngredientInfo(new ItemStack(ModItems.CAPSULE_INSTANT), VanillaTypes.ITEM, "potioncapsule.jei_desc.capsule_instant",
																								   "potioncapsule.jei_desc.emptyline",
				   																				   "potioncapsule.jei_desc.capsule_instant.transfer");
		registration.addIngredientInfo(new ItemStack(ModItems.GELATIN_POWDER), VanillaTypes.ITEM, "potioncapsule.jei_desc.gelatin_powder");
		
		// Pendant
		if (ICurioProxy.isCurioLoaded()) {
			registration.addIngredientInfo(new ItemStack(ModItems.PENDANT), VanillaTypes.ITEM, "potioncapsule.jei_desc.pendant",
																							   "potioncapsule.jei_desc.emptyline",
																							   "potioncapsule.jei_desc.pendant_curio");
		}
		else {
			registration.addIngredientInfo(new ItemStack(ModItems.PENDANT), VanillaTypes.ITEM, "potioncapsule.jei_desc.pendant");
		}
		
		// Wart dust
		if (CommonConfigs.recipe_enableWartDust.get()) {
			registration.addIngredientInfo(new ItemStack(ModItems.WART_DUST), VanillaTypes.ITEM, "potioncapsule.jei_desc.wart_dust",
																								 "potioncapsule.jei_desc.emptyline",
																								 "potioncapsule.jei_desc.wart_dust_craft");
		}
		else {
			registration.addIngredientInfo(new ItemStack(ModItems.WART_DUST), VanillaTypes.ITEM, "potioncapsule.jei_desc.wart_dust");
		}
		
		// Catalysts
		registration.addIngredientInfo(new ItemStack(ModItems.CATALYST), VanillaTypes.ITEM, "potioncapsule.jei_desc.catalyst");
		registration.addIngredientInfo(new ItemStack(ModItems.CREATIVE_CATALYST), VanillaTypes.ITEM, "potioncapsule.jei_desc.c_catalyst");
		
		// Gelatin Extract and Former
		registration.addIngredientInfo(new ItemStack(ModBlocks.GELATIN_EXTRACTOR), VanillaTypes.ITEM, "potioncapsule.jei_desc.gel_extractor");
		registration.addIngredientInfo(new ItemStack(ModBlocks.GELATIN_FORMER), VanillaTypes.ITEM, "potioncapsule.jei_desc.gel_former");
		
		// Special Capsule
		for (EnumSpecialType type: EnumSpecialType.values()) {
			if (type.canTrigger)
				registration.addIngredientInfo(new ItemStack(ItemSpecialCapsule.getCapsuleInstance(type)), VanillaTypes.ITEM, "potioncapsule.jei_desc.special",
																															  "potioncapsule.jei_desc.emptyline",
																															  "potioncapsule.jei_desc.special_repair",
																															  "potioncapsule.jei_desc.emptyline",
																															  "potioncapsule.jei_desc.special.man");
			else
				registration.addIngredientInfo(new ItemStack(ItemSpecialCapsule.getCapsuleInstance(type)), VanillaTypes.ITEM, "potioncapsule.jei_desc.special",
																															  "potioncapsule.jei_desc.emptyline",
																															  "potioncapsule.jei_desc.special_repair");
		}
		
		// Wither Dust
		registration.addIngredientInfo(new ItemStack(ModItems.WITHER_FRAG), VanillaTypes.ITEM, "potioncapsule.jei_desc.wither_frag");
		
		// Tiny Cactus
		registration.addIngredientInfo(new ItemStack(ModBlocks.TINY_CACTI), VanillaTypes.ITEM, "potioncapsule.jei_desc.tiny_cactus");
		registration.addIngredientInfo(new ItemStack(ModItems.CACTUS_FRAG), VanillaTypes.ITEM, "potioncapsule.jei_desc.cactus_frag");
		
		// Fiery Lilypad
		registration.addIngredientInfo(new ItemStack(ModBlocks.FIERY_LILY), VanillaTypes.ITEM, "potioncapsule.jei_desc.fiery_lily");
		
		// Cat Fur
		registration.addIngredientInfo(new ItemStack(ModItems.CAT_FUR), VanillaTypes.ITEM, "potioncapsule.jei_desc.cat_fur");
		
		// Special Repairer
		registration.addIngredientInfo(new ItemStack(ModBlocks.CAPSULE_REPAIR), VanillaTypes.ITEM, "potioncapsule.jei_desc.special_repairer");
		
		// Capsule Subtypes
		registration.addIngredientInfo(PotionCapsule.getQueryedEffects().stream().filter(effect -> !effect.getPotion().isInstant()).map(effect -> PotionUtils.appendEffects(new ItemStack(ModItems.CAPSULE), Arrays.asList(effect))).collect(Collectors.toList()), VanillaTypes.ITEM, "potioncapsule.jei_desc.capsule.transfered");
		registration.addIngredientInfo(PotionCapsule.getQueryedEffects().stream().filter(effect -> effect.getPotion().isInstant()).map(effect -> PotionUtils.appendEffects(new ItemStack(ModItems.CAPSULE_INSTANT), Arrays.asList(effect))).collect(Collectors.toList()), VanillaTypes.ITEM, "potioncapsule.jei_desc.capsule_instant.transfered");
	}
	
}
